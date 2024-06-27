package com.example.pruebacursosformacion.configuracion;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;


@Configuration
public class WebMvcConfigurer implements org.springframework.web.servlet.config.annotation.WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:4200/") // Cambia esto a la IP del ordenador del frontend
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

}
