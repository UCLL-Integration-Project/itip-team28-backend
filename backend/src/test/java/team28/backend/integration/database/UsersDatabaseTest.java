package team28.backend.integration.database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import team28.backend.exceptions.ServiceException;
import team28.backend.model.Role;
import team28.backend.model.User;
import team28.backend.repository.UserRepository;

@DataJpaTest
public class UsersDatabaseTest {

    @Autowired
    private UserRepository UserRepository;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User("Test", "kees.test@example.com", "testkees123", Role.USER);
        UserRepository.save(user);
    }

    @Test
    public void givenExistingUsername_whenSearchingForUser_thenReturnTrue() {
        String username = "Test";

        boolean existing = UserRepository.existsByUsername(username);

        assertNotEquals(false, existing);
        assertEquals(true, existing);
    }

    @Test
    public void givenNonExistingUsername_whenSearchingForUser_thenReturnFalse() {
        String username = "Meow";

        boolean existing = UserRepository.existsByUsername(username);

        assertNotEquals(true, existing);
        assertEquals(false, existing);
    }

    @Test
    public void givenExistingUsername_whenSearchingForUser_thenReturnUser() {
        String username = "Test";
        User existingUser = UserRepository.findByUsername(username)
            .orElseThrow(() -> new ServiceException("User not found"));;

        assertNotNull(existingUser);
    }

    @Test
    public void givenNonExistingUsername_whenSearchingForUser_thenReturnUser() {
        String username = "Meow";
        User existingUser = UserRepository.findByUsername(username)
            .orElseThrow(() -> new ServiceException("User not found"));;

        assertNull(existingUser);
    }
}
