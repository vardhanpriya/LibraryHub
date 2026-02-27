package com.libraryhub.api.security;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info =   @Info (
                title = "LibraryHub",
                description = "Connecting Libraries Across Cities and States",
                contact = @Contact(
                            name = "Priya Vardhan",
                        email="suppost@technology.com"
                ),
                version ="1.0",
                summary = ""
        ),
        security = {
                @SecurityRequirement(
                        name = "bearerAuth"
                        )
        }
)
@SecurityScheme(
        type = SecuritySchemeType.HTTP,
        name = "bearerAuth",
        scheme = "bearer",
        bearerFormat = "JWT"

)

public class OpenApiDocConfig {



}
