package com.example.pruebacursosformacion.entidades;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING) // Para poder convertirlo a json
public enum Tipo {
    BACKEND,
    FRONTEND,
    BBDD,
    ANALISIS
}
