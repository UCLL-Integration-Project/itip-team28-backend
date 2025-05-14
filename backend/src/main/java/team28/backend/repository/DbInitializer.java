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

    public DbInitializer(PasswordEncoder PasswordEncoder, UserRepository UserRepository) {
        this.PasswordEncoder = PasswordEncoder;
        this.UserRepository = UserRepository;
    }

    public void clearAll() {
        UserRepository.deleteAll();
    }

    @PostConstruct
    public void init() {
        clearAll();

        final var user = UserRepository
                .save(new User("test", "test@example.com", PasswordEncoder.encode("test"), Role.USER));
        final var user2 = UserRepository
                .save(new User("test2", "test@example.com", PasswordEncoder.encode("test"), Role.MANAGER));
    }
}