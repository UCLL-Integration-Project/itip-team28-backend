package team28.backend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import team28.backend.controller.dto.CarInput;
import team28.backend.controller.dto.StockTransferRequestInput;
import team28.backend.exceptions.ServiceException;
import team28.backend.model.Car;
import team28.backend.model.Stock;
import team28.backend.model.StockTransferRequest;
import team28.backend.service.CarService;
import team28.backend.service.StockService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/cars")
public class CarController {

    private final CarService CarService;
    private final StockService stockService;

    public CarController(CarService CarService, StockService stockService) {
        this.stockService = stockService;
        this.CarService = CarService;
    }

    @Operation(summary = "Get all cars")
    @ApiResponse(responseCode = "200", description = "List of cars returned successfully")
    @GetMapping
    public List<Car> GetAllCars() {
        return CarService.GetAllCars();
    }

    @Operation(summary = "Find path to endpoint")
    @PostMapping("/pathfind")
    public void findPath(@RequestBody CarInput carInput) {
        CarService.findPath(carInput.longitude(), carInput.latitude());
    }

    @Operation(summary = "Get stocks for car")
    @ApiResponse(responseCode = "200", description = "List of stocks returned successfully")
    @GetMapping("/{carId}/stocks")
    public List<Stock> getStockForCar(@PathVariable Long carId) {
        return CarService.getStocksForCar(carId);
    }

    @Operation(summary = "Request sock transfer from a reader to a car")
    @ApiResponse(responseCode = "200", description = "Stock transfer request was successfully created")
    @PostMapping("/{carId}/requestPickup")
    public StockTransferRequest requestPickup(@PathVariable Long carId, @RequestBody @Valid StockTransferRequestInput requestInput) {
        return stockService.requestStockPickup(carId, requestInput.readerId(), requestInput.itemId(), requestInput.quantity());
    }

    @Operation(summary = "Request stock delivery from a car to a reader")
    @ApiResponse(responseCode = "200", description = "Stock transfer request was successfully created")
    @PostMapping("/{carId}/requestDelivery")
    public StockTransferRequest requestStockDelivery(@PathVariable Long carId, @RequestBody @Valid StockTransferRequestInput requestInput) {
        return stockService.requestStockDelivery(carId, requestInput.readerId(), requestInput.itemId(), requestInput.quantity());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ ServiceException.class })
    public Map<String, String> handleServiceException(ServiceException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("ServiceException", ex.getMessage());
        return errors;
    }
}
