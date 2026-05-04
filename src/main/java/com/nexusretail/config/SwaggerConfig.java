package com.nexusretail.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Nexus Retail Backend API")
                        .version("1.0.0")
                        .description("API documentation for Nexus Retail Backend application")
                        .contact(new Contact()
                                .name("Nexus Retail Team")
                                .email("support@nexusretail.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://www.apache.org/licenses/LICENSE-2.0")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Development server"),
                        new Server().url("https://api.nexusretail.com").description("Production server")
                ));
    }
}
