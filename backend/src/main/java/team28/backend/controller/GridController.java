package team28.backend.controller;

import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import team28.backend.controller.dto.GridInput;
import team28.backend.exceptions.ServiceException;
import team28.backend.model.Coordinate;
import team28.backend.model.Grid;
import team28.backend.service.GridService;

@RestController
@RequestMapping("/api/grids")
public class GridController {

    private final GridService gridService;

    public GridController(GridService gridService) {
        this.gridService = gridService;
    }

    @Operation(summary = "Create new grid")
    @ApiResponse(responseCode = "200", description = "The created grid")
    @PostMapping
    public ResponseEntity<Grid> createGrid(@RequestBody GridInput input) {

        List<Coordinate> coordinates = input.coordinates().stream()
                .map(coordInput -> new Coordinate(coordInput.longitude(), coordInput.latitude()))
                .collect(Collectors.toList());

        Grid savedGrid = gridService.createGrid(coordinates);

        return ResponseEntity.ok(savedGrid);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ ServiceException.class })
    public Map<String, String> handleServiceException(ServiceException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("ServiceException", ex.getMessage());
        return errors;
    }
}
