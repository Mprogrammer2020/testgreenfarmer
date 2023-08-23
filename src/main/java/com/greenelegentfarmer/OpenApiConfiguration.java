package com.greenelegentfarmer;

import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@Configuration
@SecurityScheme(type = SecuritySchemeType.HTTP, name = "bearer_token",scheme = "bearer" ,bearerFormat = "JWT", in = SecuritySchemeIn.HEADER)
@OpenAPIDefinition(info = @Info(title = "Swap and style API documentation", version = "1.0.0"), security = { @SecurityRequirement(name = "bearer_token") })
public class OpenApiConfiguration {

}
