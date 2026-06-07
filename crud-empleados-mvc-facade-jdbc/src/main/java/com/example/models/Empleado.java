package com.example.models;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Builder;

@Builder
public record Empleado(
		int id, 
		String nombre, 
		String primerApellido, 
		String segundoApellido, 
		LocalDate fechaAlta,
		Genero genero,
		BigDecimal salario,
		int departamentos_id ) {

	
}
