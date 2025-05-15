package team28.backend.repository;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import team28.backend.model.Role;
import team28.backend.model.Scan;
import team28.backend.model.Tag;
import team28.backend.model.User;

@Component
public class DbInitializer {

    private final UserRepository UserRepository;
    private final PasswordEncoder PasswordEncoder;
    private final ScanRepository ScanRepository;
    private final TagRepository TagRepository;

    public DbInitializer(PasswordEncoder PasswordEncoder, UserRepository UserRepository,
            ScanRepository ScanRepository, TagRepository TagRepository) {
        this.PasswordEncoder = PasswordEncoder;
        this.UserRepository = UserRepository;
        this.ScanRepository = ScanRepository;
        this.TagRepository = TagRepository;
    }

    public void clearAll() {
        UserRepository.deleteAll();
        ScanRepository.deleteAll();
        TagRepository.deleteAll();
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

        @SuppressWarnings("unused")
        final var tag1 = TagRepository.save(new Tag("Tag_123"));

        @SuppressWarnings("unused")
        final var tag2 = TagRepository.save(new Tag("Tag_456"));

        @SuppressWarnings("unused")
        final var scan1 = ScanRepository.save(new Scan("1", "macaddr1", LocalDateTime.of(2025, 5, 1, 9, 15)));
        @SuppressWarnings("unused")
        final var scan2 = ScanRepository.save(new Scan("1", "macaddr2", LocalDateTime.of(2025, 5, 1, 11, 18)));
    }
}