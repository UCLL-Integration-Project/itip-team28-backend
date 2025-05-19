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
public class ReaderComponentTest {

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
    public void givenReaders_whenUserIsRequestingReaders_thenShowAllTags() {
        WebTestClient
                .get()
                .uri("/readers")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void givenReader_whenUserIsNotLoggedIn_thenThrowException() {
        WebTestClient
                .get()
                .uri("/readers")
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus()
                .isUnauthorized();
    }
}
