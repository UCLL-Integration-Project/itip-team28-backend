package team28.backend.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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
public class Reader implements StockHolderInt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "MacAddress cannot be empty.")
    private String MacAddress;

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

    @OneToMany(mappedBy = "holder")
    @JsonManagedReference
    private List<Stock> stocks = new ArrayList<>();

    protected Reader() {
    };

    public Reader(String MacAddress, String name, Coordinate coordinate) {
        this.MacAddress = MacAddress;
        this.name = name;
        this.coordinate = coordinate;
    }

    public Long getId() {
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

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public List<Route> getStartingPoint() {
        return StartingPoint;
    }

    public void setStartingPoint(List<Route> startingPoint) {
        StartingPoint = startingPoint;
    }

    public List<Route> getDestination() {
        return destination;
    }

    public void setDestination(List<Route> destination) {
        this.destination = destination;
    }

    public List<Route> getCurrentPoint() {
        return CurrentPoint;
    }

    public void setCurrentPoint(List<Route> currentPoint) {
        CurrentPoint = currentPoint;
    }

    public void setStocks(List<Stock> stocks) {
        this.stocks = stocks;
    }

    @Override
    public List<Stock> getStocks() {
        return stocks;
    }

    @Override  
    public void addStock(Stock stock) {
        this.stocks.add(stock);
        stock.setHolder(this);
    }

}
