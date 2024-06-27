package com.example.pruebacursosformacion;

import com.example.pruebacursosformacion.entidades.Curso;
import com.example.pruebacursosformacion.entidades.Empleado;
import com.example.pruebacursosformacion.servicios.CursoDAO;
import com.example.pruebacursosformacion.servicios.EmpleadoDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Locale;

@SpringBootApplication
public class PruebaCursosFormacionApplication implements CommandLineRunner {

	@Autowired
	private MessageSource messageSource;

	public static void main(String[] args) {
		SpringApplication.run(PruebaCursosFormacionApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		// Imprimir mensajes de depuración
		Locale locale = LocaleContextHolder.getLocale();
		System.out.println("Locale actual: " + locale);
		System.out.println("Mensajes cargados en el sistema:");
		//System.out.println("basedatos.empleado.guardar: " + messageSource.getMessage("basedatos.empleado.guardar", null, "No se encontró el mensaje", locale));


	}



}
