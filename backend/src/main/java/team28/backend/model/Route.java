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
import jakarta.persistence.ManyToOne;
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

    @ManyToOne
    @NotNull(message = "Starting point cannot be empty.")
    private Reader startingPoint;

    @ManyToOne
    @JsonBackReference
    @NotNull(message = "Destination cannot be empty.")
    private Reader destination;

    @ManyToOne
    @NotNull(message = "Current point cannot be empty.")
    private Reader currentPoint;

    @NotNull(message = "Timestamp cannot be empty.")
    private LocalDateTime timestamp;

    @ElementCollection
    private List<String> instructions = new ArrayList<String>();

    protected Route() {
    };

    public Route(boolean status, Reader startingPoint, Reader destination, Reader currentPoint, LocalDateTime timestamp,
            List<String> instructions) {
        this.status = status;
        this.startingPoint = startingPoint;
        this.destination = destination;
        this.currentPoint = currentPoint;
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
        return startingPoint;
    }

    public void setStartingPoint(Reader startingPoint) {
        this.startingPoint = startingPoint;
    }

    public Reader getDestination() {
        return destination;
    }

    public void setDestination(Reader destination) {
        this.destination = destination;
    }

    public Reader getCurrentPoint() {
        return currentPoint;
    }

    public void setCurrentPoint(Reader currentPoint) {
        this.currentPoint = currentPoint;
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
        result = prime * result + ((startingPoint == null) ? 0 : startingPoint.hashCode());
        result = prime * result + ((destination == null) ? 0 : destination.hashCode());
        result = prime * result + ((currentPoint == null) ? 0 : currentPoint.hashCode());
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
        if (startingPoint == null) {
            if (other.startingPoint != null)
                return false;
        } else if (!startingPoint.equals(other.startingPoint))
            return false;
        if (destination == null) {
            if (other.destination != null)
                return false;
        } else if (!destination.equals(other.destination))
            return false;
        if (currentPoint == null) {
            if (other.currentPoint != null)
                return false;
        } else if (!currentPoint.equals(other.currentPoint))
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

    @Override
    public String toString() {
        return "Route [id=" + id + ", status=" + status + ", startingPoint=" + startingPoint + ", destination="
                + destination + ", currentPoint=" + currentPoint + ", timestamp=" + timestamp + ", instructions="
                + instructions + "]";
    }

}
