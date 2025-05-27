package team28.backend.repository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

import team28.backend.model.Role;
import team28.backend.model.User;
import team28.backend.service.StockService;

@Component
public class DbInitializer {
    private final UserRepository UserRepository;
    private final PasswordEncoder PasswordEncoder;
    private final ReaderRepository ReaderRepository;
    private final CarRepository CarRepository;
    private final CoordinateRepository CoordinateRepository;
    private final RouteRepository RouteRepository;
    private final ItemRepository ItemRepository;
    private final StockRepository StockRepository;
    private final StockService StockService;

    public DbInitializer(PasswordEncoder PasswordEncoder, UserRepository UserRepository,
            ReaderRepository ReaderRepository, CarRepository CarRepository,
            CoordinateRepository CoordinateRepository,
            RouteRepository RouteRepository,
            ItemRepository ItemRepository,
            StockRepository StockRepository,
            StockService StockService) {
        this.ItemRepository = ItemRepository;
        this.StockRepository = StockRepository;
        this.PasswordEncoder = PasswordEncoder;
        this.UserRepository = UserRepository;
        this.ReaderRepository = ReaderRepository;
        this.CarRepository = CarRepository;
        this.CoordinateRepository = CoordinateRepository;
        this.RouteRepository = RouteRepository;
        this.StockService = StockService;
    }

    public void clearAll() {
        RouteRepository.deleteAll();
        StockRepository.deleteAll();
        ReaderRepository.deleteAll();
        CarRepository.deleteAll();
        UserRepository.deleteAll();
        ItemRepository.deleteAll();
        CoordinateRepository.deleteAll();
    }

    @PostConstruct
    public void init() {
        @SuppressWarnings("unused")
        final var user = UserRepository
                .save(new User("test", "test@example.com", PasswordEncoder.encode("test"), Role.USER));
        @SuppressWarnings("unused")
        final var user2 = UserRepository
                .save(new User("test2", "test@example.com", PasswordEncoder.encode("test"), Role.MANAGER));

    }

}