package team28.backend.unit.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import team28.backend.model.Car;

public class CarTest {
    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    public static void createValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    public void givenValidValues_whenCreatingTag_thenTagIsCreatedWithThoseValues() {
        int CarId = 123;

        Car car = new Car(CarId);

        assertEquals(123, car.getNumber());
    }

    @Test
    public void givenNegativeNumber_whenCreatingTag_thenThrowExecption() {
        int CarId = -123;

        Car car = new Car(CarId);

        Set<ConstraintViolation<Car>> violations = validator.validate(car);

        assertEquals(1, violations.size());
        ConstraintViolation<Car> violation = violations.iterator().next();
        assertEquals("Number must be a positive number", violation.getMessage());
    }
}
