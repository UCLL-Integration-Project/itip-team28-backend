package team28.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import team28.backend.controller.dto.ScanInput;
import team28.backend.exceptions.ScanException;
import team28.backend.model.Car;
import team28.backend.model.Scan;
import team28.backend.model.Tag;
import team28.backend.repository.CarRepository;
import team28.backend.repository.ScanRepository;
import team28.backend.repository.TagRepository;

@Service
public class ScanService {
    private final ScanRepository ScanRepository;
    private final CarRepository CarRepository;
    private final TagRepository TagRepository;

    public ScanService(ScanRepository ScanRepository, CarRepository CarRepository, TagRepository TagRepository) {
        this.ScanRepository = ScanRepository;
        this.CarRepository = CarRepository;
        this.TagRepository = TagRepository;
    }

    public List<Scan> GetAllScans() {
        return ScanRepository.findAll();
    }

    public Scan CreateScan(ScanInput ScanInput) {

        Optional<Car> carOptional = CarRepository.findById(ScanInput.carId());
        if (carOptional.isEmpty()) {
            throw new ScanException("Car with ID: " + ScanInput.carId() + " doesn't exist");
        }
        Car car = carOptional.get();

        Optional<Tag> tagOptional = TagRepository.findById(ScanInput.tagId());
        if (tagOptional.isEmpty()) {
            throw new ScanException("Tag with ID: " + ScanInput.tagId() + " doesn't exist");
        }
        Tag tag = tagOptional.get();

        var scan = new Scan(car, tag, ScanInput.timestamp());

        return ScanRepository.save(scan);
    }
}
