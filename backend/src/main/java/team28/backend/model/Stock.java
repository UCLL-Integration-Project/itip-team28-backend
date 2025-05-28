package team28.backend.model;

import org.hibernate.annotations.Any;
import org.hibernate.annotations.AnyDiscriminator;
import org.hibernate.annotations.AnyDiscriminatorValue;
import org.hibernate.annotations.AnyKeyJavaClass;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.PositiveOrZero;

//reperesents the stock in the database, it associates an item with a stock holder (either a reader or a car) and keeps track of the quantity available
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

    @Any
    @AnyDiscriminator(DiscriminatorType.STRING)
    @AnyDiscriminatorValue(discriminator = "READER", entity = Reader.class)
    @AnyDiscriminatorValue(discriminator = "CAR", entity = Car.class)
    @AnyKeyJavaClass(Long.class)
    @Column(name = "holder_type")
    @JoinColumn(name = "holder_id")
    @JsonBackReference
    private StockHolderInt holder;

    protected Stock() {
    }

    public Stock(StockHolderInt holder, Item item, int quantity) {
        this.holder = holder;
        this.item = item;
        this.quantity = quantity;
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

    @Override
    public String toString() {
        return "Stock [id=" + id + ", item=" + item + ", quantity=" + quantity + ", holder=" + holder + "]";
    }

}