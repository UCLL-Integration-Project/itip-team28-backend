package team28.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import team28.backend.exceptions.ServiceException;
import team28.backend.model.Car;
import team28.backend.model.Stock;
import team28.backend.repository.CarRepository;

@Service
public class CarService {

    private final CarRepository CarRepository;
    private final StockService StockService;

    public CarService(CarRepository CarRepository, StockService StockService) {
        this.CarRepository = CarRepository;
        this.StockService = StockService;
    }

    public List<Car> GetAllCars() {
        return CarRepository.findAll();
    }

    // retrieves all the stock for this car
    public List<Stock> getStocksForCar(Long carId) {
        Car car = CarRepository.findById(carId)
                .orElseThrow(() -> new ServiceException("Car with id " + carId + " not found"));

        return StockService.getStocksForHolder(car);
    }
}
