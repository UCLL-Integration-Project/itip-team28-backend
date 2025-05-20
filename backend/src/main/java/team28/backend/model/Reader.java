package team28.backend.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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

    @NotNull(message = "Coordinates cannot be empty.")
    private String coordinates;

    @OneToMany(mappedBy = "reader")
    @JsonBackReference
    private List<Scan> scans = new ArrayList<Scan>();;

    protected Reader() {
    };

    public Reader(String MacAddress, String name, String coordinates) {
        this.MacAddress = MacAddress;
        this.name = name;
        this.coordinates = coordinates;
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

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
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
