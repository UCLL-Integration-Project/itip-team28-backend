package team28.service;
import team28.backend.exceptions.ServiceException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import team28.backend.controller.dto.AuthenticationResponse;
import team28.backend.controller.dto.UserInput;
import team28.backend.model.Role;
import team28.backend.model.User;
import team28.backend.repository.UserRepository;
import team28.backend.service.JwtService;
import team28.backend.service.UserDetailsImpl;
import team28.backend.service.UserService;
import org.springframework.security.core.Authentication;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
	@Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private Authentication authentication;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private User user;

    @Mock
    private UserDetailsImpl userDetails;

    @BeforeEach
    void setUp(){
        user = new User("John", "Doe", "johndoe", "johnDoe@email.com", "password123", Role.USER);
        user.SetId(1L);
    }

    @Test 
    void getAllUsers_success() {
        List<User> users = List.of(user);
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.GetAllUsers();

        assertEquals(1, result.size());
        assertEquals("johndoe", result.get(0).GetUsername());
        verify(userRepository, times(1)).findAll();
    }


    @Test 
    void createUser_succes() {
        when(userRepository.existsByUsername("johndoe")).thenReturn(false);
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.CreateUser(user);

        assertNotNull(result);
        assertEquals("johndoe", result.GetUsername());
        verify(userRepository, times(1)).existsByUsername("johndoe");
        verify(userRepository, times(1)).save(user);
    }

    @Test 
    void createUser_userAlreadyExists_throwsException(){
        when(userRepository.existsByUsername("johndoe")).thenReturn(true);

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            userService.CreateUser(user);
        });
        assertEquals("User already exists", exception.getMessage());
        verify(userRepository, times(1)).existsByUsername("johndoe");
        verify(userRepository, never()).save(user);
    }

    @Test 
    void updateUser_success() {
        when(userRepository.existsById(1L)).thenReturn(true);
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.UpdateUser(1L, user);

        assertNotNull(result);
        assertEquals("johndoe", result.GetUsername());
        verify(userRepository, times(1)).existsById(1L);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateUser_userNotFound_throwsException() {
        when(userRepository.existsById(1L)).thenReturn(false);

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            userService.UpdateUser(1L, user);
        });

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).existsById(1L);
        verify(userRepository, never()).save(user);
    }

    @Test
    void deleteUser_success() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.DeleteUser(1L);

        verify(userRepository, times(1)).existsById(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteUser_userNotFound_throwsException() {
        when(userRepository.existsById(1L)).thenReturn(false);

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            userService.DeleteUser(1L);
        });

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).existsById(1L);
        verify(userRepository, never()).deleteById(1L);
    }

    @Test 
    void login_success() {
        String username = "johndoe";
        String password = "password";
        String token = "jwtToken";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.user()).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn(token);

        AuthenticationResponse response = userService.Login(username, password);

        assertEquals("Authentication successful.", response.message());
        assertEquals(token, response.token());
        assertEquals("johndoe", response.username());
        assertEquals(user.GetRole(), response.role());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, times(1)).generateToken(user);
    }

    @Test
    void signup_success() {
        UserInput userInput = new UserInput("john", "doe", "johndoe", "john.doe@example.com", "password", Role.USER);
        when(userRepository.existsByUsername("johndoe")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.Signup(userInput);

        assertNotNull(result);
        assertEquals("johndoe", result.GetUsername());
        verify(userRepository, times(1)).existsByUsername("johndoe");
        verify(passwordEncoder, times(1)).encode("password");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test 
    void signup_usernameInUse_throwsException() {
        UserInput userInput = new UserInput("john", "doe", "johndoe", "john.doe@example.com", "password", Role.USER);
        when(userRepository.existsByUsername("johndoe")).thenReturn(true);

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            userService.Signup(userInput);
        });

        assertEquals("Username is already in use.", exception.getMessage());
        verify(userRepository, times(1)).existsByUsername("johndoe");
        verify(passwordEncoder, never()).encode("password");
        verify(userRepository, never()).save(any(User.class));
    }
}   
