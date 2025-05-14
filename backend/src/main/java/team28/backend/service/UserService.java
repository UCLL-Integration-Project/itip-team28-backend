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
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    
    public UserService(UserRepository UserRepository, AuthenticationManager authenticationManager, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.UserRepository = UserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> GetAllUsers() {
        return UserRepository.findAll();
    }

    public User CreateUser(User user) {
        boolean ExistingUser = UserRepository.existsByUsername(user.GetUsername());

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

        User UpdatedUser = new User(user.GetName(), user.GetLastname(), user.GetUsername(), user.GetEmail(), user.GetPassword(), user.GetRole());
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
        final var usernamePasswordAuthentication = new UsernamePasswordAuthenticationToken(username, password);
        final var authentication = authenticationManager.authenticate(usernamePasswordAuthentication);
        final var user = ((UserDetailsImpl) authentication.getPrincipal()).user();
        final var token = jwtService.generateToken(user);
        return new AuthenticationResponse(
                "Authentication successful.",
                token,
                user.GetUsername(),
                user.GetFullName(),
                user.GetRole(),
                user.GetId()
        );
    }

    public User Signup(UserInput userInput) {
        if (UserRepository.existsByUsername(userInput.username())) {
            throw new ServiceException("Username is already in use.");
        }

        final var hashedPassword = passwordEncoder.encode(userInput.password());
        final var user = new User(
                userInput.username(),
                userInput.name(),
                userInput.lastname(),
                userInput.email(),
                hashedPassword,
                userInput.role()
        );

        return UserRepository.save(user);
    }
}
