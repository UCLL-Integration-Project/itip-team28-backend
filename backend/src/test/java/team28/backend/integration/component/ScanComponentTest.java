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
public class ScanComponentTest {

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
    public void givenScans_whenUserIsRequestingScans_thenShowAllUsers() {
        WebTestClient
                .get()
                .uri("/scans")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void givenScans_whenUserIsNotLoggedIn_thenThrowException() {
        WebTestClient
                .get()
                .uri("/scans")
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus()
                .isUnauthorized();
    }
}
