package com.kin1st.teddybearshopping.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }

    @Bean
    public OpenAPI teddyBearShopOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, createAPIKeyScheme()))
                .info(new Info()
                        .title("Teddy Bear Shop API")
                        .description("""
                                <h2>API Documentation for Teddy Bear Shop</h2>
                                <p>This is the API documentation for the Teddy Bear Shop e-commerce platform.</p>
                                <h3>Authentication</h3>
                                <p>Most endpoints require authentication. Use the <strong>/api/auth/login</strong> endpoint to get a JWT token.</p>
                                <p>Add the token to the <strong>Authorization</strong> header as: <code>Bearer your_token_here</code></p>
                                """)
                        .version("1.0")
                        .contact(new Contact()
                                .name("Teddy Bear Shop Support")
                                .email("support@teddybearshop.com")
                                .url("https://teddybearshop.com/support"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")));
    }
}
