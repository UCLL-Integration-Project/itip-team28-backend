package team28.backend.service;

import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import team28.backend.model.Role;
import team28.backend.model.User;
import team28.backend.config.JwtProperties;

import java.time.Instant;

@Service
public class JwtService {
    private final JwtProperties JwtProperties;
    private final JwtEncoder JwtEncoder;

    public JwtService(JwtProperties JwtProperties,
            JwtEncoder JwtEncoder) {
        this.JwtProperties = JwtProperties;
        this.JwtEncoder = JwtEncoder;
    }

    public String generateToken(String username, Role role) {
        final var now = Instant.now();
        final var ExpiresAt = now.plus(JwtProperties.token().lifetime());
        final var header = JwsHeader.with(MacAlgorithm.HS256).build();
        final var claims = JwtClaimsSet.builder()
                .issuer(JwtProperties.token().issuer())
                .issuedAt(now)
                .expiresAt(ExpiresAt)
                .subject(username)
                .claim("scope", role.toGrantedAuthority().getAuthority())
                .build();
        return JwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }

    public String generateToken(User user) {
        return generateToken(user.GetUsername(), user.GetRole());
    }
}
