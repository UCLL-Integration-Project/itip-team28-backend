package team28.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "stocks")
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id")    
    private Item item;

    @PositiveOrZero(message = "Stock quantity must be zero or positive")
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "holder_id")
    private StockHolderInt holder;

    protected Stock() {}

    public Stock(Item item, int quantity, StockHolderInt holder) {
        this.item = item;
        this.quantity = quantity;
        this.holder = holder;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public StockHolderInt getHolder() {
        return holder;
    }

    public void setHolder(StockHolderInt holder) {
        this.holder = holder;
    }

}