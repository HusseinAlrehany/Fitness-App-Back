package com.coding.fitness.tokens.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "alrehany",
                        email = "huss@contact.com",
                        url = "http://fitness.com"
                ),
                description = "OpenApi Documentation for fitness app",
                title = "Api Documentation- fitness app ",
                version = "1.0",
                license = @License (
                            name = "Licenece name",
                            url = "http://licence"
                ),
                termsOfService = "Terms Of Service"
        ),
        servers = {
                @Server(
                        description = "Local Env",
                        url = "http://localhost:8080"
                ),
                @Server(
                        description = "Prod Env",
                        url = "http://localhost:8080"
                )
        },
        security = {
                //can be used at controller level
                //for jwt authentication
                @SecurityRequirement(
                        name = "bearerAuth"
                )
        }
)
//this annotation for using bearer token in swagger
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT auth description",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        //inject token to header
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
