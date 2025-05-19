package team28.backend.unit.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import team28.backend.model.Car;
import team28.backend.model.Scan;
import team28.backend.model.Reader;

public class ScanTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    public static void createValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    public void givenValidValues_whenCreatingScan_thenScanIsCreatedWithThoseValues() {
        Car CarId = new Car(1);
        Reader Number = new Reader(13);
        LocalDateTime timestamp = LocalDateTime.of(2025, 5, 1, 9, 15);

        Scan scan = new Scan(CarId, Number, timestamp);

        assertEquals(1, scan.getCar().getNumber());
        assertEquals(13, scan.getReader().getNumber());
        assertEquals(LocalDateTime.parse("2025-05-01T09:15"), scan.getTimestamp());
    }

    @Test
    public void givenEmptyCar_whenCreatingScan_thenThrowExecption() {
        Car CarId = null;
        Reader Number = new Reader(13);
        LocalDateTime timestamp = LocalDateTime.of(2025, 5, 1, 9, 15);

        Scan scan = new Scan(CarId, Number, timestamp);

        Set<ConstraintViolation<Scan>> violations = validator.validate(scan);

        assertEquals(1, violations.size());
        ConstraintViolation<Scan> violation = violations.iterator().next();
        assertEquals("CarID cannot be empty.", violation.getMessage());
    }

    @Test
    public void givenEmptyReader_whenCreatingScan_thenThrowExecption() {
        Car CarId = new Car(1);
        Reader Number = null;
        LocalDateTime timestamp = LocalDateTime.of(2025, 5, 1, 9, 15);

        Scan scan = new Scan(CarId, Number, timestamp);

        Set<ConstraintViolation<Scan>> violations = validator.validate(scan);

        assertEquals(1, violations.size());
        ConstraintViolation<Scan> violation = violations.iterator().next();
        assertEquals("ReaderID cannot be empty.", violation.getMessage());
    }

    @Test
    public void givenEmptyTimestamp_whenCreatingScan_thenThrowExecption() {
        Car CarId = new Car(1);
        Reader Number = new Reader(13);
        LocalDateTime timestamp = null;

        Scan scan = new Scan(CarId, Number, timestamp);

        Set<ConstraintViolation<Scan>> violations = validator.validate(scan);

        assertEquals(1, violations.size());
        ConstraintViolation<Scan> violation = violations.iterator().next();
        assertEquals("Timestamp cannot be empty.", violation.getMessage());
    }

}
