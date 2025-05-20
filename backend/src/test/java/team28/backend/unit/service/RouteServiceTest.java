package team28.backend.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import team28.backend.controller.dto.RouteInput;
import team28.backend.exceptions.RouteException;
import team28.backend.model.Reader;
import team28.backend.model.Route;
import team28.backend.repository.ReaderRepository;
import team28.backend.repository.RouteRepository;
import team28.backend.service.RouteService;

@ExtendWith(MockitoExtension.class)
public class RouteServiceTest {

    @Mock
    private RouteRepository RouteRepository;

    @Mock
    private ReaderRepository ReaderRepository;

    @InjectMocks
    private RouteService RouteService;

    @Mock
    private Route route;

    @Mock
    private Reader reader1;

    @Mock
    private Reader reader2;

    @BeforeEach
    void setUp() {
        reader1 = new Reader("00-B0-D0-63-C2-26", "Reader1", "40N");
        reader1.setId(1L);

        reader2 = new Reader("01-B1-D1-63-C1-26", "Reader2", "40S");
        reader2.setId(1L);

        route = new Route(true, reader1, reader2, reader1, LocalDateTime.of(2025, 3, 9, 10, 15, 0),
                List.of("step 1", "step 2"));
        route.setId(1L);
    }

    @Test
    public void givenAllRoutes_whenAllRoutesIsRequested_thenGiveListOfAllRoutes() {
        List<Route> routes = List.of(route);
        when(RouteRepository.findAll()).thenReturn(routes);

        List<Route> result = RouteService.GetAllRoutes();

        assertEquals(1, result.size());
        assertEquals("Reader1", result.get(0).getStartingPoint().getName());
        assertEquals("Reader2", result.get(0).getDestination().getName());
        assertEquals("Reader1", result.get(0).getCurrentPoint().getName());
        verify(RouteRepository, times(1)).findAll();
    }

    public void givenRouteInfo_whenRouteIsBeingCreated_thenRouteIsAddedToDatabase() {

        RouteInput RouteInput = new RouteInput(route.isStatus(), route.getStartingPoint().getId(),
                route.getDestination().getId(), route.getCurrentPoint().getId(), route.getTimestamp(),
                route.getInstructions());
        when(ReaderRepository.findById(route.getStartingPoint().getId())).thenReturn(Optional.of(reader1));
        when(ReaderRepository.findById(route.getDestination().getId())).thenReturn(Optional.of(reader2));
        when(ReaderRepository.findById(route.getCurrentPoint().getId())).thenReturn(Optional.of(reader1));
        when(RouteRepository.save(any(Route.class))).thenReturn(route);

        Route result = RouteService.CreateRoute(RouteInput);

        assertNotNull(result);
        verify(RouteRepository, times(1)).save(any(Route.class));
    }

    @Test
    public void givenNonExistingStartingPointInfo_whenRouteIsCreated_thenThrowException() {
        RouteInput RouteInput = new RouteInput(route.isStatus(), route.getStartingPoint().getId(),
                route.getDestination().getId(), route.getCurrentPoint().getId(), route.getTimestamp(),
                route.getInstructions());
        when(ReaderRepository.findById(route.getStartingPoint().getId())).thenReturn(Optional.empty());

        RouteException exception = assertThrows(RouteException.class, () -> {
            RouteService.CreateRoute(RouteInput);
        });

        assertEquals("Reader with ID: 1 doesn't exist", exception.getMessage());
        verify(RouteRepository, never()).save(any(Route.class));
    }

    @Test
    public void givenNonExistingDestinationInfo_whenRouteIsCreated_thenThrowException() {
        RouteInput RouteInput = new RouteInput(route.isStatus(), route.getStartingPoint().getId(),
                route.getDestination().getId(), route.getCurrentPoint().getId(), route.getTimestamp(),
                route.getInstructions());
        when(ReaderRepository.findById(route.getStartingPoint().getId())).thenReturn(Optional.of(reader1));
        when(ReaderRepository.findById(route.getDestination().getId())).thenReturn(Optional.empty());

        RouteException exception = assertThrows(RouteException.class, () -> {
            RouteService.CreateRoute(RouteInput);
        });

        assertEquals("Reader with ID: 1 doesn't exist", exception.getMessage());
        verify(RouteRepository, never()).save(any(Route.class));
    }

    @Test
    public void givenNonExistingCurrentPointInfo_whenRouteIsCreated_thenThrowException() {
        RouteInput RouteInput = new RouteInput(route.isStatus(), route.getStartingPoint().getId(),
                route.getDestination().getId(), route.getCurrentPoint().getId(), route.getTimestamp(),
                route.getInstructions());
        when(ReaderRepository.findById(route.getStartingPoint().getId())).thenReturn(Optional.of(reader1));
        when(ReaderRepository.findById(route.getDestination().getId())).thenReturn(Optional.of(reader2));
        when(ReaderRepository.findById(route.getCurrentPoint().getId())).thenReturn(Optional.empty());

        RouteException exception = assertThrows(RouteException.class, () -> {
            RouteService.CreateRoute(RouteInput);
        });

        assertEquals("Reader with ID: 1 doesn't exist", exception.getMessage());
        verify(RouteRepository, never()).save(any(Route.class));
    }
}
