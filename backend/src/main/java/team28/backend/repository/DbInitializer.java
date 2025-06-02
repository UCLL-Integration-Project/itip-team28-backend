package team28.backend.repository;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import team28.backend.model.Car;
import team28.backend.model.Coordinate;
import team28.backend.model.Item;
import team28.backend.model.Reader;
import team28.backend.model.Role;
import team28.backend.model.Scan;
import team28.backend.model.Stock;
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
    private final GridRepository GridRepository;
    private final ScanRepository ScanRepository;

    public DbInitializer(PasswordEncoder PasswordEncoder, UserRepository UserRepository,
            ReaderRepository ReaderRepository, CarRepository CarRepository,
            CoordinateRepository CoordinateRepository,
            RouteRepository RouteRepository,
            ItemRepository ItemRepository,
            StockRepository StockRepository,
            StockService StockService, GridRepository GridRepository,ScanRepository ScanRepository) {
        this.ItemRepository = ItemRepository;
        this.StockRepository = StockRepository;
        this.PasswordEncoder = PasswordEncoder;
        this.UserRepository = UserRepository;
        this.ReaderRepository = ReaderRepository;
        this.CarRepository = CarRepository;
        this.CoordinateRepository = CoordinateRepository;
        this.RouteRepository = RouteRepository;
        this.GridRepository = GridRepository;
        this.ScanRepository = ScanRepository;
    }

    public void clearAll() {
        RouteRepository.deleteAll();
        StockRepository.deleteAll();
        ReaderRepository.deleteAll();
        CarRepository.deleteAll();
        UserRepository.deleteAll();
        ItemRepository.deleteAll();
        CoordinateRepository.deleteAll();
        GridRepository.deleteAll();
    }

    @PostConstruct
    public void init() {
        clearAll();

        // ➕ Users
        @SuppressWarnings("unused")
        final var user = UserRepository
                .save(new User("test", "test@example.com", PasswordEncoder.encode("test"), Role.USER));
        @SuppressWarnings("unused")
        final var user2 = UserRepository
                .save(new User("test2", "test@example.com", PasswordEncoder.encode("test"), Role.MANAGER));

        final var item = ItemRepository.save(new Item("Batterij AA"));
        final var item2 = ItemRepository.save(new Item("Monitors"));
        final var car = CarRepository.save(new Car("NONA142"));
        final var stock = StockRepository.save(new Stock(car, item, 50));
        final var coordinates= CoordinateRepository.save(new Coordinate(3, 1));
        final var reader = ReaderRepository.save(new Reader("00-B0-D0-63-C2-26", "Reader1", coordinates));
        final var stock2 = StockRepository.save(new Stock(reader, item2, 50));
        final var scan = ScanRepository.save(new Scan(car, reader, LocalDateTime.of(2025, 5, 1, 9, 15)));
        
    } 

}