package com.example.pruebacursosformacion.solicitudes;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmpleadoResponse {
    private long idEmpleado;
    private String nombreEmpleado;
    private String apellidosEmpleado;
    private String emailEmpleado;
    private String rol;
}
