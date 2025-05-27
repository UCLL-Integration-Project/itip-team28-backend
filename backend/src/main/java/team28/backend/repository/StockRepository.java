package team28.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import team28.backend.model.Item;
import team28.backend.model.Stock;
import team28.backend.model.StockHolderInt;

public interface StockRepository extends JpaRepository<Stock, Long> {
    // Finds all stocks associated with a specific stock holder, reader/car
    List<Stock> findByHolder(StockHolderInt holder);

    // Finds a specific stock by its holder and item
    Optional<Stock> findByHolderAndItem(StockHolderInt holder, Item item);
}