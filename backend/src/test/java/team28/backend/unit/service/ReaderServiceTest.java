package team28.backend.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

import team28.backend.model.Reader;
import team28.backend.repository.ReaderRepository;
import team28.backend.service.ReaderService;

@ExtendWith(MockitoExtension.class)
public class ReaderServiceTest {

    @Mock
    private ReaderRepository ReaderRepository;

    @InjectMocks
    private ReaderService ReaderService;

    @Mock
    private Reader reader;

    @BeforeEach
    void setUp() {
        reader = new Reader("00-B0-D0-63-C2-26", "Reader1", "40N");
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
        assertEquals("40N", result.get(0).getCoordinates());
        verify(ReaderRepository, times(1)).findAll();
    }
}
