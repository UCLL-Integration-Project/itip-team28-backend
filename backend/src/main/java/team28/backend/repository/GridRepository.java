package team28.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import team28.backend.model.Grid;

public interface GridRepository extends JpaRepository<Grid, Long> {

    Optional<Grid> findFirstByOrderByIdAsc();

}
