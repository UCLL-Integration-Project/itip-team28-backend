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

import team28.backend.controller.dto.ScanInput;
import team28.backend.exceptions.ScanException;
import team28.backend.model.Car;
import team28.backend.model.Coordinate;
import team28.backend.model.Scan;
import team28.backend.model.Reader;
import team28.backend.repository.CarRepository;
import team28.backend.repository.CoordinateRepository;
import team28.backend.repository.ScanRepository;
import team28.backend.repository.ReaderRepository;
import team28.backend.service.ScanService;

@ExtendWith(MockitoExtension.class)
public class ScanServiceTest {

    @Mock
    private ScanRepository ScanRepository;

    @Mock
    private CarRepository CarRepository;

    @Mock
    private ReaderRepository ReaderRepository;

    @Mock
    private CoordinateRepository CoordinateRepository;

    @InjectMocks
    private ScanService ScanService;

    @Mock
    private Scan scan;

    @Mock
    private Reader reader;

    @Mock
    private Car car;

    @BeforeEach
    void setUp() {
        final var coordinate1 = CoordinateRepository.save(new Coordinate(0, 0));
        reader = new Reader("00-B0-D0-63-C2-26", "Reader1", coordinate1);
        reader.setId(1L);
        car = new Car(1);
        car.setId(1L);
        scan = new Scan(car, reader, LocalDateTime.of(2025, 5, 1, 9, 15));
        scan.setId(1L);
    }

    @Test
    public void givenAllScans_whenAllScansIsRequested_thenGiveListOfAllScans() {
        List<Scan> scans = List.of(scan);
        when(ScanRepository.findAll()).thenReturn(scans);

        List<Scan> result = ScanService.GetAllScans();

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getCar().getNumber());
        assertEquals("Reader1", result.get(0).getReader().getName());
        verify(ScanRepository, times(1)).findAll();
    }

    @Test
    public void givenScanInfo_whenScanIsBeingCreated_thenScanIsAddedToDatabase() {
        ScanInput ScanInput = new ScanInput(car.getId(), reader.getId(), LocalDateTime.of(2025, 5, 1, 9, 15));
        when(CarRepository.findById(car.getId())).thenReturn(Optional.of(car));
        when(ReaderRepository.findById(reader.getId())).thenReturn(Optional.of(reader));
        when(ScanRepository.save(any(Scan.class))).thenReturn(scan);

        Scan result = ScanService.CreateScan(ScanInput);

        assertNotNull(result);
        assertEquals(1, result.getCar().getNumber());
        verify(CarRepository, times(1)).findById(car.getId());
        verify(ReaderRepository, times(1)).findById(reader.getId());
        verify(ScanRepository, times(1)).save(any(Scan.class));
    }

    @Test
    public void givenNonExistingCarInfo_whenScanIsCreated_thenThrowException() {
        ScanInput ScanInput = new ScanInput(car.getId(), reader.getId(), LocalDateTime.of(2025, 5, 1, 9, 15));
        when(CarRepository.findById(car.getId())).thenReturn(Optional.empty());

        ScanException exception = assertThrows(ScanException.class, () -> {
            ScanService.CreateScan(ScanInput);
        });

        assertEquals("Car with ID: 1 doesn't exist", exception.getMessage());
        verify(CarRepository, times(1)).findById(car.getId());
        verify(ScanRepository, never()).save(any(Scan.class));
    }

    @Test
    public void givenNonExistingReaderInfo_whenScanIsCreated_thenThrowException() {
        ScanInput ScanInput = new ScanInput(car.getId(), reader.getId(), LocalDateTime.of(2025, 5, 1, 9, 15));
        when(CarRepository.findById(car.getId())).thenReturn(Optional.of(car));
        when(ReaderRepository.findById(reader.getId())).thenReturn(Optional.empty());

        ScanException exception = assertThrows(ScanException.class, () -> {
            ScanService.CreateScan(ScanInput);
        });

        assertEquals("Reader with ID: 1 doesn't exist", exception.getMessage());
        verify(ReaderRepository, times(1)).findById(reader.getId());
        verify(ScanRepository, never()).save(any(Scan.class));
    }

}
