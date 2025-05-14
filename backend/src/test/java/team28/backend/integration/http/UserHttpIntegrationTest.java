package team28.backend.integration.http;

import static org.mockito.ArgumentMatchers.any;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.fasterxml.jackson.databind.ObjectMapper;

import team28.backend.controller.UserController;
import team28.backend.model.Role;
import team28.backend.model.User;
import team28.backend.service.JwtService;
import team28.backend.service.UserService;

@WebMvcTest(UserController.class)
public class UserHttpIntegrationTest {

    @Autowired
    private MockMvc MockMvc;

    @Autowired
    private ObjectMapper ObjectMapper;

    @MockBean
    private UserService UserService;

    @MockBean
    private Authentication Authentication;

    @MockBean
    private JwtService JwtService;

    private User user;

    String token;

    @BeforeEach
    public void setUp() {
        user = new User("Test", "kees.test@example.com", "testkees123", Role.MANAGER);
        user.setId(1L);
    }

    @Test
    public void givenManagerUser_whenManagerIsRequestingUsers_thenReturnAllUsers() throws Exception {

        Mockito.when(JwtService.generateToken("test-manager", Role.MANAGER)).thenReturn("test-manager");
        Mockito.when(Authentication.getPrincipal()).thenReturn(user);
        Mockito.when(Authentication.isAuthenticated()).thenReturn(true);
        SecurityContextHolder.getContext().setAuthentication(Authentication);

        Mockito.when(UserService.GetAllUsers()).thenReturn(List.of(user));

        MockMvc.perform(MockMvcRequestBuilders.get("/users")
                .header("Authorization", "Bearer test-manager"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L));
    }

}
