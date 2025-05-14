package team28.backend.repository;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import team28.backend.model.Role;
import team28.backend.model.User;

@Component
public class DbInitializer {

    private final UserRepository UserRepository;

    public DbInitializer(UserRepository UserRepository) {
        this.UserRepository = UserRepository;
    }

    public void clearAll() {
        UserRepository.deleteAll();
    }

    @PostConstruct
    public void init() {
        clearAll();

        final var user = UserRepository
                .save(new User("test", "test@example.com", "test", Role.USER));
    }
}