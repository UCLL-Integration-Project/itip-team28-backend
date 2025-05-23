package team28.backend.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "routes")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean status;

    @OneToOne
    @NotNull(message = "Starting point cannot be empty.")
    private Reader StartingPoint;

    @OneToOne
    @JsonBackReference
    @NotNull(message = "Destination cannot be empty.")
    private Reader destination;

    @OneToOne
    @NotNull(message = "Current point cannot be empty.")
    private Reader CurrentPoint;

    @NotNull(message = "Timestamp cannot be empty.")
    private LocalDateTime timestamp;

    @ElementCollection
    private List<String> instructions = new ArrayList<String>();

    protected Route() {
    };

    public Route(boolean status, Reader StartingPoint, Reader destination, Reader CurrentPoint, LocalDateTime timestamp,
            List<String> instructions) {
        this.status = status;
        this.StartingPoint = StartingPoint;
        this.destination = destination;
        this.CurrentPoint = CurrentPoint;
        this.timestamp = timestamp;
        this.instructions = instructions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Reader getStartingPoint() {
        return StartingPoint;
    }

    public void setStartingPoint(Reader startingPoint) {
        StartingPoint = startingPoint;
    }

    public Reader getDestination() {
        return destination;
    }

    public void setDestination(Reader destination) {
        this.destination = destination;
    }

    public Reader getCurrentPoint() {
        return CurrentPoint;
    }

    public void setCurrentPoint(Reader currentPoint) {
        CurrentPoint = currentPoint;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public List<String> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<String> instructions) {
        this.instructions = instructions;
    }

}
