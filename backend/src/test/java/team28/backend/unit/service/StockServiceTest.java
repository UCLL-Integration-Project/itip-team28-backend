package team28.backend.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import team28.backend.exceptions.ServiceException;
import team28.backend.model.Car;
import team28.backend.model.Item;
import team28.backend.model.Reader;
import team28.backend.model.Stock;
import team28.backend.model.StockTransferRequest;
import team28.backend.model.TransferDirection;
import team28.backend.model.TransferStatus;
import team28.backend.repository.CarRepository;
import team28.backend.repository.ItemRepository;
import team28.backend.repository.ReaderRepository;
import team28.backend.repository.StockRepository;
import team28.backend.repository.StockTransferRequestRepository;
import team28.backend.service.StockService;

@ExtendWith(MockitoExtension.class)
public class StockServiceTest {
@Mock
    private StockRepository stockRepository;

    @Mock
    private StockTransferRequestRepository stockTransferRequestRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ReaderRepository readerRepository;

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private StockService stockService;

    private Reader reader;
    private Car car;
    private Item item;

    @BeforeEach
    public void setUp() {
        reader = mock(Reader.class);

        car = mock(Car.class);

        item = new Item("backpack");
        item.setId(1L);
    }

    @Test
    public void testGetStocksForHolder() {
        List<Stock> stocks = new ArrayList<>();
        stocks.add(new Stock(reader, item, 50));
        when(stockRepository.findByHolder(reader)).thenReturn(stocks);

        List<Stock> result = stockService.getStocksForHolder(reader);

        assertEquals(1, result.size());
        assertEquals(50, result.get(0).getQuantity());
        verify(stockRepository).findByHolder(reader);
    }

