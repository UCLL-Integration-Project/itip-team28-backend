package team28.backend.service;

import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.AuthenticationManager;

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

    public User CreateUser(User user) {
        boolean ExistingUser = UserRepository.existsByUsername(user.getUsername());

        if (ExistingUser) {
            throw new ServiceException("User already exists");
        }

        return UserRepository.save(user);
    }

    public User UpdateUser(Long id, User user) {
        boolean ExistingUser = UserRepository.existsById(id);

        if (!ExistingUser) {
            throw new ServiceException("User not found");
        }

        User UpdatedUser = new User(user.getUsername(), user.getEmail(),
                user.getPassword(), user.getRole());
        return UserRepository.save(UpdatedUser);
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
        if (UserRepository.existsByUsername(UserInput.username())) {
            throw new ServiceException("Username is already in use.");
        }

        final var HashedPassword = PasswordEncoder.encode(UserInput.password());
        final var user = new User(
                UserInput.username(),
                UserInput.email(),
                HashedPassword,
                UserInput.role());

        return UserRepository.save(user);
    }
}
