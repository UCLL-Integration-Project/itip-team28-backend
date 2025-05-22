package team28.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import team28.backend.model.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query("SELECT CASE WHEN COUNT(i) > 0 THEN true ELSE false END FROM Item i WHERE LOWER(i.name) = LOWER(:name)")
    boolean existsByName(@Param("name") String name);

    @Query("SELECT i FROM Item i WHERE LOWER(i.name) = LOWER(:name)")
    Optional<Item> findByName(@Param("name") String name);
}
