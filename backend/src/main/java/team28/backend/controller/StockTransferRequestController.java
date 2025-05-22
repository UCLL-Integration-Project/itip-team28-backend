package team28.backend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import team28.backend.exceptions.ServiceException;
import team28.backend.model.Car;
import team28.backend.model.StockTransferRequest;
import team28.backend.model.TransferStatus;
import team28.backend.repository.CarRepository;
import team28.backend.repository.StockTransferRequestRepository;
import team28.backend.service.StockService;

@RestController
@RequestMapping("/stockTransferRequests")
public class StockTransferRequestController {
    private final StockService stockService;
    private final StockTransferRequestRepository stockTransferRequestRepository;
    private final CarRepository carRepository;

    public StockTransferRequestController(StockService stockService, StockTransferRequestRepository stockTransferRequestRepository, CarRepository carRepository) {
        this.carRepository = carRepository;
        this.stockService = stockService;
        this.stockTransferRequestRepository = stockTransferRequestRepository;
    }

    @Operation(summary = "Get pending stock transfer requests for a car")
    @ApiResponse(responseCode = "200", description = "List of pending stock transfer requests returned successfully")
    @GetMapping("/pending/{carId}") 
    public List<StockTransferRequest> getPendingRequestsForCar(@PathVariable Long carId) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new ServiceException("Car with id " + carId + " not found"));
        return stockTransferRequestRepository.findByCar(car).stream()
                .filter(request -> request.getStatus() == TransferStatus.PENDING)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Complete a stock transfer request")
    @ApiResponse(responseCode = "200", description = "Stock transfer request was successfully set to COMPLETE")
    @PutMapping("{requestId}/complete")
    public void completeRequest(@PathVariable Long requestId) {
        stockService.completeStockTransfer(requestId);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ ServiceException.class })
    public Map<String, String> handleServiceException(ServiceException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("ServiceException", ex.getMessage());
        return errors;
    }
}
