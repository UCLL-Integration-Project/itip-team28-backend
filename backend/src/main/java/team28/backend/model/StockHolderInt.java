package team28.backend.model;

import java.util.List;

public interface StockHolderInt {
    Long getId();
    String getName();
    List<Stock> getStocks();
    void setStocks(List<Stock> stocks);
    void addStock(Stock stock);
}
