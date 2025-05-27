package team28.backend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
@RequestMapping("/stocktransferrequests")
public class StockTransferRequestController {
    private final StockService StockService;
    private final StockTransferRequestRepository StockTransferRequestRepository;
    private final CarRepository CarRepository;

    public StockTransferRequestController(StockService StockService,
            StockTransferRequestRepository StockTransferRequestRepository, CarRepository CarRepository) {
        this.CarRepository = CarRepository;
        this.StockService = StockService;
        this.StockTransferRequestRepository = StockTransferRequestRepository;
    }

    //Retrieves all pending stock transfer requests for a specific car. 
    //You put in the carId as a path variable and it returns all pending requests for that car.
    @Operation(summary = "Get pending stock transfer requests for a car")
    @ApiResponse(responseCode = "200", description = "List of pending stock transfer requests returned successfully")
    @GetMapping("/pending/{carId}")
    public List<StockTransferRequest> getPendingRequestsForCar(@PathVariable Long carId) {
        Car car = CarRepository.findById(carId)
                .orElseThrow(() -> new ServiceException("Car with id " + carId + " not found"));
        return StockTransferRequestRepository.findByCar(car).stream()
                .filter(request -> request.getStatus() == TransferStatus.PENDING)
                .collect(Collectors.toList());
    }

    // Sets the status of a stock transfer to COMPLETE and updates the stock quantities on both car and reader
    // You put in which request you want to complete by providing the requestId as a path variable
    @Operation(summary = "Complete a stock transfer request")
    @ApiResponse(responseCode = "200", description = "Stock transfer request was successfully set to COMPLETE")
    @PutMapping("{requestId}/complete")
    public void completeRequest(@PathVariable Long requestId) {
        StockService.completeStockTransfer(requestId);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ ServiceException.class })
    public Map<String, String> handleServiceException(ServiceException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("ServiceException", ex.getMessage());
        return errors;
    }
}
