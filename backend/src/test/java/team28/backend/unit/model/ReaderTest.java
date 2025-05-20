package team28.backend.unit.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import team28.backend.model.Coordinate;
import team28.backend.model.Reader;

public class ReaderTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    public static void createValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    public void givenValidValues_whenCreatingReader_thenReaderIsCreatedWithThoseValues() {
        String MacAddress = "00-B0-D0-63-C2-26";
        String name = "Reader1";
        Coordinate coordinates = new Coordinate(0, 0);
;

        Reader reader = new Reader(MacAddress, name, coordinates);

        assertEquals("00-B0-D0-63-C2-26", reader.getMacAddress());
        assertEquals("Reader1", reader.getName());
        assertEquals("40N", reader.getCoordinates());
    }

    @Test
    public void givenEmptyMacAddress_whenCreatingReader_thenThrowExecption() {
        String MacAddress = null;
        String name = "Reader1";
        Coordinate coordinates = new Coordinate(0, 0);

        Reader reader = new Reader(MacAddress, name, coordinates);

        Set<ConstraintViolation<Reader>> violations = validator.validate(reader);

        assertEquals(1, violations.size());
        ConstraintViolation<Reader> violation = violations.iterator().next();
        assertEquals("MacAddress cannot be empty.", violation.getMessage());
    }

    @Test
    public void givenEmptyName_whenCreatingReader_thenThrowExecption() {
        String MacAddress = "00-B0-D0-63-C2-26";
        String name = null;
        Coordinate coordinates = new Coordinate(0, 0);

        Reader reader = new Reader(MacAddress, name, coordinates);

        Set<ConstraintViolation<Reader>> violations = validator.validate(reader);

        assertEquals(1, violations.size());
        ConstraintViolation<Reader> violation = violations.iterator().next();
        assertEquals("Name cannot be empty.", violation.getMessage());
    }

    @Test
    public void givenEmptyCoordinates_whenCreatingReader_thenThrowExecption() {
        String MacAddress = "00-B0-D0-63-C2-26";
        String name = "Reader1";
        Coordinate coordinates = null;

        Reader reader = new Reader(MacAddress, name, coordinates);

        Set<ConstraintViolation<Reader>> violations = validator.validate(reader);

        assertEquals(1, violations.size());
        ConstraintViolation<Reader> violation = violations.iterator().next();
        assertEquals("Coordinates cannot be empty.", violation.getMessage());
    }

}
