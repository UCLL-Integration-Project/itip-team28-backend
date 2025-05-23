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
    private StockRepository StockRepository;

    @Mock
    private StockTransferRequestRepository StockTransferRequestRepository;

    @Mock
    private ItemRepository ItemRepository;

    @Mock
    private ReaderRepository ReaderRepository;

    @Mock
    private CarRepository CarRepository;

    @InjectMocks
    private StockService StockService;

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
        when(StockRepository.findByHolder(reader)).thenReturn(stocks);

        List<Stock> result = StockService.getStocksForHolder(reader);

        assertEquals(1, result.size());
        assertEquals(50, result.get(0).getQuantity());
        verify(StockRepository).findByHolder(reader);
    }

    @Test
    public void testAddStockToHolder_newStock() {
        when(StockRepository.findByHolderAndItem(reader, item)).thenReturn(Optional.empty());
        when(StockRepository.save(any(Stock.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Stock result = StockService.addStockToHolder(reader, item, 50);

        assertEquals(item, result.getItem());
        assertEquals(50, result.getQuantity());
        assertEquals(reader, result.getHolder());
        verify(StockRepository).save(any(Stock.class));
        verify(reader).addStock(any(Stock.class));
    }

    @Test
    public void testAddStockToHolder_existingStock() {
        Stock existingStock = new Stock(reader, item, 50);
        when(StockRepository.findByHolderAndItem(reader, item)).thenReturn(Optional.of(existingStock));
        when(StockRepository.save(any(Stock.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Stock result = StockService.addStockToHolder(reader, item, 25);

        assertEquals(75, result.getQuantity());
        verify(StockRepository).save(existingStock);
        verify(reader, never()).addStock(any(Stock.class));
    }

    @Test
    public void testAddStockToHolder_negativeQuantity() {
        assertThrows(ServiceException.class, () -> StockService.addStockToHolder(reader, item, -10));
    }

    @Test
    public void testRequestStockPickup_success() {
        when(CarRepository.findById(1L)).thenReturn(Optional.of(car));
        when(ReaderRepository.findById(1L)).thenReturn(Optional.of(reader));
        when(ItemRepository.findById(1L)).thenReturn(Optional.of(item));
        Stock readerStock = new Stock(reader, item, 50);
        when(StockRepository.findByHolderAndItem(reader, item)).thenReturn(Optional.of(readerStock));
        when(StockTransferRequestRepository.save(any(StockTransferRequest.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        StockTransferRequest result = StockService.requestStockPickup(1L, 1L, 1L, 10);

        assertEquals(car, result.getCar());
        assertEquals(reader, result.getReader());
        assertEquals(item, result.getItem());
        assertEquals(10, result.getQuantity());
        assertEquals(TransferDirection.PICKUP, result.getDirection());
        assertEquals(TransferStatus.PENDING, result.getStatus());
        verify(StockTransferRequestRepository).save(any(StockTransferRequest.class));
    }

    @Test
    public void testRequestStockPickup_carNotFound() {
        when(CarRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> StockService.requestStockPickup(1L, 1L, 1L, 10));
    }

    @Test
    public void testRequestStockPickup_readerNotFound() {
        when(CarRepository.findById(1L)).thenReturn(Optional.of(car));
        when(ReaderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> StockService.requestStockPickup(1L, 1L, 1L, 10));
    }

    @Test
    public void testRequestStockPickup_itemNotFound() {
        when(CarRepository.findById(1L)).thenReturn(Optional.of(car));
        when(ReaderRepository.findById(1L)).thenReturn(Optional.of(reader));
        when(ItemRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> StockService.requestStockPickup(1L, 1L, 1L, 10));
    }

    @Test
    public void testRequestStockPickup_noStock() {
        when(CarRepository.findById(1L)).thenReturn(Optional.of(car));
        when(ReaderRepository.findById(1L)).thenReturn(Optional.of(reader));
        when(ItemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(StockRepository.findByHolderAndItem(reader, item)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> StockService.requestStockPickup(1L, 1L, 1L, 10));
    }

    @Test
    public void testRequestStockPickup_insufficientStock() {
        when(CarRepository.findById(1L)).thenReturn(Optional.of(car));
        when(ReaderRepository.findById(1L)).thenReturn(Optional.of(reader));
        when(ItemRepository.findById(1L)).thenReturn(Optional.of(item));
        Stock readerStock = new Stock(reader, item, 5);
        when(StockRepository.findByHolderAndItem(reader, item)).thenReturn(Optional.of(readerStock));

        assertThrows(ServiceException.class, () -> StockService.requestStockPickup(1L, 1L, 1L, 10));
    }

    @Test
    public void testRequestStockDelivery_success() {
        when(CarRepository.findById(1L)).thenReturn(Optional.of(car));
        when(ReaderRepository.findById(1L)).thenReturn(Optional.of(reader));
        when(ItemRepository.findById(1L)).thenReturn(Optional.of(item));
        Stock carStock = new Stock(car, item, 50);
        when(StockRepository.findByHolderAndItem(car, item)).thenReturn(Optional.of(carStock));
        when(StockTransferRequestRepository.save(any(StockTransferRequest.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        StockTransferRequest result = StockService.requestStockDelivery(1L, 1L, 1L, 10);

        assertEquals(car, result.getCar());
        assertEquals(reader, result.getReader());
        assertEquals(item, result.getItem());
        assertEquals(10, result.getQuantity());
        assertEquals(TransferDirection.DELIVERY, result.getDirection());
        assertEquals(TransferStatus.PENDING, result.getStatus());
        verify(StockTransferRequestRepository).save(any(StockTransferRequest.class));
    }

    @Test
    public void testRequestStockDelivery_noStock() {
        when(CarRepository.findById(1L)).thenReturn(Optional.of(car));
        when(ReaderRepository.findById(1L)).thenReturn(Optional.of(reader));
        when(ItemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(StockRepository.findByHolderAndItem(car, item)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> StockService.requestStockDelivery(1L, 1L, 1L, 10));
    }

    @Test
    public void testCompleteStockTransfer_pickupSuccess() {
        StockTransferRequest request = new StockTransferRequest(car, reader, item, 10, LocalDateTime.now());
        request.setDirection(TransferDirection.PICKUP);
        request.setStatus(TransferStatus.PENDING);
        when(StockTransferRequestRepository.findById(1L)).thenReturn(Optional.of(request));

        Stock readerStock = new Stock(reader, item, 50);
        when(StockRepository.findByHolderAndItem(reader, item)).thenReturn(Optional.of(readerStock));

        when(StockRepository.findByHolderAndItem(car, item)).thenReturn(Optional.empty());
        when(StockRepository.save(any(Stock.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(StockTransferRequestRepository.save(any(StockTransferRequest.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        StockService.completeStockTransfer(1L);

        assertEquals(40, readerStock.getQuantity());
        verify(StockRepository).save(readerStock);
        verify(StockRepository, times(3)).save(any(Stock.class));
        assertEquals(TransferStatus.COMPLETE, request.getStatus());
        verify(StockTransferRequestRepository).save(request);
    }

    @Test
    public void testCompleteStockTransfer_deliverySuccess() {
        StockTransferRequest request = new StockTransferRequest(car, reader, item, 10, LocalDateTime.now());
        request.setDirection(TransferDirection.DELIVERY);
        request.setStatus(TransferStatus.PENDING);
        when(StockTransferRequestRepository.findById(1L)).thenReturn(Optional.of(request));

        Stock carStock = new Stock(car, item, 50);
        when(StockRepository.findByHolderAndItem(car, item)).thenReturn(Optional.of(carStock));

        when(StockRepository.findByHolderAndItem(reader, item)).thenReturn(Optional.empty());
        when(StockRepository.save(any(Stock.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(StockTransferRequestRepository.save(any(StockTransferRequest.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        StockService.completeStockTransfer(1L);

        assertEquals(40, carStock.getQuantity());
        verify(StockRepository).save(carStock);
        verify(StockRepository, times(3)).save(any(Stock.class));
        assertEquals(TransferStatus.COMPLETE, request.getStatus());
        verify(StockTransferRequestRepository).save(request);
    }

    @Test
    public void testCompleteStockTransfer_requestNotFound() {
        when(StockTransferRequestRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> StockService.completeStockTransfer(1L));
    }

    @Test
    public void testCompleteStockTransfer_notPending() {
        StockTransferRequest request = new StockTransferRequest(car, reader, item, 10, LocalDateTime.now());
        request.setDirection(TransferDirection.PICKUP);
        request.setStatus(TransferStatus.COMPLETE);
        when(StockTransferRequestRepository.findById(1L)).thenReturn(Optional.of(request));

        assertThrows(ServiceException.class, () -> StockService.completeStockTransfer(1L));
    }
}
