package team28.backend.integration.database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import team28.backend.model.Tag;
import team28.backend.repository.TagRepository;

@DataJpaTest
public class TagDatabaseTest {
    @Autowired
    private TagRepository TagRepository;

    private Tag tag;

    @BeforeEach
    public void setUp() {
        tag = new Tag(1);
        TagRepository.save(tag);
    }

    @Test
    public void givenExistingNumber_whenSearchingForTag_thenReturnTrue() {
        int Number = 1;

        boolean existing = TagRepository.existsByNumber(Number);

        assertNotEquals(false, existing);
        assertEquals(true, existing);
    }

    @Test
    public void givenNonExistingNumber_whenSearchingForTag_thenReturnFalse() {
        int Number = 2;

        boolean existing = TagRepository.existsByNumber(Number);

        assertNotEquals(true, existing);
        assertEquals(false, existing);
    }
}
