package team28.backend.unit.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import team28.backend.model.Car;
import team28.backend.model.Item;
import team28.backend.model.Reader;
import team28.backend.model.StockTransferRequest;
import team28.backend.model.TransferDirection;
import team28.backend.model.TransferStatus;

@ExtendWith(MockitoExtension.class)
public class StockTransferRequestTest {
    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @Mock
    private Car car;

    @Mock
    private Reader reader;

    @Mock
    private Item item;

    @BeforeAll
    public static void createValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    public void testStockTransferRequestCreation() {
        LocalDateTime timestamp = LocalDateTime.now();
        StockTransferRequest request = new StockTransferRequest(car, reader, item, 5, timestamp);

        assertEquals(car, request.getCar());
        assertEquals(reader, request.getReader());
        assertEquals(item, request.getItem());
        assertEquals(5, request.getQuantity());
        assertEquals(timestamp, request.getTimestamp());
        assertEquals(TransferStatus.PENDING, request.getStatus());
    }

    @Test
    public void givenNegativeQuantity_whenCreatingStockTransferRequest_thenThrowException() {
        LocalDateTime timestamp = LocalDateTime.now();
        StockTransferRequest request = new StockTransferRequest(car, reader, item, -5, timestamp);

        Set<ConstraintViolation<StockTransferRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        ConstraintViolation<StockTransferRequest> violation = violations.iterator().next();
        assertEquals("Stock quantity must be a positive number", violation.getMessage());
    }

    @Test
    public void givenZeroQuantity_whenCreatingStockTransferRequest_thenThrowException() {
        LocalDateTime timestamp = LocalDateTime.now();
        StockTransferRequest request = new StockTransferRequest(car, reader, item, 0, timestamp);

        Set<ConstraintViolation<StockTransferRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        ConstraintViolation<StockTransferRequest> violation = violations.iterator().next();
        assertEquals("Stock quantity must be a positive number", violation.getMessage());
    }

    @Test
    public void givenNullTimestamp_whenCreatingStockTransferRequest_thenThrowException() {
        StockTransferRequest request = new StockTransferRequest(car, reader, item, 5, null);

        Set<ConstraintViolation<StockTransferRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        ConstraintViolation<StockTransferRequest> violation = violations.iterator().next();
        assertEquals("Timestamp cannot be empty.", violation.getMessage());
    }

    @Test
    public void testSetDirection() {
        LocalDateTime timestamp = LocalDateTime.now();
        StockTransferRequest request = new StockTransferRequest(car, reader, item, 5, timestamp);

        request.setDirection(TransferDirection.DELIVERY);
        assertEquals(TransferDirection.DELIVERY, request.getDirection());
    }

    @Test
    public void testSetStatus() {
        LocalDateTime timestamp = LocalDateTime.now();
        StockTransferRequest request = new StockTransferRequest(car, reader, item, 5, timestamp);

        request.setStatus(TransferStatus.COMPLETE);
        assertEquals(TransferStatus.COMPLETE, request.getStatus());
    }
}

