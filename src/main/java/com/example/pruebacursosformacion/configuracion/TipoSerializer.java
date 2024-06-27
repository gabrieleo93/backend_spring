package com.example.pruebacursosformacion.configuracion;

import com.example.pruebacursosformacion.entidades.Tipo;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class TipoSerializer extends JsonSerializer<Tipo> {

    @Override
    public void serialize(Tipo value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(value.name());
    }

}
