package com.example.services;

import java.util.List;

import com.example.models.Departamento;

public interface DepartamentoService {
	
	List<Departamento> getDepartamentos() throws Exception;
	
}
