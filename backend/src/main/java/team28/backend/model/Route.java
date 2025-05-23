package team28.backend.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "routes")
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

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

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        result = prime * result + (status ? 1231 : 1237);
        result = prime * result + ((StartingPoint == null) ? 0 : StartingPoint.hashCode());
        result = prime * result + ((destination == null) ? 0 : destination.hashCode());
        result = prime * result + ((CurrentPoint == null) ? 0 : CurrentPoint.hashCode());
        result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
        result = prime * result + ((instructions == null) ? 0 : instructions.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Route other = (Route) obj;
        if (id != other.id)
            return false;
        if (status != other.status)
            return false;
        if (StartingPoint == null) {
            if (other.StartingPoint != null)
                return false;
        } else if (!StartingPoint.equals(other.StartingPoint))
            return false;
        if (destination == null) {
            if (other.destination != null)
                return false;
        } else if (!destination.equals(other.destination))
            return false;
        if (CurrentPoint == null) {
            if (other.CurrentPoint != null)
                return false;
        } else if (!CurrentPoint.equals(other.CurrentPoint))
            return false;
        if (timestamp == null) {
            if (other.timestamp != null)
                return false;
        } else if (!timestamp.equals(other.timestamp))
            return false;
        if (instructions == null) {
            if (other.instructions != null)
                return false;
        } else if (!instructions.equals(other.instructions))
            return false;
        return true;
    }

}
