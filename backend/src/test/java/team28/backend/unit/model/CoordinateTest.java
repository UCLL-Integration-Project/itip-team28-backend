package team28.backend.unit.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import team28.backend.model.Coordinate;

public class CoordinateTest {

    @Test
    public void givenValidValues_whenCreatingCoordinate_thenCoordinateIsCreatedWithThoseValues() {
        int longitude = 10;
        int latitude = 20;

        Coordinate coordinate = new Coordinate(longitude, latitude);

        assertEquals(10, coordinate.getLongitude());
        assertEquals(20, coordinate.getLatitude());
    }
}
