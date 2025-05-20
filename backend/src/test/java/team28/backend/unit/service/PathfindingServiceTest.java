package team28.backend.unit.service;

import org.junit.jupiter.api.Test;
import team28.backend.model.Coordinate;
import team28.backend.model.Reader;
import team28.backend.service.PathfindingService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PathfindingServiceTest {

    @Test
    public void testSimplePath() {
        // Arrange
        PathfindingService service = new PathfindingService();

        List<Reader> readers = new ArrayList<>();

        for (int lat = 0; lat < 3; lat++) {
            for (int lon = 0; lon < 3; lon++) {
                readers.add(new Reader("mac-" + lat + "-" + lon, "Reader-" + lat + "-" + lon,
                        new Coordinate(lon, lat)));
            }
        }

        Reader start = getReaderAt(readers, 0, 0); 
        Reader end = getReaderAt(readers, 2, 2);  

        // Act
        List<String> path = service.findPath(start, end, readers);

        // Assert
        // One possible path: [down, down, right, right]
        assertNotNull(path);
        assertEquals(4, path.size());
        assertTrue(path.contains("down") && path.contains("right"));
    }

    private Reader getReaderAt(List<Reader> readers, int lat, int lon) {
        for (Reader r : readers) {
            if (r.getCoordinates().getlatitude() == lat &&
                r.getCoordinates().getlongitude() == lon) {
                return r;
            }
        }
        throw new IllegalArgumentException("No reader at given coordinates");
    }
}
