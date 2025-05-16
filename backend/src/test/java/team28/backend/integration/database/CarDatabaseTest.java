package team28.backend.integration.database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import team28.backend.model.Car;
import team28.backend.repository.CarRepository;

@DataJpaTest
public class CarDatabaseTest {
    @Autowired
    private CarRepository CarRepository;

    private Car car;

    @BeforeEach
    public void setUp() {
        car = new Car(1);
        CarRepository.save(car);
    }

    @Test
    public void givenExistingCarId_whenSearchingForCar_thenReturnTrue() {
        int CarId = 1;

        boolean existing = CarRepository.existsByNumber(CarId);

        assertNotEquals(false, existing);
        assertEquals(true, existing);
    }

    @Test
    public void givenNonExistingCarId_whenSearchingForCar_thenReturnFalse() {
        int CarId = 2;

        boolean existing = CarRepository.existsByNumber(CarId);

        assertNotEquals(true, existing);
        assertEquals(false, existing);
    }
}
