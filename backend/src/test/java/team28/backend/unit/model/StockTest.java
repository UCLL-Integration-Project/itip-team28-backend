package team28.backend.unit.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import org.junit.jupiter.api.*;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import team28.backend.model.Item;
import team28.backend.model.StockHolderInt;
import team28.backend.model.Stock;

public class StockTest {
    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    public static void createValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    public void givenValidValues_whenCreatingStock_thenStocksCreatedWithThoseValues() {
        Item item = new Item("item101");
        int quantity = 8;
        StockHolderInt stockHolderInt = new StockHolderInt();

        Stock stock = new Stock(item, quantity, stockHolderInt);

        assertEquals("item101", item.getName());
    }

    @Test
    public void givenInvalidValues_whenCreatingItem_thenItemIsCreatedWithThoseValues() {
        String name = null;

        Item item = new Item(name);

        Set<ConstraintViolation<Item>> violations = validator.validate(item);

        assertEquals(1, violations.size());
        ConstraintViolation<Item> violation = violations.iterator().next();
        assertEquals("Name cannot be empty.", violation.getMessage());
    }
}