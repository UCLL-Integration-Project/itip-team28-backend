package team28.backend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.cglib.core.Local;
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
    private final StockRepository stockRepository;
    private final StockTransferRequestRepository stockTransferRequestRepository;
    private final ItemRepository itemRepository;
    private final ReaderRepository readerRepository;
    private final CarRepository carRepository;

    public StockService(StockRepository stockRepository, StockTransferRequestRepository stockTransferRequestRepository,
                        ItemRepository itemRepository, ReaderRepository readerRepository, CarRepository carRepository) {
        this.stockRepository = stockRepository;
        this.stockTransferRequestRepository = stockTransferRequestRepository;
        this.itemRepository = itemRepository;
        this.readerRepository = readerRepository;
        this.carRepository = carRepository;
    }

    public List<Stock> getStocksForHolder(StockHolderInt holder){
        return stockRepository.findByHolder(holder);
    }

    public Stock addStockToHolder(StockHolderInt holder, Item item, int quantity){
        if (quantity < 0) {
            throw new ServiceException("Quantity cannot be negative");
        }

        Optional<Stock> existingStock = stockRepository.findByHolderAndItem(holder, item);
        if (existingStock.isPresent()){
            Stock stock = existingStock.get();
            stock.setQuantity(stock.getQuantity() + quantity);
            stock.setHolder(holder);
            return stockRepository.save(stock);
        } else {
            Stock stock = new Stock(holder, item, quantity );
            holder.addStock(stock);
            return stockRepository.save(stock);
        }
    }

    public StockTransferRequest requestStockPickup(Long carId, Long readerId, Long itemId, int quantity){
        if (quantity <= 0) {
            throw new ServiceException("Quantity cannot be negative");
        }

        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new ServiceException("Car with id" + carId + "not found"));
        Reader reader = readerRepository.findById(readerId)
                .orElseThrow(() -> new ServiceException("Reader with id" + readerId + "not found"));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ServiceException("Item with id" + itemId + "not found"));

        Stock readerStock = stockRepository.findByHolderAndItem(reader, item)
                .orElseThrow(() -> new ServiceException("Reader does not have stock for item" + item.getName()));
        if (readerStock.getQuantity() < quantity){
            throw new ServiceException("Reader does not have enough stock. Available: " + readerStock.getQuantity());
        }
        StockTransferRequest request = new StockTransferRequest(car, reader, item, quantity, LocalDateTime.now());
        request.setDirection(TransferDirection.PICKUP);
        return stockTransferRequestRepository.save(request);
    }

    public void completeStockTransfer(Long requestId){
        StockTransferRequest request = stockTransferRequestRepository.findById(requestId)
                .orElseThrow(() -> new ServiceException("Stock transfer request with id" + requestId + "not found"));

        if (request.getStatus() != TransferStatus.PENDING){
            throw new ServiceException("Stock transfer request with id" + requestId + "is not in PENDING status");
        }

        Car car = request.getCar();
        Reader reader = request.getReader();
        Item item = request.getItem();
        int quantity = request.getQuantity();

        if(request.getDirection() == TransferDirection.PICKUP){
            Stock readerStock = stockRepository.findByHolderAndItem(reader, item)
                .orElseThrow(() -> new ServiceException("Reader does not have stock for item" + item.getName()));
            if (readerStock.getQuantity() < quantity){
                throw new ServiceException("Reader does not have enough stock. Available: " + readerStock.getQuantity());
        }

        readerStock.setQuantity(readerStock.getQuantity() - quantity);
        if (readerStock.getQuantity() == 0) {
            stockRepository.delete(readerStock);
        } else {
            stockRepository.save(readerStock);
        }

        Stock carStock = stockRepository.findByHolderAndItem(car, item)
            .orElseGet(() -> {
                Stock newStock = new Stock(car, item, 0 );
                return stockRepository.save(newStock);
            });
        carStock.setQuantity(carStock.getQuantity() + quantity);
        stockRepository.save(carStock);

    } else if (request.getDirection() == TransferDirection.DELIVERY) {
        Stock carStock = stockRepository.findByHolderAndItem(car, item)
            .orElseThrow(() -> new ServiceException("Car does not have stock for item" + item.getName()));

        if (carStock.getQuantity() < quantity){
            throw new ServiceException("Car does not have enough stock. Available: " + carStock.getQuantity());
        }

        carStock.setQuantity(carStock.getQuantity() - quantity);
        if (carStock.getQuantity() == 0) {
            stockRepository.delete(carStock);
        } else {
            stockRepository.save(carStock);
        }

        Stock readerStock = stockRepository.findByHolderAndItem(reader, item)
            .orElseGet(() -> {
                Stock newStock = new Stock(reader, item, 0);
                return stockRepository.save(newStock);
            });
        readerStock.setQuantity(readerStock.getQuantity() + quantity);
        stockRepository.save(readerStock);
    }
        request.setStatus(TransferStatus.COMPLETE);
        stockTransferRequestRepository.save(request);
    }

    public StockTransferRequest requestStockDelivery(Long carId, Long readerId, Long itemId, int quantity){
        if (quantity <= 0) {
            throw new ServiceException("Quantity cannot be negative");
        }
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new ServiceException("Car with id" + carId + "not found"));
        Reader reader = readerRepository.findById(readerId)
                .orElseThrow(() -> new ServiceException("Reader with id" + readerId + "not found"));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ServiceException("Item with id" + itemId + "not found"));
        
        Stock carStock = stockRepository.findByHolderAndItem(car, item)
            .orElseThrow(() -> new ServiceException("Car does not have stock for item" + item.getName()));
        if (carStock.getQuantity() < quantity){
            throw new ServiceException("Car does not have enough stock. Available: " + carStock.getQuantity());
        }

        StockTransferRequest request = new StockTransferRequest(car, reader, item, quantity, LocalDateTime.now());
        request.setDirection(TransferDirection.DELIVERY);
        return stockTransferRequestRepository.save(request);
    }
}
