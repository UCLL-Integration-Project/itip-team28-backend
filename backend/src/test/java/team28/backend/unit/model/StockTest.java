package team28.backend.unit.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import java.util.Set;

import org.junit.jupiter.api.*;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import team28.backend.model.Item;
import team28.backend.model.StockHolderInt;
import team28.backend.model.Stock;

@ExtendWith(MockitoExtension.class)
public class StockTest {
    @Mock
    private StockHolderInt stockHolder; 

    @Test
    public void testStockCreation() {
        Item item = new Item("backpack");
        Stock stock = new Stock(stockHolder,item, 50);

        assertEquals(item, stock.getItem());
        assertEquals(50, stock.getQuantity());
        assertEquals(stockHolder, stock.getHolder());
    }

    @Test
    public void testSetQuantity() {
        Item item = new Item("backpack");
        Stock stock = new Stock(stockHolder,item, 50);

        stock.setQuantity(75);
        assertEquals(75, stock.getQuantity());
    }

    @Test
    public void testSetItem() {
        Item item1 = new Item("backpack");
        Item item2 = new Item("pencil_case");
        Stock stock = new Stock(stockHolder, item1, 50);

        stock.setItem(item2);
        assertEquals(item2, stock.getItem());
    }

    @Test
    public void testSetHolder() {
        Item item = new Item("backpack");
        Stock stock = new Stock(stockHolder, item, 50);

        StockHolderInt newHolder = mock(StockHolderInt.class);
        stock.setHolder(newHolder);
        assertEquals(newHolder, stock.getHolder());
    }
}