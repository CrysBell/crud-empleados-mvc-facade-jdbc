package com.example.services;

import java.sql.SQLException;
import java.util.List;

import com.example.models.Detalle;
import com.example.models.Empleado;
import com.example.models.EmpleadoUpdate;

public interface EmpleadoService {
	boolean isConnectionOk() throws SQLException, Exception ;
	List<Empleado> getEmpleados();
	void altaEmpleado(Empleado empleado, 
			List<String> emails, 
			List<String> nTelefonos) throws SQLException;
	Detalle detalles(int idEmpleado);
	EmpleadoUpdate getEmpleadoById(int idEmpleado);
	void updateEmpleado(Empleado empleado,
			List<String> emails,
			List<String> telefonos);
	
}