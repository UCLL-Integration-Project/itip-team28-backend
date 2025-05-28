package team28.backend.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Reader implements StockHolderInt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @OneToMany(mappedBy = "holder")
    @JsonManagedReference
    private List<Stock> stocks = new ArrayList<>();

    protected Reader() {
    };

    public Reader(String MacAddress, String name, Coordinate coordinate) {
        this.macAddress = MacAddress;
        this.name = name;
        this.coordinate = coordinate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Reader other = (Reader) obj;
        return id != null && id.equals(other.id);
    }

    @Override
    public String toString() {
        return "Reader [id=" + id + ", macAddress=" + macAddress + ", name=" + name + ", coordinate=" + coordinate
                + ", scans=" + scans + ", StartingPoint=" + StartingPoint + ", destination=" + destination
                + ", CurrentPoint=" + CurrentPoint + ", stocks=" + stocks + "]";
    }    

    
}
