package team28.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import team28.backend.model.Coordinate;

@Repository
public interface CoordinateRepository extends JpaRepository<Coordinate, Long> {

}