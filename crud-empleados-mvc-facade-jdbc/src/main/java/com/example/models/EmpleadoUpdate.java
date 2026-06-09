package com.example.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import com.example.models.Genero;

public record EmpleadoUpdate(
	int id,
	String nombreEmpleado,
	String primerApellido,
	String segundoApellido,
	LocalDate fechaAlta,
	Genero genero,
	BigDecimal salario,
	int idDpto,
	String nombreDpto,
	Set<String> numerosTelefono,
	Set<String> emails
		) {}
