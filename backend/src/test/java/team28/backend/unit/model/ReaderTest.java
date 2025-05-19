package team28.backend.unit.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
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
        int Number = 123;

        Reader reader = new Reader(Number);

        assertEquals(123, reader.getNumber());
    }

    @Test
    public void givenNegativeNumber_whenCreatingReader_thenThrowExecption() {
        int Number = -123;

        Reader reader = new Reader(Number);

        Set<ConstraintViolation<Reader>> violations = validator.validate(reader);

        assertEquals(1, violations.size());
        ConstraintViolation<Reader> violation = violations.iterator().next();
        assertEquals("Number must be a positive number", violation.getMessage());
    }

}
