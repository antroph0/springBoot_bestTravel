package com.debbugeando_ideas.best_travel.configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Best Travel API",
                version = "1.0.0",
                description = "Documentation for endpoints that api Best travel")
)
public class OpenApiConfig {
}
