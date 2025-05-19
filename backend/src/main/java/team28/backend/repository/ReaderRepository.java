package team28.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import team28.backend.model.Reader;

public interface ReaderRepository extends JpaRepository<Reader, Long> {
    boolean existsByNumber(int ReaderId);
}
