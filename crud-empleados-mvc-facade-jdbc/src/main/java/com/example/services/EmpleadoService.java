package com.example.services;

import java.sql.SQLException;
import java.util.List;

import com.example.controllers.DetallesController;
import com.example.models.Detalle;
import com.example.models.Empleado;

public interface EmpleadoService {
	
	boolean isConnectionOk() throws SQLException, Exception;
	
	List<Empleado> getEmpleados();
	
	void altaEmpleado(Empleado empleado, 
			List<String> emails, 
			List<String> nTelefonos) throws SQLException;
	
	Detalle detalles(int idEmpleado);
	
	
}
