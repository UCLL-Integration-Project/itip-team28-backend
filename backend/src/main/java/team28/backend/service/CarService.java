package team28.backend.service;

import java.util.List;

import java.util.Optional;
import org.springframework.stereotype.Service;

import team28.backend.exceptions.ServiceException;
import team28.backend.model.Car;
import team28.backend.model.Reader;
import team28.backend.model.Stock;
import team28.backend.repository.CarRepository;

@Service
public class CarService {

    private final CarRepository CarRepository;
    private final PathfindingService PathfindingService;
    private final ReaderService ReaderService;
    private final StockService StockService;

    public CarService(CarRepository CarRepository, PathfindingService PathfindingService, ReaderService ReaderService, StockService StockService) {
        this.StockService = StockService;
        this.CarRepository = CarRepository;
        this.PathfindingService = PathfindingService;
        this.ReaderService = ReaderService;
    }

    public List<Car> GetAllCars() {
        return CarRepository.findAll();
    }

    public void findPath(int longitude, int latitude) {
        List<Reader> readers = ReaderService.GetAllReaders();

        Optional<Reader> endReader = readers.stream()
                .filter(r -> r.getCoordinates().getLongitude() == longitude
                        && r.getCoordinates().getLatitude() == latitude)
                .findFirst();

        if (endReader.isPresent()) {
            Reader start = readers.get(0);
            Reader end = endReader.get();

            System.out.println(PathfindingService.findPath(start, end, readers));
        } else {
            System.out.println("No reader found at the given coordinates.");
        }
    }

    public List<Stock> getStocksForCar(Long carId) {
        Car car = CarRepository.findById(carId)
            .orElseThrow(() -> new ServiceException("Car with id " + carId + " not found"));

        return StockService.getStocksForHolder(car);
    }
}
