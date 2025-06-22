package team28.backend.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import team28.backend.exceptions.ServiceException;
import team28.backend.model.Coordinate;
import team28.backend.model.Grid;
import team28.backend.model.Reader;
import team28.backend.model.Route;
import team28.backend.repository.GridRepository;
import team28.backend.repository.ReaderRepository;
import team28.backend.repository.RouteRepository;

@Service
public class RouteService {

    private final GridRepository GridRepository;
    private final RouteRepository RouteRepository;
    private final PathfindingService PathfindingService;
    private final ReaderRepository ReaderRepository;

    public RouteService(RouteRepository RouteRepository, GridRepository GridRepository,
            PathfindingService PathfindingService, GridRepository gridRepository, ReaderRepository ReaderRepository) {
        this.RouteRepository = RouteRepository;
        this.GridRepository = GridRepository;
        this.PathfindingService = PathfindingService;
        this.ReaderRepository = ReaderRepository;
    }

    public List<Route> GetAllRoutes() {
        return RouteRepository.findAll();
    }

    @Transactional
    public List<Route> generateRoutes() {
        RouteRepository.deleteAll();

        List<Route> routes = new ArrayList<>();

        Grid grid = GridRepository.findFirstByOrderByIdAsc()
                .orElse(null);

        if (grid == null) {
            System.out.println("Grid not found. Please create a grid first.");
            return routes;
        }

        List<Coordinate> allCoordinates = grid.getCoordinates();

        List<Coordinate> readerCoordinates = allCoordinates.stream()
                .filter(c -> c.getReader() != null && ReaderRepository.existsById(c.getReader().getId()))
                .collect(Collectors.toList());


        if (readerCoordinates.size() < 2) {
            System.out.println("At least two reader coordinates are required to generate routes.");
            return routes;
        }

        for (int i = 0; i < readerCoordinates.size(); i++) {
            for (int j = 0; j < readerCoordinates.size(); j++) {
                if (i == j)
                    continue;

                Coordinate fromCoord = readerCoordinates.get(i);
                Coordinate toCoord = readerCoordinates.get(j);

                Reader fromReader = fromCoord.getReader();
                Reader toReader = toCoord.getReader();

                List<String> instructions = PathfindingService.findPath(fromCoord, toCoord, allCoordinates);

                if (!instructions.isEmpty()) {
                    Route route = new Route(
                            false, // status
                            fromReader, // startingPoint
                            toReader, // destination
                            fromReader, // currentPoint (initially the same as startingPoint)
                            LocalDateTime.now(), // timestamp
                            instructions // instructions
                    );
                    routes.add(route);
                } else {
                    System.out.println("No path found between " + fromReader.getName() + " and " + toReader.getName());
                }
            }
        }

        return RouteRepository.saveAll(routes);
    }

}
