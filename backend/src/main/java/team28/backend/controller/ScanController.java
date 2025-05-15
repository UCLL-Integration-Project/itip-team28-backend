package team28.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import team28.backend.model.Scan;
import team28.backend.service.ScanService;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/scans")
public class ScanController {
    
    private final ScanService scanService;

    public ScanController(ScanService scanService) {
        this.scanService = scanService;
    }

    @GetMapping
    public List<Scan> getAllScans() {
        return scanService.GetAllScans();
    }
    
}
