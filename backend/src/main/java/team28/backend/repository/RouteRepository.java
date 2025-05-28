package team28.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import team28.backend.model.Reader;
import team28.backend.model.Route;

public interface RouteRepository extends JpaRepository<Route, Long> {
    Route findByStartingPointAndCurrentPoint(Reader startingPoint, Reader currentPoint);
}
