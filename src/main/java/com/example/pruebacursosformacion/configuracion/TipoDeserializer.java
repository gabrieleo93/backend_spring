package com.example.pruebacursosformacion.configuracion;

import com.example.pruebacursosformacion.entidades.Tipo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class TipoDeserializer extends JsonDeserializer<Tipo> {
    @Override
    public Tipo deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return Tipo.valueOf(p.getText().toUpperCase());
    }

}
