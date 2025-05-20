package team28.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import team28.backend.controller.dto.RouteInput;
import team28.backend.exceptions.RouteException;
import team28.backend.model.Reader;
import team28.backend.model.Route;
import team28.backend.repository.ReaderRepository;
import team28.backend.repository.RouteRepository;

@Service
public class RouteService {

    private final RouteRepository RouteRepository;
    private final ReaderRepository ReaderRepository;

    public RouteService(RouteRepository RouteRepository, ReaderRepository ReaderRepository) {
        this.RouteRepository = RouteRepository;
        this.ReaderRepository = ReaderRepository;
    }

    public List<Route> GetAllRoutes() {
        return RouteRepository.findAll();
    }

    public Route CreateRoute(RouteInput RouteInput) {
        Optional<Reader> StartingPointReaderId = ReaderRepository.findById(RouteInput.startingPointReaderId());
        if (StartingPointReaderId.isEmpty()) {
            throw new RouteException("Reader with ID: " + RouteInput.startingPointReaderId() + " doesn't exist");
        }
        Reader StartingPoint = StartingPointReaderId.get();

        Optional<Reader> DestinationReaderId = ReaderRepository.findById(RouteInput.destinationReaderId());
        if (DestinationReaderId.isEmpty()) {
            throw new RouteException("Reader with ID: " + RouteInput.destinationReaderId() + " doesn't exist");
        }
        Reader destination = DestinationReaderId.get();

        Optional<Reader> CurrentPointReaderId = ReaderRepository.findById(RouteInput.currentPointReaderId());
        if (CurrentPointReaderId.isEmpty()) {
            throw new RouteException("Reader with ID: " + RouteInput.currentPointReaderId() + " doesn't exist");
        }
        Reader CurrentPoint = DestinationReaderId.get();

        var route = new Route(RouteInput.status(), StartingPoint, destination, CurrentPoint, RouteInput.timestamp(),
                RouteInput.instructions());

        return RouteRepository.save(route);
    }
}
