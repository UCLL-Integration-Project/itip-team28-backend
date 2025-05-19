package team28.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import team28.backend.model.Car;
import team28.backend.service.CarService;

@RestController
@RequestMapping("/cars")
public class CarController {

    private final CarService CarService;

    public CarController(CarService CarService) {
        this.CarService = CarService;
    }

    @Operation(summary = "Get all cars")
    @ApiResponse(responseCode = "200", description = "List of cars returned successfully")
    @GetMapping
    public List<Car> GetAllCars() {
        return CarService.GetAllCars();
    }
}
