package team28.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import team28.backend.controller.dto.ScanInput;
import team28.backend.exceptions.ServiceException;
import team28.backend.model.Car;
import team28.backend.model.Scan;
import team28.backend.model.Reader;
import team28.backend.repository.CarRepository;
import team28.backend.repository.ScanRepository;
import team28.backend.repository.ReaderRepository;

@Service
public class ScanService {
    private final ScanRepository ScanRepository;
    private final CarRepository CarRepository;
    private final ReaderRepository ReaderRepository;

    public ScanService(ScanRepository ScanRepository, CarRepository CarRepository, ReaderRepository ReaderRepository) {
        this.ScanRepository = ScanRepository;
        this.CarRepository = CarRepository;
        this.ReaderRepository = ReaderRepository;
    }

    public List<Scan> GetAllScans() {
        return ScanRepository.findAll();
    }

    public Scan CreateScan(ScanInput ScanInput) {

        Optional<Car> CarOptional = CarRepository.findById(ScanInput.carId());
        if (CarOptional.isEmpty()) {
            throw new ServiceException("Car with ID: " + ScanInput.carId() + " doesn't exist");
        }
        Car car = CarOptional.get();

        Optional<Reader> ReaderOptional = ReaderRepository.findById(ScanInput.tagId());
        if (ReaderOptional.isEmpty()) {
            throw new ServiceException("Reader with ID: " + ScanInput.tagId() + " doesn't exist");
        }
        Reader reader = ReaderOptional.get();

        var scan = new Scan(car, reader, ScanInput.timestamp());

        return ScanRepository.save(scan);
    }
}
