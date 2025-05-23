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

    @NotNull(message = "macAddress cannot be empty.")
    private String macAddress;

    @NotNull(message = "Name cannot be empty.")
    private String name;

    @OneToOne
    @NotNull(message = "Coordinates cannot be empty.")
    private Coordinate coordinate;

    @OneToMany(mappedBy = "reader")
    @JsonBackReference
    private List<Scan> scans = new ArrayList<Scan>();

    @OneToMany(mappedBy = "StartingPoint")
    @JsonBackReference
    private List<Route> StartingPoint = new ArrayList<Route>();

    @OneToMany(mappedBy = "destination")
    @JsonBackReference
    private List<Route> destination = new ArrayList<Route>();

    @OneToMany(mappedBy = "CurrentPoint")
    @JsonBackReference
    private List<Route> CurrentPoint = new ArrayList<Route>();

    private String ipAddress;

    protected Reader() {
    };

    public Reader(String MacAddress, String name, Coordinate coordinate) {
        this.macAddress = MacAddress;
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
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
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

    public void addStartingPoint(Route route) {
        this.StartingPoint.add(route);
        route.setStartingPoint(this);
    }

    public void addDestination(Route route) {
        this.destination.add(route);
        route.setDestination(this);
    }

    public void addCurrentPoint(Route route) {
        this.CurrentPoint.add(route);
        route.setCurrentPoint(this);
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

}
