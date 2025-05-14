package team28.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import team28.backend.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
}
