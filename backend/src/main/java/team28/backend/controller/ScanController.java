package team28.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import team28.backend.controller.dto.ScanInput;
import team28.backend.model.Scan;
import team28.backend.service.ScanService;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/scans")
public class ScanController {

    private final ScanService ScanService;

    public ScanController(ScanService ScanService) {
        this.ScanService = ScanService;
    }

    @GetMapping
    public List<Scan> GetAllScans() {
        return ScanService.GetAllScans();
    }

    @PostMapping
    public Scan CreateScan(@Valid @RequestBody ScanInput ScanInput) {
        return ScanService.CreateScan(ScanInput);
    }

}
