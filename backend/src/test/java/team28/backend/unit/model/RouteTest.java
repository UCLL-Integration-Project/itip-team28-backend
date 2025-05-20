package team28.backend.unit.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import team28.backend.model.Reader;
import team28.backend.model.Route;

public class RouteTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    public static void createValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    public void givenValidValues_whenCreatingRouter_thenRouterIsCreatedWithThoseValues() {
        boolean status = false;
        Reader StartingPoint = new Reader("macaddress", "name1", "30S");
        Reader destination = new Reader("macaddress2", "name2", "30N");
        Reader CurrentPoint = new Reader("macaddress", "name1", "30S");
        LocalDateTime timestamp = LocalDateTime.of(2025, 5, 1, 9, 15);
        List<String> instructions = List.of("Step 1", "Step 2");

        Route route = new Route(status, StartingPoint, destination, CurrentPoint, timestamp, instructions);

        assertEquals(false, route.isStatus());
        assertEquals("name1", route.getStartingPoint().getName());
        assertEquals("name2", route.getDestination().getName());
        assertEquals("name1", route.getCurrentPoint().getName());
        assertEquals(LocalDateTime.parse("2025-05-01T09:15"), route.getTimestamp());
    }

    @Test
    public void givenEmptyStartingPoint_whenCreatingRoute_thenThrowExecption() {
        boolean status = false;
        Reader StartingPoint = null;
        Reader destination = new Reader("macaddress2", "name2", "30N");
        Reader CurrentPoint = new Reader("macaddress", "name1", "30S");
        LocalDateTime timestamp = LocalDateTime.of(2025, 5, 1, 9, 15);
        List<String> instructions = List.of("Step 1", "Step 2");

        Route route = new Route(status, StartingPoint, destination, CurrentPoint, timestamp, instructions);

        Set<ConstraintViolation<Route>> violations = validator.validate(route);

        assertEquals(1, violations.size());
        ConstraintViolation<Route> violation = violations.iterator().next();
        assertEquals("Starting point cannot be empty.", violation.getMessage());
    }

    @Test
    public void givenEmptyDestination_whenCreatingRoute_thenThrowExecption() {
        boolean status = false;
        Reader StartingPoint = new Reader("macaddress", "name1", "30S");
        Reader destination = null;
        Reader CurrentPoint = new Reader("macaddress", "name1", "30S");
        LocalDateTime timestamp = LocalDateTime.of(2025, 5, 1, 9, 15);
        List<String> instructions = List.of("Step 1", "Step 2");

        Route route = new Route(status, StartingPoint, destination, CurrentPoint, timestamp, instructions);

        Set<ConstraintViolation<Route>> violations = validator.validate(route);

        assertEquals(1, violations.size());
        ConstraintViolation<Route> violation = violations.iterator().next();
        assertEquals("Destination cannot be empty.", violation.getMessage());
    }

    @Test
    public void givenEmptyCurrentPoint_whenCreatingRoute_thenThrowExecption() {
        boolean status = false;
        Reader StartingPoint = new Reader("macaddress", "name1", "30S");
        Reader destination = new Reader("macaddress2", "name2", "30N");
        Reader CurrentPoint = null;
        LocalDateTime timestamp = LocalDateTime.of(2025, 5, 1, 9, 15);
        List<String> instructions = List.of("Step 1", "Step 2");

        Route route = new Route(status, StartingPoint, destination, CurrentPoint, timestamp, instructions);

        Set<ConstraintViolation<Route>> violations = validator.validate(route);

        assertEquals(1, violations.size());
        ConstraintViolation<Route> violation = violations.iterator().next();
        assertEquals("Current point cannot be empty.", violation.getMessage());
    }

    @Test
    public void givenEmptyTimestamp_whenCreatingRoute_thenThrowExecption() {
        boolean status = false;
        Reader StartingPoint = new Reader("macaddress", "name1", "30S");
        Reader destination = new Reader("macaddress2", "name2", "30N");
        Reader CurrentPoint = new Reader("macaddress", "name1", "30S");
        LocalDateTime timestamp = null;
        List<String> instructions = List.of("Step 1", "Step 2");

        Route route = new Route(status, StartingPoint, destination, CurrentPoint, timestamp, instructions);

        Set<ConstraintViolation<Route>> violations = validator.validate(route);

        assertEquals(1, violations.size());
        ConstraintViolation<Route> violation = violations.iterator().next();
        assertEquals("Timestamp cannot be empty.", violation.getMessage());
    }

}
