package com.finance.dashboard.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;
import io.swagger.v3.oas.models.info.Contact;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customConfig() {

        return new OpenAPI().info(
                        new Info().title("Finance Data Processing Application APIs")
                                .description("By S Nagarjuna")
                                .version("1.0.0")
                                .contact(new Contact()
                                    .name("S Nagarjuna")
                                    .email("srinivasnagarjuna04@gmail.com")
                                )
                )
                .tags(List.of(
                        new Tag().name("User APIs").description("Register and Login Users."),
                        new Tag().name("Financial Record APIs").description("Add, Delete, Update, View-All, View-by-filters Financial Records."),
                        new Tag().name("Admin APIs").description("Allows an Admin to create an Admin or Analyst. Update the role of the user and the user's profile status."),
                        new Tag().name("Dashboard APIs").description("Get summaries of the financial records.")
                        ))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components().addSecuritySchemes(
                        "bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)
                                .name("Authorization")
                ));
    }
}
