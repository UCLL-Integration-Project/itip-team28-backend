package team28.backend.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import team28.backend.controller.dto.ReaderInput;
import team28.backend.exceptions.ReaderException;
import team28.backend.model.Coordinate;
import team28.backend.model.Reader;
import team28.backend.repository.CoordinateRepository;
import team28.backend.repository.ReaderRepository;
import team28.backend.service.ReaderService;

@ExtendWith(MockitoExtension.class)
public class ReaderServiceTest {

    @Mock
    private ReaderRepository ReaderRepository;

    @Mock
    private CoordinateRepository CoordinateRepository;

    @InjectMocks
    private ReaderService ReaderService;

    @Mock
    private Reader reader;

    @BeforeEach
    void setUp() {
        final var coordinate1 = CoordinateRepository.save(new Coordinate(0, 0));
        reader = new Reader("00-B0-D0-63-C2-26", "Reader1", coordinate1);
        reader.setId(1L);
    }

    @Test
    public void givenAllReaders_whenAllReadersIsRequested_thenGiveListOfAllReaders() {
        List<Reader> readers = List.of(reader);
        when(ReaderRepository.findAll()).thenReturn(readers);

        List<Reader> result = ReaderService.GetAllReaders();

        assertEquals(1, result.size());
        assertEquals("00-B0-D0-63-C2-26", result.get(0).getMacAddress());
        assertEquals("Reader1", result.get(0).getName());
        verify(ReaderRepository, times(1)).findAll();
    }

    @Test
    public void givenReaderInfo_whenReaderIsBeingCreated_thenReaderIsAddedToDatabase() {
        Coordinate coordinate = new Coordinate(10,
                20);
        when(CoordinateRepository.save(any(Coordinate.class))).thenReturn(coordinate);

        ReaderInput ReaderInput = new ReaderInput(reader.getMacAddress(), reader.getName(), coordinate);
        when(ReaderRepository.save(any(Reader.class))).thenReturn(reader);

        Reader result = ReaderService.CreateReader(ReaderInput);

        assertNotNull(result);
        verify(ReaderRepository, times(1)).save(any(Reader.class));
    }

    @Test
    public void givenReaderId_whenDeletingReader_thenDeleteThatReader() {
        when(ReaderRepository.existsById(1L)).thenReturn(true);

        ReaderService.DeleteReader(1L);

        verify(ReaderRepository, times(1)).existsById(1L);
        verify(ReaderRepository, times(1)).deleteById(1L);
    }

    @Test
    public void givenUserIdTHatDoesntExists_whenDeletingUser_thenThrowException() {
        when(ReaderRepository.existsById(1L)).thenReturn(false);

        ReaderException exception = assertThrows(ReaderException.class, () -> {
            ReaderService.DeleteReader(1L);
        });

        assertEquals("Reader not found", exception.getMessage());
        verify(ReaderRepository, times(1)).existsById(1L);
        verify(ReaderRepository, never()).deleteById(1L);
    }
}
