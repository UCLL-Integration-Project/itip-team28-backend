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

        var car = CarRepository.findById(ScanInput.carId())
                .orElseThrow(() -> new ScanException("Car with ID: " + ScanInput.carId() + " doesn't exist"));

        var tag = TagRepository.findById(ScanInput.tagId())
                .orElseThrow(() -> new ScanException("Tag with ID: " + ScanInput.tagId() + " doesn't exist"));

        var scan = new Scan(car, tag, ScanInput.timestamp());

        return ScanRepository.save(scan);
    }
}
