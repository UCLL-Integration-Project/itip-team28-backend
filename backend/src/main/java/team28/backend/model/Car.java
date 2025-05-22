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
import jakarta.persistence.Table;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "cars")
public class Car implements StockHolderInt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Positive(message = "Number must be a positive number")
    private int number;

    @OneToMany(mappedBy = "car")
    @JsonBackReference
    private List<Scan> scans = new ArrayList<Scan>();

    @OneToMany(mappedBy = "holder")
    @JsonManagedReference
    private List<Stock> stocks = new ArrayList<Stock>();

    protected Car() {
    };

    public Car(int number) {
        this.number = number;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
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
    public String getName() {
        return "Car" + number;
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
}
