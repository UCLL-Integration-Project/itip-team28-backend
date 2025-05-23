package team28.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;

@Entity
@Table(name = "coordinates")
public class Coordinate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int longitude;

    private int latitude;

    @ManyToOne(optional = true)
    @JoinColumn(name = "reader_id", nullable = true)
    private Reader reader;

    @ManyToOne
    @JoinColumn(name = "grid_id")
    @JsonBackReference
    private Grid grid;

    protected Coordinate() {
    }

    public Coordinate(int longitude, int latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public long getId() {
        return id;
    }

    public int getLongitude() {
        return longitude;
    }

    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }

    public int getLatitude() {
        return latitude;
    }

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }

    public Reader getReader() {
        return reader;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }

    public Grid getGrid() {
        return grid;
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        result = prime * result + longitude;
        result = prime * result + latitude;
        result = prime * result + ((reader == null) ? 0 : reader.hashCode());
        result = prime * result + ((grid == null) ? 0 : grid.hashCode());
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
        Coordinate other = (Coordinate) obj;
        if (id != other.id)
            return false;
        if (longitude != other.longitude)
            return false;
        if (latitude != other.latitude)
            return false;
        if (reader == null) {
            if (other.reader != null)
                return false;
        } else if (!reader.equals(other.reader))
            return false;
        if (grid == null) {
            if (other.grid != null)
                return false;
        } else if (!grid.equals(other.grid))
            return false;
        return true;
    }

}
