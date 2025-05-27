package team28.backend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import team28.backend.exceptions.ServiceException;
import team28.backend.model.Car;
import team28.backend.model.Item;
import team28.backend.model.Reader;
import team28.backend.model.Stock;
import team28.backend.model.StockHolderInt;
import team28.backend.model.StockTransferRequest;
import team28.backend.model.TransferDirection;
import team28.backend.model.TransferStatus;
import team28.backend.repository.CarRepository;
import team28.backend.repository.ItemRepository;
import team28.backend.repository.ReaderRepository;
import team28.backend.repository.StockRepository;
import team28.backend.repository.StockTransferRequestRepository;

@Service
public class StockService {
    private final StockRepository StockRepository;
    private final StockTransferRequestRepository StockTransferRequestRepository;
    private final ItemRepository ItemRepository;
    private final ReaderRepository ReaderRepository;
    private final CarRepository CarRepository;

    public StockService(StockRepository StockRepository, StockTransferRequestRepository StockTransferRequestRepository,
            ItemRepository ItemRepository, ReaderRepository ReaderRepository, CarRepository CarRepository) {
        this.StockRepository = StockRepository;
        this.StockTransferRequestRepository = StockTransferRequestRepository;
        this.ItemRepository = ItemRepository;
        this.ReaderRepository = ReaderRepository;
        this.CarRepository = CarRepository;
    }

    //retrieves all the stock for this holder (car/reader)
    public List<Stock> getStocksForHolder(StockHolderInt holder) {
        return StockRepository.findByHolder(holder);
    }

    // adds stock to a holder (car/reader) for a specific item
    // if holder already has stock for the item, it increases the quantity
    // otherwise, it creates a new stock entry
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

    // creates a stock transfer request for picking up an item from a reader to a car
    // checks that reader has enough stock for the item
    public StockTransferRequest requestStockPickup(Long carId, Long readerId, Long itemId, int quantity) {
        if (quantity <= 0) {
            throw new ServiceException("Quantity cannot be negative");
        }

        Car car = CarRepository.findById(carId)
                .orElseThrow(() -> new ServiceException("Car with id" + carId + "not found"));
        Reader reader = ReaderRepository.findById(readerId)
                .orElseThrow(() -> new ServiceException("Reader with id" + readerId + "not found"));
        Item item = ItemRepository.findById(itemId)
                .orElseThrow(() -> new ServiceException("Item with id" + itemId + "not found"));

        Stock readerStock = StockRepository.findByHolderAndItem(reader, item)
                .orElseThrow(() -> new ServiceException("Reader does not have stock for item" + item.getName()));
        
        if (readerStock.getQuantity() < quantity) {
            throw new ServiceException("Reader does not have enough stock. Available: " + readerStock.getQuantity());
        }

        StockTransferRequest request = new StockTransferRequest(car, reader, item, quantity, LocalDateTime.now());
        request.setDirection(TransferDirection.PICKUP);
        return StockTransferRequestRepository.save(request);
    }

    // completes a stock transfer request by updating the stock of the car and reader based on the transfer direction
    // checks again if the car/reader has enough stock before completing the transfer
    public void completeStockTransfer(Long requestId) {
        StockTransferRequest request = StockTransferRequestRepository.findById(requestId)
                .orElseThrow(() -> new ServiceException("Stock transfer request with id" + requestId + "not found"));

        if (request.getStatus() != TransferStatus.PENDING) {
            throw new ServiceException("Stock transfer request with id" + requestId + "is not in PENDING status");
        }

        Car car = request.getCar();
        Reader reader = request.getReader();
        Item item = request.getItem();
        int quantity = request.getQuantity();

        // Checks if PICKUP
        if (request.getDirection() == TransferDirection.PICKUP) {
            Stock readerStock = StockRepository.findByHolderAndItem(reader, item)
                    .orElseThrow(() -> new ServiceException("Reader does not have stock for item" + item.getName()));
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
                    .orElseGet(() -> {
                        Stock newStock = new Stock(car, item, 0);
                        return StockRepository.save(newStock);
                    });
            carStock.setQuantity(carStock.getQuantity() + quantity);
            StockRepository.save(carStock);

        //Else DELIVERY
        } else if (request.getDirection() == TransferDirection.DELIVERY) {
            Stock carStock = StockRepository.findByHolderAndItem(car, item)
                    .orElseThrow(() -> new ServiceException("Car does not have stock for item" + item.getName()));

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
                    .orElseGet(() -> {
                        Stock newStock = new Stock(reader, item, 0);
                        return StockRepository.save(newStock);
                    });
            readerStock.setQuantity(readerStock.getQuantity() + quantity);
            StockRepository.save(readerStock);
        }
        request.setStatus(TransferStatus.COMPLETE);
        StockTransferRequestRepository.save(request);
    }

    // creates a stock delivery request for dropping of an item from a cat to a reader
    // checks that car has enough stock for the item
    public StockTransferRequest requestStockDelivery(Long carId, Long readerId, Long itemId, int quantity) {
        if (quantity <= 0) {
            throw new ServiceException("Quantity cannot be negative");
        }
        Car car = CarRepository.findById(carId)
                .orElseThrow(() -> new ServiceException("Car with id" + carId + "not found"));
        Reader reader = ReaderRepository.findById(readerId)
                .orElseThrow(() -> new ServiceException("Reader with id" + readerId + "not found"));
        Item item = ItemRepository.findById(itemId)
                .orElseThrow(() -> new ServiceException("Item with id" + itemId + "not found"));

        Stock carStock = StockRepository.findByHolderAndItem(car, item)
                .orElseThrow(() -> new ServiceException("Car does not have stock for item" + item.getName()));
        if (carStock.getQuantity() < quantity) {
            throw new ServiceException("Car does not have enough stock. Available: " + carStock.getQuantity());
        }

        StockTransferRequest request = new StockTransferRequest(car, reader, item, quantity, LocalDateTime.now());
        request.setDirection(TransferDirection.DELIVERY);
        return StockTransferRequestRepository.save(request);
    }

}
