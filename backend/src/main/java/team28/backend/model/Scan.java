package team28.backend.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "scans")
public class Scan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull(message = "CardID cannot be empty.")
    private String CarId;

    @NotNull(message = "ReaderID cannot be empty.")
    private String ReaderId;

    @NotNull(message = "Timestamp cannot be empty.")
    private LocalDateTime timestamp;

    protected Scan() {

    }

    public Scan(String CarId, String ReaderId, LocalDateTime timestamp) {
        this.CarId = CarId;
        this.ReaderId = ReaderId;
        this.timestamp = timestamp;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCarId() {
        return CarId;
    }

    public void setCarId(String CarId) {
        this.CarId = CarId;
    }

    public String getReaderId() {
        return ReaderId;
    }

    public void setReaderId(String ReaderId) {
        this.ReaderId = ReaderId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
