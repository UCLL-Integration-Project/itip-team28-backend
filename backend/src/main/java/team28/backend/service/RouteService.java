package team28.backend.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import team28.backend.exceptions.ServiceException;
import team28.backend.model.Coordinate;
import team28.backend.model.Grid;
import team28.backend.model.Reader;
import team28.backend.model.Route;
import team28.backend.repository.GridRepository;
import team28.backend.repository.RouteRepository;

@Service
public class RouteService {

    private final GridRepository GridRepository;
    private final RouteRepository RouteRepository;
    private final PathfindingService PathfindingService;

    public RouteService(RouteRepository RouteRepository, GridRepository GridRepository,
            PathfindingService PathfindingService, GridRepository gridRepository) {
        this.RouteRepository = RouteRepository;
        this.GridRepository = GridRepository;
        this.PathfindingService = PathfindingService;
    }

    public List<Route> GetAllRoutes() {
        return RouteRepository.findAll();
    }

    public List<Route> generateRoutes() {
        Grid grid = GridRepository.findFirstByOrderByIdAsc()
                .orElseThrow(() -> new ServiceException("No grid found"));

        List<Coordinate> allCoordinates = grid.getCoordinates();

        List<Coordinate> readerCoordinates = allCoordinates.stream()
                .filter(c -> c.getReader() != null)
                .collect(Collectors.toList());

        if (readerCoordinates.size() < 2) {
            throw new ServiceException("At least two reader coordinates are required to generate routes.");
        }

        List<Route> routes = new ArrayList<>();

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
                            fromReader, // StartingPoint
                            toReader, // destination
                            fromReader, // CurrentPoint (initially the same as StartingPoint)
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
