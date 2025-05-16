package team28.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import team28.backend.controller.dto.ScanInput;
import team28.backend.exceptions.ScanException;
import team28.backend.model.Scan;
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
        boolean ExistsCar = CarRepository.existsById(ScanInput.carId().getId());
        if (!ExistsCar) {
            throw new ScanException("Car with ID: " + ScanInput.carId().getId() + " doesn't exist");
        }

        boolean ExistsTag = TagRepository.existsById(ScanInput.tagId().getId());
        if (!ExistsTag) {
            throw new ScanException("Tag with ID: " + ScanInput.tagId().getId() + " doesn't exist");
        }

        final var scan = new Scan(
                ScanInput.carId(),
                ScanInput.tagId(),
                ScanInput.timestamp());

        return ScanRepository.save(scan);
    }
}
