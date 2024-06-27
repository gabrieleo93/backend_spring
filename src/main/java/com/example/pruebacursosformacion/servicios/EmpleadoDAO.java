package com.example.pruebacursosformacion.servicios;

import com.example.pruebacursosformacion.entidades.Curso;
import com.example.pruebacursosformacion.entidades.Empleado;
import com.example.pruebacursosformacion.repositorio.EmpleadoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class EmpleadoDAO implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(EmpleadoDAO.class);
    @Autowired
    private MessageSource messageSource;
    @Autowired
    EmpleadoRepositorio repositorioEmpleado;
    @Autowired
    private PasswordEncoder passwordEncoder;



    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.info("Entra en el loadUserByUsername");
        Empleado usuario = obtenerEmpleadoPorEmail(email);
        logger.info("El usuario es: "+usuario.getEmailEmpleado()+" con contraseña "+usuario.getPassword());
        if (usuario == null) {
            logger.info(messageSource.getMessage("usuario.NoEncontrado", null, LocaleContextHolder.getLocale()));
            throw new UsernameNotFoundException("Usuario no encontrado");
        }

        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(usuario.getRol()));

        return new User(usuario.getEmailEmpleado(), usuario.getPassword(), authorities);


    }

    public boolean insertarActualizaEmlpleado(Empleado empleado){
        try {
            empleado.setPassword(passwordEncoder.encode(empleado.getPassword()));
            repositorioEmpleado.save(empleado);
            logger.info(messageSource.getMessage("basedatos.empleado.guardar", new Object[]{empleado.getIdEmpleado()}, LocaleContextHolder.getLocale()));
            return true;
        } catch (Exception e) {
            logger.error(messageSource.getMessage("basedatos.empleado.errorGuardar", new Object[]{empleado.getIdEmpleado()}, LocaleContextHolder.getLocale()), empleado.getIdEmpleado());
            return false;
        }
    }

    public List<Empleado> obtenerTodosLosEmpleados(){
        logger.trace(messageSource.getMessage("basedatos.empleado.obtenerTodos", null, LocaleContextHolder.getLocale()));
        return repositorioEmpleado.findAll();
    }


    public Empleado obtenerEmpleadoPorId(long id){
        logger.trace(messageSource.getMessage("basedatos.empleado.obtenerPorId", new Object[]{id}, LocaleContextHolder.getLocale()));
        return repositorioEmpleado.getById(id);
    }


    public Empleado obtenerEmpleadoPorNombre(String nombre){
        logger.trace(messageSource.getMessage("basedatos.empleado.obtenerPorNombre", new Object[]{nombre}, LocaleContextHolder.getLocale()));
        return repositorioEmpleado.findEmpleadoByNombreEmpleado(nombre);
    }

    // METODO devuelve lista de empleados que ha realizado un curso en concreto
    public List<Empleado> obtenerEmpleadosPorCurso(Curso curso) {
        logger.info(messageSource.getMessage("basedatos.empleado.obtenerPorCurso", new Object[]{curso.getNombreCurso()}, LocaleContextHolder.getLocale()));
        return repositorioEmpleado.findByCurso(curso);
    }

    public void eliminarEmpleado(Empleado empleado) {
        repositorioEmpleado.delete(empleado);
        logger.info(messageSource.getMessage("basedatos.empleado.eliminar", new Object[]{empleado.getIdEmpleado()}, LocaleContextHolder.getLocale()));
    }

    public Empleado obtenerEmpleadoPorEmail(String email){
        logger.info("Buscando empleado por email: " + email);
        Empleado empleado = repositorioEmpleado.findEmpleadoByEmailEmpleado(email);
        if (empleado != null) {
            logger.info("Empleado encontrado: " + empleado.getEmailEmpleado());
        } else {
            logger.info("No se encontró un empleado con el email: " + email);
        }
        return empleado;
    }



}
