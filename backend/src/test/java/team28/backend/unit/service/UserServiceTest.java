package team28.backend.unit.service;

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
    private UserRepository UserRepository;

    @InjectMocks
    private UserService UserService;

    @Mock
    private AuthenticationManager AuthenticationManager;

    @Mock
    private Authentication authentication;

    @Mock
    private JwtService JwtService;

    @Mock
    private PasswordEncoder PasswordEncoder;

    @Mock
    private User user;

    @Mock
    private UserDetailsImpl UserDetails;

    @BeforeEach
    void setUp() {
        user = new User("johndoe", "johnDoe@email.com", "password123", Role.USER);
        user.setId(1L);
    }

    @Test
    public void givenAllUser_whenAllUsersIsRequested_thenGiveListOfAllUsers() {
        List<User> users = List.of(user);
        when(UserRepository.findAll()).thenReturn(users);

        List<User> result = UserService.GetAllUsers();

        assertEquals(1, result.size());
        assertEquals("johndoe", result.get(0).getUsername());
        verify(UserRepository, times(1)).findAll();
    }

    // @Test
    // public void givenNewUserInfo_whenUpdatingUser_thenUpdateTheUserInfo() {
    // when(UserRepository.existsById(1L)).thenReturn(true);
    // when(UserRepository.save(any(User.class))).thenReturn(user);

    // User result = UserService.UpdateUser(1L, user);

    // assertNotNull(result);
    // assertEquals("johndoe", result.getUsername());
    // verify(UserRepository, times(1)).existsById(1L);
    // verify(UserRepository, times(1)).save(any(User.class));
    // }

    // @Test
    // public void
    // givenNewUserInfoForUserThatDoesntExists_whenUpdatingUser_thenThrowException()
    // {
    // when(UserRepository.existsById(1L)).thenReturn(false);

    // UserException exception = assertThrows(UserException.class, () -> {
    // UserService.UpdateUser(1L, user);
    // });

    // assertEquals("User not found", exception.getMessage());
    // verify(UserRepository, times(1)).existsById(1L);
    // verify(UserRepository, never()).save(user);
    // }

    @Test
    public void givenUserId_whenDeletingUser_thenDeleteThatUser() {
        when(UserRepository.existsById(1L)).thenReturn(true);

        UserService.DeleteUser(1L);

        verify(UserRepository, times(1)).existsById(1L);
        verify(UserRepository, times(1)).deleteById(1L);
    }

    @Test
    public void givenUserIdTHatDoesntExists_whenDeletingUser_thenThrowException() {
        when(UserRepository.existsById(1L)).thenReturn(false);

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            UserService.DeleteUser(1L);
        });

        assertEquals("User not found", exception.getMessage());
        verify(UserRepository, times(1)).existsById(1L);
        verify(UserRepository, never()).deleteById(1L);
    }

    @Test
    public void givenUserCredintials_whenUserWantsToLogin_thenUserIsLoggedIn() {
        String username = "johndoe";
        String password = "password";
        String token = "jwtToken";

        when(AuthenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(UserDetails);
        when(UserDetails.user()).thenReturn(user);
        when(JwtService.generateToken(user)).thenReturn(token);

        AuthenticationResponse response = UserService.Login(username, password);

        assertEquals("Authentication successful.", response.message());
        assertEquals(token, response.token());
        assertEquals("johndoe", response.username());
        assertEquals(user.getRole(), response.role());
        verify(AuthenticationManager,
                times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(JwtService, times(1)).generateToken(user);
    }

    @Test
    public void givenUserInfo_whenUserIsRegistrating_thenUserIsAddedToDatabase() {
        UserInput UserInput = new UserInput("johndoe", "john.doe@example.com", "password", Role.USER);
        when(UserRepository.existsByUsername("johndoe")).thenReturn(false);
        when(PasswordEncoder.encode("password")).thenReturn("hashedPassword");
        when(UserRepository.save(any(User.class))).thenReturn(user);

        User result = UserService.Signup(UserInput);

        assertNotNull(result);
        assertEquals("johndoe", result.getUsername());
        verify(UserRepository, times(1)).existsByUsername("johndoe");
        verify(PasswordEncoder, times(1)).encode("password");
        verify(UserRepository, times(1)).save(any(User.class));
    }

    @Test
    public void givenExistingUserInfo_whenUserIsRegistrating_thenThrowException() {
        UserInput UserInput = new UserInput("johndoe", "john.doe@example.com", "password", Role.USER);
        when(UserRepository.existsByUsername("johndoe")).thenReturn(true);

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            UserService.Signup(UserInput);
        });

        assertEquals("Username is already in use", exception.getMessage());
        verify(UserRepository, times(1)).existsByUsername("johndoe");
        verify(PasswordEncoder, never()).encode("password");
        verify(UserRepository, never()).save(any(User.class));
    }
}
