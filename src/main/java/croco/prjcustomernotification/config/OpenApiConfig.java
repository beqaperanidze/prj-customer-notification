package croco.prjcustomernotification.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Customer Notification Address Facade API",
                version = "1.0",
                description = "API documentation for the Customer Notification Address Facade System. " +
                        "Manages customer contact information, preferences, and notification tracking."
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Local Development Server"),
        },
        security = @SecurityRequirement(name = "BearerAuth")
)
@SecurityScheme(
        name = "BearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "JWT authentication using a Bearer token. To get a token, use the /api/auth/login endpoint."
)
public class OpenApiConfig {
}