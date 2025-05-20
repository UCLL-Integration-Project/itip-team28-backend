package team28.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import team28.backend.controller.dto.CarInput;
import team28.backend.model.Car;
import team28.backend.service.CarService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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

    @Operation(summary = "Find path to endpoint")
    @PostMapping("/pathfind")
    public void findPath(@RequestBody CarInput carInput) {
        CarService.findPath(carInput.longitude(), carInput.latitude());
    }
}
