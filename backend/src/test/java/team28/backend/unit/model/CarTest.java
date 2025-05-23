package team28.backend.unit.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import team28.backend.model.Car;

public class CarTest {

    @Test
    public void givenValidValues_whenCreatingCar_thenCarIsCreatedWithThoseValues() {
        String CarId = "O1OR3NI";

        Car car = new Car(CarId);

        assertEquals("O1OR3NI", car.getName());
    }
}
