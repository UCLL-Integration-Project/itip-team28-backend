package team28.backend.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team28.backend.model.Coordinate;
import team28.backend.model.Grid;
import team28.backend.repository.CoordinateRepository;
import team28.backend.repository.GridRepository;

@Service
public class GridService {

    @Autowired
    private GridRepository gridRepository;

    @Autowired
    private CoordinateRepository CoordinateRepository;

    public Grid createGrid(List<Coordinate> coordinates, int measurement) {
        gridRepository.deleteAll();

        Grid grid = new Grid();

        for (Coordinate coordinate : coordinates) {
            grid.addCoordinate(coordinate);
        }

        grid.setMeasurement(measurement);

        return gridRepository.save(grid);
    }

    public List<Grid> getAllGrids() {
        return gridRepository.findAll();
    }

    public Grid getGridById(Long id) {
        return gridRepository.findById(id).orElse(null);
    }
}
