package team28.backend.integration.database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import team28.backend.model.Coordinate;
import team28.backend.model.Reader;
import team28.backend.repository.ReaderRepository;

@DataJpaTest
public class ReaderDatabaseTest {
    @Autowired
    private ReaderRepository ReaderRepository;

    private Reader reader;

    @BeforeEach
    public void setUp() {
        Coordinate coordinates = new Coordinate(0, 0);
        reader = new Reader("00-B0-D0-63-C2-26", "Reader1", coordinates);
        ReaderRepository.save(reader);
    }

    @Test
    public void givenExistingName_whenSearchingForReader_thenReturnTrue() {
        String name = "Reader1";

        boolean existing = ReaderRepository.existsByName(name);

        assertNotEquals(false, existing);
        assertEquals(true, existing);
    }

    @Test
    public void givenNonExistingName_whenSearchingForReader_thenReturnFalse() {
        String name = "Reader101";

        boolean existing = ReaderRepository.existsByName(name);

        assertNotEquals(true, existing);
        assertEquals(false, existing);
    }
}
