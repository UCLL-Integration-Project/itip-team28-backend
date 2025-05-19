package team28.backend.integration.database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import team28.backend.model.Reader;
import team28.backend.repository.ReaderRepository;

@DataJpaTest
public class ReaderDatabaseTest {
    @Autowired
    private ReaderRepository ReaderRepository;

    private Reader reader;

    @BeforeEach
    public void setUp() {
        reader = new Reader(1);
        ReaderRepository.save(reader);
    }

    @Test
    public void givenExistingNumber_whenSearchingForReader_thenReturnTrue() {
        int number = 1;

        boolean existing = ReaderRepository.existsByNumber(number);

        assertNotEquals(false, existing);
        assertEquals(true, existing);
    }

    @Test
    public void givenNonExistingNumber_whenSearchingForTag_thenReturnFalse() {
        int number = 2;

        boolean existing = ReaderRepository.existsByNumber(number);

        assertNotEquals(true, existing);
        assertEquals(false, existing);
    }
}
