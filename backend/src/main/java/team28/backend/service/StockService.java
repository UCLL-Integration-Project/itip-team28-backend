package team28.backend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import team28.backend.exceptions.ServiceException;
import team28.backend.model.*;
import team28.backend.repository.*;

@Service
public class StockService {
    private final StockRepository StockRepository;
    private final StockTransferRequestRepository StockTransferRequestRepository;
    private final ItemRepository ItemRepository;
    private final ReaderRepository ReaderRepository;
    private final CarRepository CarRepository;
    private final RouteRepository RouteRepository;
    private final ScanRepository ScanRepository;
    private final RestTemplate restTemplate;

    private final String externalUrl = "https://carservice-itip-project28.apps.okd.ucll.cloud/routes";

    public StockService(StockRepository StockRepository, StockTransferRequestRepository StockTransferRequestRepository,
            ItemRepository ItemRepository, ReaderRepository ReaderRepository,
            CarRepository CarRepository, RouteRepository RouteRepository,
            ScanRepository ScanRepository, RestTemplate restTemplate) {
        this.StockRepository = StockRepository;
        this.StockTransferRequestRepository = StockTransferRequestRepository;
        this.ItemRepository = ItemRepository;
        this.ReaderRepository = ReaderRepository;
        this.CarRepository = CarRepository;
        this.RouteRepository = RouteRepository;
        this.ScanRepository = ScanRepository;
        this.restTemplate = restTemplate;
    }

    public List<Stock> getStocksForHolder(StockHolderInt holder) {
        return StockRepository.findByHolder(holder);
    }

    public Stock addStockToHolder(StockHolderInt holder, Item item, int quantity) {
        if (quantity < 0) {
            throw new ServiceException("Quantity cannot be negative");
        }

        Optional<Stock> existingStock = StockRepository.findByHolderAndItem(holder, item);
        if (existingStock.isPresent()) {
            Stock stock = existingStock.get();
            stock.setQuantity(stock.getQuantity() + quantity);
            stock.setHolder(holder);
            return StockRepository.save(stock);
        } else {
            Stock stock = new Stock(holder, item, quantity);
            holder.addStock(stock);
            return StockRepository.save(stock);
        }
    }

    public StockTransferRequest requestStockPickup(Long carId, Long readerId, Long itemId, int quantity) {
        if (quantity <= 0) {
            throw new ServiceException("Quantity cannot be negative");
        }

        Car car = CarRepository.findById(carId)
                .orElseThrow(() -> new ServiceException("Car with id " + carId + " not found"));
        Reader reader = ReaderRepository.findById(readerId)
                .orElseThrow(() -> new ServiceException("Reader with id " + readerId + " not found"));
        Item item = ItemRepository.findById(itemId)
                .orElseThrow(() -> new ServiceException("Item with id " + itemId + " not found"));

        Stock readerStock = StockRepository.findByHolderAndItem(reader, item)
                .orElseThrow(() -> new ServiceException("Reader does not have stock for item " + item.getName()));

        if (readerStock.getQuantity() < quantity) {
            throw new ServiceException("Reader does not have enough stock. Available: " + readerStock.getQuantity());
        }

        StockTransferRequest request = new StockTransferRequest(car, reader, item, quantity, LocalDateTime.now());
        request.setDirection(TransferDirection.PICKUP);

        Reader currentLocation = ScanRepository.findLatest().getReader();
        Route route = RouteRepository.findByStartingPointAndCurrentPoint(currentLocation, reader);
        sendRouteToExternalService(route.getInstructions());

        return StockTransferRequestRepository.save(request);
    }

    public StockTransferRequest requestStockDelivery(Long carId, Long readerId, Long itemId, int quantity) {
        if (quantity <= 0) {
            throw new ServiceException("Quantity cannot be negative");
        }

        Car car = CarRepository.findById(carId)
                .orElseThrow(() -> new ServiceException("Car with id " + carId + " not found"));
        Reader reader = ReaderRepository.findById(readerId)
                .orElseThrow(() -> new ServiceException("Reader with id " + readerId + " not found"));
        Item item = ItemRepository.findById(itemId)
                .orElseThrow(() -> new ServiceException("Item with id " + itemId + " not found"));

        Stock carStock = StockRepository.findByHolderAndItem(car, item)
                .orElseThrow(() -> new ServiceException("Car does not have stock for item " + item.getName()));

        if (carStock.getQuantity() < quantity) {
            throw new ServiceException("Car does not have enough stock. Available: " + carStock.getQuantity());
        }

        StockTransferRequest request = new StockTransferRequest(car, reader, item, quantity, LocalDateTime.now());
        request.setDirection(TransferDirection.DELIVERY);

        Reader currentLocation = ScanRepository.findLatest().getReader();
        Route route = RouteRepository.findByStartingPointAndCurrentPoint(currentLocation, reader);
        sendRouteToExternalService(route.getInstructions());

        return StockTransferRequestRepository.save(request);
    }

    public void completeStockTransfer(Long requestId) {
        StockTransferRequest request = StockTransferRequestRepository.findById(requestId)
                .orElseThrow(() -> new ServiceException("Stock transfer request with id " + requestId + " not found"));

        if (request.getStatus() != TransferStatus.PENDING) {
            throw new ServiceException("Stock transfer request with id " + requestId + " is not in PENDING status");
        }

        Car car = request.getCar();
        Reader reader = request.getReader();
        Item item = request.getItem();
        int quantity = request.getQuantity();

        if (request.getDirection() == TransferDirection.PICKUP) {
            Stock readerStock = StockRepository.findByHolderAndItem(reader, item)
                    .orElseThrow(() -> new ServiceException("Reader does not have stock for item " + item.getName()));

            if (readerStock.getQuantity() < quantity) {
                throw new ServiceException(
                        "Reader does not have enough stock. Available: " + readerStock.getQuantity());
            }

            readerStock.setQuantity(readerStock.getQuantity() - quantity);
            if (readerStock.getQuantity() == 0) {
                StockRepository.delete(readerStock);
            } else {
                StockRepository.save(readerStock);
            }

            Stock carStock = StockRepository.findByHolderAndItem(car, item)
                    .orElseGet(() -> StockRepository.save(new Stock(car, item, 0)));
            carStock.setQuantity(carStock.getQuantity() + quantity);
            StockRepository.save(carStock);

        } else if (request.getDirection() == TransferDirection.DELIVERY) {
            Stock carStock = StockRepository.findByHolderAndItem(car, item)
                    .orElseThrow(() -> new ServiceException("Car does not have stock for item " + item.getName()));

            if (carStock.getQuantity() < quantity) {
                throw new ServiceException("Car does not have enough stock. Available: " + carStock.getQuantity());
            }

            carStock.setQuantity(carStock.getQuantity() - quantity);
            if (carStock.getQuantity() == 0) {
                StockRepository.delete(carStock);
            } else {
                StockRepository.save(carStock);
            }

            Stock readerStock = StockRepository.findByHolderAndItem(reader, item)
                    .orElseGet(() -> StockRepository.save(new Stock(reader, item, 0)));
            readerStock.setQuantity(readerStock.getQuantity() + quantity);
            StockRepository.save(readerStock);
        }

        request.setStatus(TransferStatus.COMPLETE);
        StockTransferRequestRepository.save(request);
    }

    private void sendRouteToExternalService(List<String> instructions) {
        try {
            restTemplate.postForEntity(externalUrl, instructions, Void.class);
        } catch (Exception e) {
            System.err.println("Failed to send route to external service: " + e.getMessage());
        }
    }
}
