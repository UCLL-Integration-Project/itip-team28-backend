package team28.backend.unit.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import team28.backend.model.Role;
import team28.backend.model.User;

public class UserTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    public static void createValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    public void givenValidValues_whenCreatingUser_thenUserIsCreatedWithThoseValues() {
        String username = "testuser";
        String email = "test@example.com";
        String password = "password";
        Role role = Role.USER;

        User user = new User(username, email, password, role);

        assertEquals("testuser", user.getUsername());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("password", user.getPassword());
        assertEquals(Role.USER, user.getRole());
    }

    @Test
    public void givenEmptyUsername_whenCreatingUser_thenThrowExecption() {
        String username = null;
        String email = "test@example.com";
        String password = "password";
        Role role = Role.USER;

        User user = new User(username, email, password, role);

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(1, violations.size());
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals("Username cannot be empty.", violation.getMessage());
    }

    @Test
    public void givenEmptyEmail_whenCreatingUser_thenThrowExecption() {
        String username = "testuser";
        String email = null;
        String password = "password";
        Role role = Role.USER;

        User user = new User(username, email, password, role);

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(1, violations.size());
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals("Email cannot be empty.", violation.getMessage());
    }

    @Test
    public void givenInvalidEmail_whenCreatingUser_thenThrowExecption() {
        String username = "testuser";
        String email = "example";
        String password = "password";
        Role role = Role.USER;

        User user = new User(username, email, password, role);

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(1, violations.size());
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals("Email is not valid.", violation.getMessage());
    }

    @Test
    public void givenEmptyPassword_whenCreatingUser_thenThrowExecption() {
        String username = "testuser";
        String email = "test@example.com";
        String password = null;
        Role role = Role.USER;

        User user = new User(username, email, password, role);

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(1, violations.size());
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals("Password cannot be empty.", violation.getMessage());
    }
}
