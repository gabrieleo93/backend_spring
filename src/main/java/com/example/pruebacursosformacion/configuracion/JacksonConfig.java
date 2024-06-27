package com.example.pruebacursosformacion.configuracion;

import com.example.pruebacursosformacion.entidades.Tipo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Tipo.class, new TipoDeserializer());
        module.addSerializer(Tipo.class, new TipoSerializer());
        mapper.registerModule(module);
        return mapper;
    }

}
