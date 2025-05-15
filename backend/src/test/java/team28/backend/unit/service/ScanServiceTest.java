package team28.backend.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import team28.backend.model.Scan;
import team28.backend.repository.ScanRepository;
import team28.backend.service.JwtService;
import team28.backend.service.ScanService;
import team28.backend.service.UserDetailsImpl;

@ExtendWith(MockitoExtension.class)
public class ScanServiceTest {

    @Mock
    private ScanRepository ScanRepository;

    @InjectMocks
    private ScanService ScanService;

    @Mock
    private AuthenticationManager AuthenticationManager;

    @Mock
    private Authentication authentication;

    @Mock
    private JwtService JwtService;

    @Mock
    private PasswordEncoder PasswordEncoder;

    @Mock
    private Scan scan;

    @Mock
    private UserDetailsImpl UserDetails;

    @BeforeEach
    void setUp() {
        scan = new Scan("C1", "R1", LocalDateTime.of(2025, 5, 1, 9, 15));
        scan.setId(1L);
    }

    @Test
    public void givenAllScans_whenAllScansIsRequested_thenGiveListOfAllScans() {
        List<Scan> scans = List.of(scan);
        when(ScanRepository.findAll()).thenReturn(scans);

        List<Scan> result = ScanService.GetAllScans();

        assertEquals(1, result.size());
        assertEquals("C1", result.get(0).getCarId());
        assertEquals("R1", result.get(0).getReaderId());
        verify(ScanRepository, times(1)).findAll();
    }

}
