package team28.backend.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
// Represents the request to transfer stock between a reader and a car
@Entity
@Table(name = "stock_transfer_requests")
public class StockTransferRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //tells you which car is involved
    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;

    //tells you which reader is involved
    @ManyToOne
    @JoinColumn(name = "reader_id")
    private Reader reader;

    //tells you which item is being transferred
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    //tells you how many of the item is being transferred
    @Positive(message = "Stock quantity must be a positive number")
    private int quantity;

    //tells you when the transfer request was made
    @NotNull(message = "Timestamp cannot be empty.")
    private LocalDateTime timestamp;

    //tells you the status of the transfer request, PENDING or COMPLETE
    @Enumerated(EnumType.STRING)
    private TransferStatus status;

    //tells you the direction of the transfer, is the car doing either PICKUP or DELIVERY
    @Enumerated(EnumType.STRING)
    private TransferDirection direction;

    protected StockTransferRequest() {
    }

    public StockTransferRequest(Car car, Reader reader, Item item, int quantity, LocalDateTime timestamp) {
        this.car = car;
        this.reader = reader;
        this.item = item;
        this.quantity = quantity;
        this.timestamp = timestamp;
        this.status = TransferStatus.PENDING; //pending is the default status
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Reader getReader() {
        return reader;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public TransferStatus getStatus() {
        return status;
    }

    public void setStatus(TransferStatus status) {
        this.status = status;
    }

    public TransferDirection getDirection() {
        return direction;
    }

    public void setDirection(TransferDirection direction) {
        this.direction = direction;
    }
}