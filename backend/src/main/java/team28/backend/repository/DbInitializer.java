package team28.backend.repository;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import team28.backend.model.Car;
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
    private final CarRepository CarRepository;

    public DbInitializer(PasswordEncoder PasswordEncoder, UserRepository UserRepository,
            ScanRepository ScanRepository, TagRepository TagRepository, CarRepository CarRepository) {
        this.PasswordEncoder = PasswordEncoder;
        this.UserRepository = UserRepository;
        this.ScanRepository = ScanRepository;
        this.TagRepository = TagRepository;
        this.CarRepository = CarRepository;
    }

    public void clearAll() {
        ScanRepository.deleteAll();
        TagRepository.deleteAll();
        CarRepository.deleteAll();
        UserRepository.deleteAll();
    }

    @PostConstruct
    public void init() {
        clearAll();

        final var user = UserRepository
                .save(new User("test", "test@example.com", PasswordEncoder.encode("test"), Role.USER));
        @SuppressWarnings("unused")
        final var user2 = UserRepository
                .save(new User("test2", "test@example.com", PasswordEncoder.encode("test"), Role.MANAGER));

        final var tag1 = TagRepository.save(new Tag(1));
        final var tag2 = TagRepository.save(new Tag(2));

        final var car1 = CarRepository.save(new Car(1));
        final var car2 = CarRepository.save(new Car(2));

        final var scan1 = ScanRepository.save(new Scan(car1, tag1, LocalDateTime.of(2025, 5, 1, 9, 15)));
        final var scan2 = ScanRepository.save(new Scan(car2, tag2, LocalDateTime.of(2025, 5, 1, 11, 18)));

        user.addScan(scan1);
        user.addScan(scan2);

        ScanRepository.save(scan1);
        ScanRepository.save(scan2);
        UserRepository.save(user);
    }
}