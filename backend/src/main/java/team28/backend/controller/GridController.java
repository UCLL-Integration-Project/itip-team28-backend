package team28.backend.controller;

import java.util.stream.Collectors;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team28.backend.controller.dto.GridInput;
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

    @PostMapping
    public ResponseEntity<Grid> createGrid(@RequestBody GridInput input) {

        List<Coordinate> coordinates = input.coordinates().stream()
                .map(coordInput -> new Coordinate(coordInput.longitude(), coordInput.latitude()))
                .collect(Collectors.toList());

        Grid savedGrid = gridService.createGrid(coordinates);

        return ResponseEntity.ok(savedGrid);
    }
}
