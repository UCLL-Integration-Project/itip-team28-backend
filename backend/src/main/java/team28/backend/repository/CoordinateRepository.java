package team28.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import team28.backend.model.Coordinate;

@Repository
public interface CoordinateRepository extends JpaRepository<Coordinate, Long> {
    Optional<Coordinate> findByLongitudeAndLatitude(int longitude, int latitude);
}