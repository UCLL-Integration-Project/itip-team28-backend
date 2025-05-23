package team28.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import team28.backend.model.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
    boolean existsByName(String name);

    Optional<Item> findByName(String name);
}
