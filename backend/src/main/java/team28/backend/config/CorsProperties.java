package team28.backend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.net.URL;
import java.util.List;

@ConfigurationProperties(prefix = "cors")
public record CorsProperties(
        @DefaultValue({
            "http://localhost:8080",
            "https://itip-team28-frontend-itip-project28.apps.okd.ucll.cloud"
        }) List<URL> AllowedOrigins) {
}
