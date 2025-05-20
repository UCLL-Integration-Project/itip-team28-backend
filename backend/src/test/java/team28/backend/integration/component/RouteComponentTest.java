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
public class RouteComponentTest {
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
    public void givenRoutes_whenUserIsRequestingRoutes_thenShowAllTags() {
        WebTestClient
                .get()
                .uri("/routes")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void givenRoutes_whenUserIsNotLoggedIn_thenThrowException() {
        WebTestClient
                .get()
                .uri("/routes")
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus()
                .isUnauthorized();
    }

    @Test
    public void givenInvalidRouteInfo_whenUserIsNotLoggedIn_thenThrowException() {
        WebTestClient
                .post()
                .uri("/readers")
                .header("Content-Type", "application/json")
                .bodyValue("""
                            {
                                "status": "false",
                                "startingPointReaderId": "1",
                                "destinationReaderId": "2",
                                "currentPointReaderId": "1",
                                "timestamp": "2025-05-05T10:00"
                                "instructions": []
                            }
                        """)
                .exchange()
                .expectStatus()
                .isEqualTo(401);
    }
}
