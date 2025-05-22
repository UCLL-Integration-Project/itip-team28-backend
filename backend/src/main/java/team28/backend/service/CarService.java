package team28.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import team28.backend.model.Car;
import team28.backend.repository.CarRepository;

@Service
public class CarService {

    private final CarRepository CarRepository;

    public CarService(CarRepository CarRepository) {
        this.CarRepository = CarRepository;
    }

    public List<Car> GetAllCars() {
        return CarRepository.findAll();
    }
}
