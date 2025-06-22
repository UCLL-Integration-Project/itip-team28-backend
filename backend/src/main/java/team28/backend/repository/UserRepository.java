package team28.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import team28.backend.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);
}
