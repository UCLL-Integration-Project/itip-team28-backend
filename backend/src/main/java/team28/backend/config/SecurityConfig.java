package team28.backend.config;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.h2.H2ConsoleProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@Configuration
@EnableConfigurationProperties({ CorsProperties.class, JwtProperties.class, H2ConsoleProperties.class })
@EnableMethodSecurity
public class SecurityConfig {
    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * This sets up a separate security filter chain for the H2 console
     */
    @Bean
    @Order(0)
    @ConditionalOnProperty(prefix = "spring.h2.console", name = "enabled", havingValue = "true")
    public SecurityFilterChain h2SecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher(toH2Console())
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frameOptionsConfig -> frameOptionsConfig.disable()))
                .authorizeHttpRequests(
                        AuthorizeRequests -> AuthorizeRequests.anyRequest().permitAll())
                .build();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
            CorsProperties CorsProperties) throws Exception {
        return http
                .authorizeHttpRequests(
                        AuthorizeRequests -> AuthorizeRequests
                                // Allow all access to health check
                                .requestMatchers("/status").permitAll()
                                // Allow all access to error endpoints
                                .requestMatchers("/error/**").permitAll()
                                // Allow all to login and signup
                                .requestMatchers("/users/login", "/users/signup").permitAll()
                                .requestMatchers("/tags/data").permitAll()
                                .requestMatchers("/readers/ip").permitAll()
                                .requestMatchers("/readers/config").permitAll()

                                // Allow OpenAPI access
                                .requestMatchers("/v3/api-docs/**").permitAll()
                                // Allow Swagger UI
                                .requestMatchers("/swagger-ui/**", "/swagger-ui.html").permitAll()
                                .anyRequest().authenticated())
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .oauth2ResourceServer(ResourceServer -> ResourceServer.jwt(Customizer.withDefaults()))
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(CorsProperties CorsProperties) {
        final var configuration = new CorsConfiguration();
        final var AllowedOrigins = CorsProperties.AllowedOrigins().stream().map(URL::toString).toList();
        configuration.setAllowedOrigins(AllowedOrigins);
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        final var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder PasswordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public SecretKey secretKey(JwtProperties JwtProperties) throws NoSuchAlgorithmException {
        final var SecretKeyProperty = JwtProperties.secretKey();
        if (SecretKeyProperty == null || SecretKeyProperty.isEmpty()) {
            log.warn("No secret key configured, generating a random key");
            final var SecretKeyGenerator = KeyGenerator.getInstance("AES");
            return SecretKeyGenerator.generateKey();
        } else {
            final var bytes = JwtProperties.secretKey().getBytes(StandardCharsets.UTF_8);
            return new SecretKeySpec(bytes, "AES");
        }
    }

    @Bean
    public JwtDecoder jwtDecoder(SecretKey secretKey) {
        return NimbusJwtDecoder.withSecretKey(secretKey).macAlgorithm(MacAlgorithm.HS256).build();
    }

    @Bean
    public JwtEncoder JwtEncoder(SecretKey secretKey) {
        final JWK jwk = new OctetSequenceKey.Builder(secretKey)
                .algorithm(JWSAlgorithm.HS256)
                .build();
        final var jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    public AuthenticationManager AuthenticationManager(AuthenticationConfiguration AuthenticationConfiguration)
            throws Exception {
        return AuthenticationConfiguration.getAuthenticationManager();
    }
}