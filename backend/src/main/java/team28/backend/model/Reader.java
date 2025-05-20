package team28.backend.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "readers")
public class Reader {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull(message = "MacAddress cannot be empty.")
    private String MacAddress;

    @NotNull(message = "Name cannot be empty.")
    private String name;

    @OneToOne
    private Coordinate coordinate;

    @OneToMany(mappedBy = "reader")
    @JsonBackReference
    private List<Scan> scans = new ArrayList<Scan>();;

    protected Reader() {
    };

    public Reader(String MacAddress, String name, Coordinate coordinate) {
        this.MacAddress = MacAddress;
        this.name = name;
        this.coordinate = coordinate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMacAddress() {
        return MacAddress;
    }

    public void setMacAddress(String macAddress) {
        MacAddress = macAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinate getCoordinates() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public List<Scan> getScans() {
        return scans;
    }

    public void setScans(List<Scan> scans) {
        this.scans = scans;

    }

    public void addScan(Scan scan) {
        this.scans.add(scan);
        scan.setReader(this);
    }

}
