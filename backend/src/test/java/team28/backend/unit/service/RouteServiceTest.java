package team28.backend.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import team28.backend.controller.dto.ReaderInput;
import team28.backend.controller.dto.RouteInput;
import team28.backend.model.Reader;
import team28.backend.model.Route;
import team28.backend.repository.ReaderRepository;
import team28.backend.repository.RouteRepository;
import team28.backend.service.ReaderService;
import team28.backend.service.RouteService;

@ExtendWith(MockitoExtension.class)
public class RouteServiceTest {

    @Mock
    private RouteRepository RouteRepository;

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

    @Test
    public void givenRouteInfo_whenRouteIsBeingCreated_thenRouteIsAddedToDatabase() {
        RouteInput RouteInput = new RouteInput(RouteInput.status(), RouteInput.startingPointReaderId(),
                RouteInput.destinationReaderId(), RouteInput.currentPointReaderId(), RouteInput.timestamp(),
                RouteInput.instructions());
        when(RouteRepository.save(any(Route.class))).thenReturn(route);

        Reader result = ReaderService.CreateReader(ReaderInput);

        assertNotNull(result);
        verify(ReaderRepository, times(1)).save(any(Reader.class));
    }
}
