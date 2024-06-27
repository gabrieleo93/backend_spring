package com.example.pruebacursosformacion.solicitudes;

public class AutenticacionResponse {
    private final String jwt;
    private  final long idEmpleadoRespuesta;
    private final String nombreEmpleadoRespuesta;
    private final String rolRespuesta;

    public AutenticacionResponse(String jwt, long idEmpleadoRespuesta, String nombreEmpleadoRespuesta, String rolRespuesta) {
        this.jwt = jwt;
        this.idEmpleadoRespuesta = idEmpleadoRespuesta;
        this.nombreEmpleadoRespuesta = nombreEmpleadoRespuesta;
        this.rolRespuesta = rolRespuesta;
    }

    /*public AutenticacionResponse(String jwt, String nombreEmpleado, String rol) {
        this.jwt = jwt;
        this.nombreEmpleadoRespuesta = nombreEmpleado;
        this.rolRespuesta = rol;
    }*/

    public long getIdEmpleadoRespuesta() {
        return idEmpleadoRespuesta;
    }

    public String getJwt() {
        return jwt;
    }

    public String getNombreEmpleadoRespuesta() {
        return nombreEmpleadoRespuesta;
    }

    public String getRolRespuesta() {
        return rolRespuesta;
    }
}
