package org.example.backend.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "API specification for the Annotation Tool", version = "v1.0.0",
    description = "This is an Open API specification for the Annotation Tool we have been developing throughout the" +
    "Software Project course."))
public class OpenAPIConfig {
}
