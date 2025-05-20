package team28.backend.service;

import java.util.List;

import java.util.Optional;
import org.springframework.stereotype.Service;

import team28.backend.model.Car;
import team28.backend.model.Reader;
import team28.backend.repository.CarRepository;

@Service
public class CarService {

    private final CarRepository CarRepository;
    private final PathfindingService PathfindingService;
    private final ReaderService ReaderService;

    public CarService(CarRepository CarRepository, PathfindingService PathfindingService, ReaderService ReaderService) {
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
                .filter(r -> r.getCoordinates().getlongitude() == longitude
                        && r.getCoordinates().getlatitude() == latitude)
                .findFirst();

        if (endReader.isPresent()) {
            Reader start = readers.get(0);
            Reader end = endReader.get();

            System.out.println(PathfindingService.findPath(start, end, readers));
        } else {
            System.out.println("No reader found at the given coordinates.");
        }
    }

}
