package team28.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import team28.backend.exceptions.ServiceException;
import team28.backend.model.Scan;
import team28.backend.service.ScanService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @ExceptionHandler({ ServiceException.class })
    public Map<String, String> handleServiceException(ServiceException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("ServiceException", ex.getMessage());
        return errors;
    }
}
