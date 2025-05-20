package team28.backend.repository;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import team28.backend.model.Car;
import team28.backend.model.Coordinate;
import team28.backend.model.Role;
import team28.backend.model.Scan;
import team28.backend.model.Reader;
import team28.backend.model.User;

@Component
public class DbInitializer {

    private final UserRepository UserRepository;
    private final PasswordEncoder PasswordEncoder;
    private final ScanRepository ScanRepository;
    private final ReaderRepository ReaderRepository;
    private final CarRepository CarRepository;
    private final CoordinateRepository CoordinateRepository;

    public DbInitializer(PasswordEncoder PasswordEncoder, UserRepository UserRepository,
            ScanRepository ScanRepository, ReaderRepository ReaderRepository, CarRepository CarRepository, CoordinateRepository CoordinateRepository) {
        this.PasswordEncoder = PasswordEncoder;
        this.UserRepository = UserRepository;
        this.ScanRepository = ScanRepository;
        this.ReaderRepository = ReaderRepository;
        this.CarRepository = CarRepository;
        this.CoordinateRepository = CoordinateRepository;
    }

    public void clearAll() {
        ScanRepository.deleteAll();
        ReaderRepository.deleteAll();
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

        final var coordinate1 = CoordinateRepository.save(new Coordinate(0, 0));
        final var coordinate2 = CoordinateRepository.save(new Coordinate(1, 0));
        final var coordinate3 = CoordinateRepository.save(new Coordinate(0, 1));
        final var coordinate4 = CoordinateRepository.save(new Coordinate(1, 1));
        final var coordinate5 = CoordinateRepository.save(new Coordinate(2, 1));
    
        final var reader1 = ReaderRepository.save(new Reader("00-B0-D0-63-C2-26", "Reader1", coordinate1));
        final var reader2 = ReaderRepository.save(new Reader("01-B1-D1-64-C3-27", "Reader2", coordinate2));
        @SuppressWarnings("unused")
        final var reader3 = ReaderRepository.save(new Reader("02-B2-D2-65-C4-28", "Reader3", coordinate3));
        @SuppressWarnings("unused")
        final var reader4 = ReaderRepository.save(new Reader("03-B3-D3-66-C5-29", "Reader4", coordinate4));
        @SuppressWarnings("unused")
        final var reader5 = ReaderRepository.save(new Reader("04-B4-D4-67-C6-2A", "Reader5", coordinate5));

        final var car1 = CarRepository.save(new Car(1));
        final var car2 = CarRepository.save(new Car(2));

        final var scan1 = ScanRepository.save(new Scan(car1, reader1, LocalDateTime.of(2025, 5, 1, 9, 15)));
        final var scan2 = ScanRepository.save(new Scan(car2, reader2, LocalDateTime.of(2025, 5, 1, 11, 18)));

        user.addScan(scan1);
        user.addScan(scan2);

        ScanRepository.save(scan1);
        ScanRepository.save(scan2);
        UserRepository.save(user);
    }
}