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

    private String ipAddress;
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
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        result = prime * result + ((macAddress == null) ? 0 : macAddress.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((coordinate == null) ? 0 : coordinate.hashCode());
        result = prime * result + ((scans == null) ? 0 : scans.hashCode());
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
        Reader other = (Reader) obj;
        if (id != other.id)
            return false;
        if (macAddress == null) {
            if (other.macAddress != null)
                return false;
        } else if (!macAddress.equals(other.macAddress))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (coordinate == null) {
            if (other.coordinate != null)
                return false;
        } else if (!coordinate.equals(other.coordinate))
            return false;
        if (scans == null) {
            if (other.scans != null)
                return false;
        } else if (!scans.equals(other.scans))
            return false;
        return true;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

}
