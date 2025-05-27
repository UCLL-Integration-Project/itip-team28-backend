package team28.backend.model;

import java.util.List;

//An interface that gets implemented by entities that can hold stock => reader and car
// It defines the methods for managing the stock associatedwith the holder
public interface StockHolderInt {
    // returns id of holder
    Long getId();
    
    //returns name of holder
    String getName();

    // returns list of all stocks associated with the holder
    List<Stock> getStocks();

    // sets the list of stocks associated with the holder
    void setStocks(List<Stock> stocks);

    // adds a stock to the holder's stock list
    void addStock(Stock stock);
}
