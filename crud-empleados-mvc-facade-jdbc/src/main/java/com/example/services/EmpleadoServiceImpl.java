package com.example.services;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.dao.DBConexion;
import com.example.models.Empleado;
import com.example.models.Genero;

public class EmpleadoServiceImpl implements EmpleadoService{

	@Override
	public boolean isConnectionOk() throws SQLException {
		
		DBConexion dbConexion = new DBConexion("root", "Temp2026");
		
		boolean connectionOk = false;
		Connection connection = null;
		
		try {
			connection = dbConexion.getConnection();
			if(connection != null)
				connectionOk = true;
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (connection != null)
				connection.close();
		}
		 
		return connectionOk;
	}

	@Override
	public List<Empleado> getEmpleados(){
		
		List<Empleado> empleados = new ArrayList<Empleado>();
		try (DBConexion dbConexion = new DBConexion("root", "Temp2026");
				Connection connection = dbConexion.getConnection()) {
			
			ResultSet rs = dbConexion.getEmpleados(connection);
			
			while (rs.next()) {
				
				empleados.add(
						Empleado.builder()
						.id(rs.getInt("id"))
						.nombre(rs.getString("nombre"))
						.primerApellido(rs.getString("primerApellido"))
						.segundoApellido(rs.getString("segundoApellido"))
						.fechaAlta(rs.getDate("fechaAlta").toLocalDate())
						.genero(Genero.valueOf(rs.getString("genero")))
						.salario(new BigDecimal(rs.getDouble("salario")))
						.departamentos_id(rs.getInt("departamentos_id"))
						.build()
						);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	
		return empleados;
	}

	@Override
	public void altaEmpleado (Empleado empleado, 
			List<String> emails, 
			List<String> nTelefonos) throws SQLException {
		// TODO Auto-generated method stub
		 try (DBConexion dbConexion = new DBConexion("root", "Temp2026"); 
				 Connection connection = dbConexion.getConnection()) {
			 
			 dbConexion.altaEmpleado(empleado, emails, nTelefonos, connection);
			 
			 System.out.println("Empleado dado de alta correctamente");
			
		} catch (Exception e) {
			
		}
	}
	
}
	


