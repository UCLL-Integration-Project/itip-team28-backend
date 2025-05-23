package team28.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import team28.backend.model.Car;

public interface CarRepository extends JpaRepository<Car, Long> {
    boolean existsByName(String name);

    Car findByName(String name);
}
