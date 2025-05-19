package team28.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(summary = "Get all scans")
    @ApiResponse(responseCode = "200", description = "List of scans returned successfully")
    @GetMapping
    public List<Scan> GetAllScans() {
        return ScanService.GetAllScans();
    }

    @Operation(summary = "Create new scan")
    @ApiResponse(responseCode = "200", description = "Scan is successfully created")
    @PostMapping
    public Scan CreateScan(@Valid @RequestBody ScanInput ScanInput) {
        return ScanService.CreateScan(ScanInput);
    }

}