    @Test
    public void testAddStockToHolder_newStock() {
        when(stockRepository.findByHolderAndItem(reader, item)).thenReturn(Optional.empty());
        when(stockRepository.save(any(Stock.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Stock result = stockService.addStockToHolder(reader, item, 50);

        assertEquals(item, result.getItem());
        assertEquals(50, result.getQuantity());
        assertEquals(reader, result.getHolder());
        verify(stockRepository).save(any(Stock.class));
        verify(reader).addStock(any(Stock.class));
    }

    @Test
    public void testAddStockToHolder_existingStock() {
        Stock existingStock = new Stock(reader, item, 50);
        when(stockRepository.findByHolderAndItem(reader, item)).thenReturn(Optional.of(existingStock));
        when(stockRepository.save(any(Stock.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Stock result = stockService.addStockToHolder(reader, item, 25);

        assertEquals(75, result.getQuantity());
        verify(stockRepository).save(existingStock);
        verify(reader, never()).addStock(any(Stock.class));
    }

    @Test
    public void testAddStockToHolder_negativeQuantity() {
        assertThrows(ServiceException.class, () -> stockService.addStockToHolder(reader, item, -10));
    }

    @Test
    public void testRequestStockPickup_success() {
        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        when(readerRepository.findById(1L)).thenReturn(Optional.of(reader));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        Stock readerStock = new Stock(reader, item, 50);
        when(stockRepository.findByHolderAndItem(reader, item)).thenReturn(Optional.of(readerStock));
        when(stockTransferRequestRepository.save(any(StockTransferRequest.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        StockTransferRequest result = stockService.requestStockPickup(1L, 1L, 1L, 10);

        assertEquals(car, result.getCar());
        assertEquals(reader, result.getReader());
        assertEquals(item, result.getItem());
        assertEquals(10, result.getQuantity());
        assertEquals(TransferDirection.PICKUP, result.getDirection());
        assertEquals(TransferStatus.PENDING, result.getStatus());
        verify(stockTransferRequestRepository).save(any(StockTransferRequest.class));
    }

    @Test
    public void testRequestStockPickup_carNotFound() {
        when(carRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> stockService.requestStockPickup(1L, 1L, 1L, 10));
    }

    @Test
    public void testRequestStockPickup_readerNotFound() {
        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        when(readerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> stockService.requestStockPickup(1L, 1L, 1L, 10));
    }

    @Test
    public void testRequestStockPickup_itemNotFound() {
        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        when(readerRepository.findById(1L)).thenReturn(Optional.of(reader));
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> stockService.requestStockPickup(1L, 1L, 1L, 10));
    }

    @Test
    public void testRequestStockPickup_noStock() {
        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        when(readerRepository.findById(1L)).thenReturn(Optional.of(reader));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(stockRepository.findByHolderAndItem(reader, item)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> stockService.requestStockPickup(1L, 1L, 1L, 10));
    }

    @Test
    public void testRequestStockPickup_insufficientStock() {
        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        when(readerRepository.findById(1L)).thenReturn(Optional.of(reader));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        Stock readerStock = new Stock(reader, item, 5);
        when(stockRepository.findByHolderAndItem(reader, item)).thenReturn(Optional.of(readerStock));

        assertThrows(ServiceException.class, () -> stockService.requestStockPickup(1L, 1L, 1L, 10));
    }

    @Test
    public void testRequestStockDelivery_success() {
        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        when(readerRepository.findById(1L)).thenReturn(Optional.of(reader));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        Stock carStock = new Stock(car, item, 50);
        when(stockRepository.findByHolderAndItem(car, item)).thenReturn(Optional.of(carStock));
        when(stockTransferRequestRepository.save(any(StockTransferRequest.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        StockTransferRequest result = stockService.requestStockDelivery(1L, 1L, 1L, 10);

        assertEquals(car, result.getCar());
        assertEquals(reader, result.getReader());
        assertEquals(item, result.getItem());
        assertEquals(10, result.getQuantity());
        assertEquals(TransferDirection.DELIVERY, result.getDirection());
        assertEquals(TransferStatus.PENDING, result.getStatus());
        verify(stockTransferRequestRepository).save(any(StockTransferRequest.class));
    }

    @Test
    public void testRequestStockDelivery_noStock() {
        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        when(readerRepository.findById(1L)).thenReturn(Optional.of(reader));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(stockRepository.findByHolderAndItem(car, item)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> stockService.requestStockDelivery(1L, 1L, 1L, 10));
    }

@Test
public void testCompleteStockTransfer_pickupSuccess() {
    StockTransferRequest request = new StockTransferRequest(car, reader, item, 10, LocalDateTime.now());
    request.setDirection(TransferDirection.PICKUP);
    request.setStatus(TransferStatus.PENDING);
    when(stockTransferRequestRepository.findById(1L)).thenReturn(Optional.of(request));

    Stock readerStock = new Stock(reader, item, 50);
    when(stockRepository.findByHolderAndItem(reader, item)).thenReturn(Optional.of(readerStock));

    when(stockRepository.findByHolderAndItem(car, item)).thenReturn(Optional.empty());
    when(stockRepository.save(any(Stock.class))).thenAnswer(invocation -> invocation.getArgument(0));
    when(stockTransferRequestRepository.save(any(StockTransferRequest.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

    stockService.completeStockTransfer(1L);

    assertEquals(40, readerStock.getQuantity());
    verify(stockRepository).save(readerStock); 
    verify(stockRepository, times(3)).save(any(Stock.class)); 
    assertEquals(TransferStatus.COMPLETE, request.getStatus());
    verify(stockTransferRequestRepository).save(request);
}

@Test
public void testCompleteStockTransfer_deliverySuccess() {
    StockTransferRequest request = new StockTransferRequest(car, reader, item, 10, LocalDateTime.now());
    request.setDirection(TransferDirection.DELIVERY);
    request.setStatus(TransferStatus.PENDING);
    when(stockTransferRequestRepository.findById(1L)).thenReturn(Optional.of(request));

    Stock carStock = new Stock(car, item, 50);
    when(stockRepository.findByHolderAndItem(car, item)).thenReturn(Optional.of(carStock));

    when(stockRepository.findByHolderAndItem(reader, item)).thenReturn(Optional.empty());
    when(stockRepository.save(any(Stock.class))).thenAnswer(invocation -> invocation.getArgument(0));
    when(stockTransferRequestRepository.save(any(StockTransferRequest.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

    stockService.completeStockTransfer(1L);

    assertEquals(40, carStock.getQuantity());
    verify(stockRepository).save(carStock); 
    verify(stockRepository, times(3)).save(any(Stock.class)); 
    assertEquals(TransferStatus.COMPLETE, request.getStatus());
    verify(stockTransferRequestRepository).save(request);
}

    @Test
    public void testCompleteStockTransfer_requestNotFound() {
        when(stockTransferRequestRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> stockService.completeStockTransfer(1L));
    }

    @Test
    public void testCompleteStockTransfer_notPending() {
        StockTransferRequest request = new StockTransferRequest(car, reader, item, 10, LocalDateTime.now());
        request.setDirection(TransferDirection.PICKUP);
        request.setStatus(TransferStatus.COMPLETE);
        when(stockTransferRequestRepository.findById(1L)).thenReturn(Optional.of(request));

        assertThrows(ServiceException.class, () -> stockService.completeStockTransfer(1L));
    }
}
