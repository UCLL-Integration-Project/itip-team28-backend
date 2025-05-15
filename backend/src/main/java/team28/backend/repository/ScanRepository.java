package team28.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import team28.backend.model.Scan;

public interface ScanRepository extends JpaRepository<Scan, Long>{
    
} 