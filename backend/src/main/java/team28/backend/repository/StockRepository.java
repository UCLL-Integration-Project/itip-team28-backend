package team28.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import team28.backend.model.Item;
import team28.backend.model.Stock;
import team28.backend.model.StockHolderInt;

public interface StockRepository extends JpaRepository<Stock, Long> {
    @Query("SELECT s FROM Stock s WHERE s.holder = :holder")
    List<Stock> findByHolder(@Param("holder") StockHolderInt holder);

    @Query("SELECT s FROM Stock s WHERE s.holder = :holder AND s.item = :item")
    Optional<Stock> findByHolderAndItem(@Param("holder") StockHolderInt holder, @Param("item") Item item);}