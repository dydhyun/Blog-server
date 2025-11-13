package com.yh.blogserver.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    private static final String API_NAME = "Blog Server API";
    private static final String API_VERSION = "1.0";
    private static final String API_DESCRIPTION = "블로그 프로젝트 api 명세서";

    @Bean
    public OpenAPI customOpenApi(){
        return new OpenAPI()
                .info(new Info().title(API_NAME)
                        .description(API_DESCRIPTION)
                        .version(API_VERSION))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}
