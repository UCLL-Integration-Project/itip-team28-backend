package team28.backend.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "grids")
public class Grid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToMany(mappedBy = "grid", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Coordinate> coordinates = new ArrayList<>();

    @NotNull
    private int measurement; // in meters

    public Grid() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Coordinate> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Coordinate> coordinates) {
        this.coordinates.clear();
        for (Coordinate coordinate : coordinates) {
            addCoordinate(coordinate);
        }
    }

    public void addCoordinate(Coordinate coordinate) {
        coordinate.setGrid(this);
        this.coordinates.add(coordinate);
    }

    public int getMeasurement() {
        return measurement;
    }

    public void setMeasurement(int measurement) {
        this.measurement = measurement;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        result = prime * result + ((coordinates == null) ? 0 : coordinates.hashCode());
        result = prime * result + measurement;
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
        Grid other = (Grid) obj;
        if (id != other.id)
            return false;
        if (coordinates == null) {
            if (other.coordinates != null)
                return false;
        } else if (!coordinates.equals(other.coordinates))
            return false;
        if (measurement != other.measurement)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Grid [id=" + id + ", coordinates=" + coordinates + ", measurement=" + measurement + "]";
    }

}
