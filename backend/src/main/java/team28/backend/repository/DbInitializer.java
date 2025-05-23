package team28.backend.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import team28.backend.model.Car;
import team28.backend.model.Coordinate;
import team28.backend.model.Role;
import team28.backend.model.Route;
import team28.backend.model.Reader;
import team28.backend.model.User;

@Component
public class DbInitializer {

    private final UserRepository UserRepository;
    private final PasswordEncoder PasswordEncoder;
    private final ReaderRepository ReaderRepository;
    private final CarRepository CarRepository;
    private final CoordinateRepository CoordinateRepository;
    private final RouteRepository RouteRepository;

    public DbInitializer(PasswordEncoder PasswordEncoder, UserRepository UserRepository,
            ReaderRepository ReaderRepository, CarRepository CarRepository,
            CoordinateRepository CoordinateRepository,
            RouteRepository RouteRepository) {
        this.PasswordEncoder = PasswordEncoder;
        this.UserRepository = UserRepository;
        this.ReaderRepository = ReaderRepository;
        this.CarRepository = CarRepository;
        this.CoordinateRepository = CoordinateRepository;
        this.RouteRepository = RouteRepository;
    }

    public void clearAll() {
        RouteRepository.deleteAll();
        ReaderRepository.deleteAll();
        CarRepository.deleteAll();
        CoordinateRepository.deleteAll();
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

        final var reader1 = ReaderRepository.save(new Reader("8C:4F:00:3D:13:C8", "Reader1", coordinate1));
        final var reader2 = ReaderRepository.save(new Reader("01-B1-D1-64-C3-27", "Reader2", coordinate2));
        @SuppressWarnings("unused")
        final var reader3 = ReaderRepository.save(new Reader("02-B2-D2-65-C4-28", "Reader3", coordinate3));
        @SuppressWarnings("unused")
        final var reader4 = ReaderRepository.save(new Reader("03-B3-D3-66-C5-29", "Reader4", coordinate4));
        @SuppressWarnings("unused")
        final var reader5 = ReaderRepository.save(new Reader("04-B4-D4-67-C6-2A", "Reader5", coordinate5));

        @SuppressWarnings("unused")
        final var car1 = CarRepository.save(new Car("ABAU09I2"));

        final var route1 = RouteRepository
                .save(new Route(true, reader1, reader2, reader1, LocalDateTime.of(2025, 5, 1, 9, 15),
                        List.of("Step 1", "Step 2")));

        UserRepository.save(user);

        reader1.addStartingPoint(route1);
        reader2.addDestination(route1);
        reader1.addCurrentPoint(route1);

        RouteRepository.save(route1);

        ReaderRepository.save(reader1);
        ReaderRepository.save(reader2);
    }
}