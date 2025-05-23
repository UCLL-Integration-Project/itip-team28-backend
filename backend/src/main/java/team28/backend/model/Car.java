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
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name = "cars")
public class Car implements StockHolderInt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Name cannot be empty")
    private String name;

    @OneToMany(mappedBy = "car")
    @JsonBackReference
    private List<Scan> scans = new ArrayList<Scan>();

    @OneToMany(mappedBy = "holder")
    @JsonBackReference
    private List<Stock> stocks = new ArrayList<Stock>();

    protected Car() {
    };

    public Car(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setNumber(String name) {
        this.name = name;
    }

    public List<Scan> getScans() {
        return scans;
    }

    public void setScans(List<Scan> scans) {
        this.scans = scans;
    }

    public void addScan(Scan scan) {
        this.scans.add(scan);
        scan.setCar(this);
    }

    @Override
    public List<Stock> getStocks() {
        return stocks;
    }

    @Override
    public void setStocks(List<Stock> stocks) {
        this.stocks = stocks;
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
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        Car other = (Car) obj;
        if (id != other.id)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (scans == null) {
            if (other.scans != null)
                return false;
        } else if (!scans.equals(other.scans))
            return false;
        return true;
    }

}
