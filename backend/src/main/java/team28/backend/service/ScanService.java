package team28.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import team28.backend.model.Scan;
import team28.backend.repository.ScanRepository;

@Service
public class ScanService {
    private final ScanRepository scanRepository;

    public ScanService(ScanRepository scanRepository) {
        this.scanRepository = scanRepository;
    }

    public List<Scan> GetAllScans() {
        return scanRepository.findAll();
    }
}
