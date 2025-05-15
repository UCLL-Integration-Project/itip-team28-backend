package team28.backend.repository;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import team28.backend.model.Role;
import team28.backend.model.Scan;
import team28.backend.model.User;

@Component
public class DbInitializer {

    private final UserRepository UserRepository;
    private final PasswordEncoder PasswordEncoder;
    private final ScanRepository scanRepository;

    public DbInitializer(PasswordEncoder PasswordEncoder, UserRepository UserRepository, ScanRepository scanRepository) {
        this.PasswordEncoder = PasswordEncoder;
        this.UserRepository = UserRepository;
        this.scanRepository = scanRepository;
    }

    public void clearAll() {
        UserRepository.deleteAll();
    }

    @PostConstruct
    public void init() {
        clearAll();

        @SuppressWarnings("unused")
        final var user = UserRepository
                .save(new User("test", "test@example.com", PasswordEncoder.encode("test"), Role.USER));
        @SuppressWarnings("unused")
        final var user2 = UserRepository
                .save(new User("test2", "test@example.com", PasswordEncoder.encode("test"), Role.MANAGER));
        final var scan1 = ScanRepository.save(new Scan("1", "macaddr1", LocalDateTime.of(2025, 5, 1, 9, 15)))
    }
}