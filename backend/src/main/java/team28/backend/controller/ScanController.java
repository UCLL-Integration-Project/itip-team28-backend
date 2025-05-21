package team28.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import team28.backend.exceptions.ScanException;
import team28.backend.model.Scan;
import team28.backend.service.ScanService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;

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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ ScanException.class })
    public String handleValidationExceptions(ScanException ex) {
        return ex.getMessage();
    }
}
