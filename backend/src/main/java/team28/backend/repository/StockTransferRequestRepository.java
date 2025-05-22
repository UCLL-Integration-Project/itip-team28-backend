package team28.backend.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import team28.backend.model.Car;
import team28.backend.model.StockTransferRequest;

public interface StockTransferRequestRepository extends JpaRepository<StockTransferRequest, Long> {

    Collection<StockTransferRequest> findByCar(Car car);    
}
