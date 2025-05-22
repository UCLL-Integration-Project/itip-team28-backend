package team28.backend.repository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

import team28.backend.model.Role;

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

    }
}