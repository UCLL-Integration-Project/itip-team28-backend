package team28.backend.service;

import java.lang.foreign.Linker.Option;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.cglib.core.Local;

import team28.backend.exceptions.ServiceException;
import team28.backend.model.Car;
import team28.backend.model.Item;
import team28.backend.model.Reader;
import team28.backend.model.Stock;
import team28.backend.model.StockHolderInt;
import team28.backend.model.StockTransferRequest;
import team28.backend.model.TransferStatus;
import team28.backend.repository.CarRepository;
import team28.backend.repository.ItemRepository;
import team28.backend.repository.ReaderRepository;
import team28.backend.repository.StockRepository;
import team28.backend.repository.StockTransferRequestRepository;

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
        return stockRepository.findByHolderInt(holder);
    }

    public Stock addStockToHolder(StockHolderInt holder, Item item, int quantity){
        if (quantity < 0) {
            throw new ServiceException("Quantity cannot be negative");
        }

        Optional<Stock> existingStock = stockRepository.findByHolderIntAndItem(holder, item);
        if (existingStock.isPresent()){
            Stock stock = existingStock.get();
            stock.setQuantity(stock.getQuantity() + quantity);
            return stockRepository.save(stock);
        } else {
            Stock stock = new Stock(item, quantity, holder);
            holder.addStock(stock);
            return stockRepository.save(stock);
        }
    }

    public StockTransferRequest requestStockTransfer(Long carId, Long readerId, Long itemId, int quantity){
        if (quantity < 0) {
            throw new ServiceException("Quantity cannot be negative");
        }

        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new ServiceException("Car with id" + carId + "not found"));
        Reader reader = readerRepository.findById(readerId)
                .orElseThrow(() -> new ServiceException("Reader with id" + readerId + "not found"));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ServiceException("Item with id" + itemId + "not found"));

        StockTransferRequest stockTransferRequest = new StockTransferRequest(car, reader, item, quantity, LocalDateTime.now());
        return stockTransferRequestRepository.save(stockTransferRequest);
    }

    public void completeStockTransfer(Long requestId){
        StockTransferRequest request = stockTransferRequestRepository.findById(requestId)
                .orElseThrow(() -> new ServiceException("Stock transfer request with id" + requestId + "not found"));

        if (request.getStatus() == TransferStatus.COMPLETE){
            throw new ServiceException("Stock transfer request with id" + requestId + "already completed");
        }

        Car car = request.getCar();
        Reader reader = request.getReader();
        Item item = request.getItem();
        int quantity = request.getQuantity();

        Stock readerStock = stockRepository.findByHolderIntAndItem(reader, item)
            .orElseThrow(() -> new ServiceException("Reader does not have stock for item" + item.getName()));
        if (readerStock.getQuantity() < quantity){
            throw new ServiceException("Reader does not have enough stock. Available: " + readerStock.getQuantity());
        }

        readerStock.setQuantity(readerStock.getQuantity() - quantity);
        stockRepository.save(readerStock);

        addStockToHolder(car, item, quantity);

        request.setStatus(TransferStatus.COMPLETE);
        stockTransferRequestRepository.save(request);
    }
}
