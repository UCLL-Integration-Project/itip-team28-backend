package team28.backend.service;

import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import org.springframework.security.authentication.AuthenticationManager;

import team28.backend.model.Role;
import team28.backend.model.User;
import team28.backend.repository.UserRepository;
import team28.backend.controller.dto.AuthenticationResponse;
import team28.backend.controller.dto.UserInput;
import team28.backend.exceptions.ServiceException;

import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserService {
    private final UserRepository UserRepository;
    private final AuthenticationManager AuthenticationManager;
    private final JwtService JwtService;
    private final PasswordEncoder PasswordEncoder;

    public UserService(UserRepository UserRepository, AuthenticationManager AuthenticationManager,
            JwtService JwtService, PasswordEncoder PasswordEncoder) {
        this.JwtService = JwtService;
        this.AuthenticationManager = AuthenticationManager;
        this.UserRepository = UserRepository;
        this.PasswordEncoder = PasswordEncoder;
    }

    public List<User> GetAllUsers() {
        return UserRepository.findAll();
    }

    public void DeleteUser(Long id) {
        boolean ExistingUser = UserRepository.existsById(id);

        if (!ExistingUser) {
            throw new ServiceException("User not found");
        }

        UserRepository.deleteById(id);
    }

    public AuthenticationResponse Login(String username, String password) {
        final var UsernamePasswordAuthentication = new UsernamePasswordAuthenticationToken(username, password);
        final var authentication = AuthenticationManager.authenticate(UsernamePasswordAuthentication);
        final var user = ((UserDetailsImpl) authentication.getPrincipal()).user();
        final var token = JwtService.generateToken(user);

        return new AuthenticationResponse(
                "Authentication successful.",
                token,
                user.getUsername(),
                user.getRole(),
                user.getId());
    }

    public User Signup(UserInput UserInput) {
        boolean exists = UserRepository.existsByUsername(UserInput.username());
        if (exists) {
            throw new ServiceException("Username is already in use");
        }

        final var HashedPassword = PasswordEncoder.encode(UserInput.password());
        final var user = new User(
                UserInput.username(),
                UserInput.email(),
                HashedPassword,
                Role.USER);

        return UserRepository.save(user);
    }

    public User getCurrentUser(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return UserRepository.findByUsername(username)
                .orElseThrow(() -> new ServiceException("User not found"));
    }

    @Transactional
    public void changePassword(String currentPassword, String newPassword, String confirmPassword) {
         if (currentPassword == null || currentPassword.trim().isEmpty()) {
            throw new ServiceException("Current password cannot be empty");
        }
        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new ServiceException("New password cannot be empty");
        }
        if (confirmPassword == null || confirmPassword.trim().isEmpty()) {
            throw new ServiceException("Confirm password cannot be empty");
        }
        if (!newPassword.equals(confirmPassword)) {
            throw new ServiceException("New password and confirmation do not match");
        }

        User user = getCurrentUser();

        if (currentPassword != null && !currentPassword.isEmpty()){
            if (!PasswordEncoder.matches(currentPassword, user.getPassword())) {
                throw new ServiceException("Current password is incorrect");
            }
        }

        user.setPassword(PasswordEncoder.encode(newPassword));
        UserRepository.save(user);
    }
}
