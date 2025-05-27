package team28.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import team28.backend.controller.dto.ReaderInput;
import team28.backend.controller.dto.ReaderUpdateInput;
import team28.backend.controller.dto.StockInput;
import team28.backend.exceptions.ServiceException;
import team28.backend.model.Reader;
import team28.backend.model.Stock;
import team28.backend.service.ReaderService;

import java.util.*;

@RestController
@RequestMapping("/readers")
public class ReaderController {

    private final ReaderService ReaderService;

    public ReaderController(ReaderService ReaderService) {
        this.ReaderService = ReaderService;
    }

    @Operation(summary = "Get all readers")
    @ApiResponse(responseCode = "200", description = "List of readers returned successfully")
    @GetMapping
    public List<Reader> GetAllReaders() {
        return ReaderService.GetAllReaders();
    }

    @Operation(summary = "Create new reader")
    @ApiResponse(responseCode = "200", description = "Reader was successfully created")
    @PostMapping
    public Reader CreateReader(@Valid @RequestBody ReaderInput ReaderInput) {
        return ReaderService.CreateReader(ReaderInput);
    }

    @Operation(summary = "Update reader")
    @ApiResponse(responseCode = "200", description = "Reader was successfully updated")
    @PutMapping
    public Reader UpdateReader(@Valid @RequestBody ReaderUpdateInput reader) {
        return ReaderService.UpdateReader(reader);
    }

    @Operation(summary = "Delete reader")
    @ApiResponse(responseCode = "200", description = "Reader was successfully deleted")
    @DeleteMapping
    public String DeleteReader(@RequestBody Reader reader) {
        ReaderService.DeleteReader(reader.getId());
        return "Reader deleted";
    }

    @Operation(summary = "Get stocks for reader")
    @ApiResponse(responseCode = "200", description = "List of stocks returned successfully")
    @GetMapping("/{readerId}/stocks")
    public List<Stock> getStocksForReader(@PathVariable Long readerId) {
        return ReaderService.getStockForReader(readerId);
    }

    @Operation(summary = "Add stock to a reader")
    @ApiResponse(responseCode = "200", description = "Stock was successfully added to the reader")
    @PostMapping("/{readerId}/stocks")
    public Stock addStockToReader(@PathVariable Long readerId, @RequestBody @Valid StockInput stockInput) {
        return ReaderService.addStockToReader(readerId, stockInput.itemId(), stockInput.quantity());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ ServiceException.class })
    public Map<String, String> handleServiceException(ServiceException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("ServiceException", ex.getMessage());
        return errors;
    }

    @GetMapping("/config")
    public ResponseEntity<String> getReaderConfig(@RequestParam("mac") String macAddress) {
        String name = ReaderService.getReaderName(macAddress);
        return ResponseEntity.ok(name);
    }
}
