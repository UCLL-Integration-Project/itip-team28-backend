package team28.backend.integration.component;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;

import team28.backend.config.SecurityConfig;
import team28.backend.model.Role;
import team28.backend.repository.DbInitializer;
import team28.backend.service.JwtService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Import(SecurityConfig.class)
public class UserComponentTest {

    @Autowired
    private WebTestClient WebTestClient;

    @Autowired
    private DbInitializer DbInitializer;

    @Autowired
    private JwtService JwtService;

    String token;
    String TokenManager;

    @BeforeEach
    public void setup() {

        DbInitializer.init();
        token = JwtService.generateToken("test-manager", Role.USER);
        TokenManager = JwtService.generateToken("klaas", Role.MANAGER);
    }

    @Test
    public void givenUsers_whenUserIsRequestingUsers_thenShowAllUsers() {
        WebTestClient
                .get()
                .uri("/users")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void givenUsers_whenUserIsNotLoggedIn_thenThrowException() {
        WebTestClient
                .get()
                .uri("/users")
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus()
                .isUnauthorized();
    }

    @Test
    public void givenUsersCredintials_whenUserIsLoggingIn_thenUserIsLoggedIn() {
        WebTestClient
                .post()
                .uri("/users/login")
                .header("Content-Type", "application/json")
                .bodyValue("""
                            {
                                "username": "test",
                                "password": "test"
                            }
                        """)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void givenInvalidUsersCredintials_whenUserIsLoggingIn_thenThrowException() {
        WebTestClient
                .post()
                .uri("/users/login")
                .header("Content-Type", "application/json")
                .bodyValue("""
                            {
                                "username": "tester",
                                "password": "tester"
                            }
                        """)
                .exchange()
                .expectStatus()
                .isEqualTo(400);
    }

    @Test
    public void givenUsersInfo_whenUserIsSigningUp_thenUserIsSignedUp() {
        WebTestClient
                .post()
                .uri("/users/signup")
                .header("Content-Type", "application/json")
                .bodyValue("""
                        {
                            "username": "test",
                            "email": "test@example.com",
                            "password": "hahahaha",
                            "role": 1
                        }
                        """)
                .exchange()
                .expectStatus()
                .isEqualTo(400);
    }
}
