package com.example.services;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import com.example.dao.DBConexion;
import com.example.models.Detalle;
import com.example.models.Empleado;
import com.example.models.Genero;

public class EmpleadoServiceImpl implements EmpleadoService{
	
	private static final Logger LOG = Logger.getLogger("EmpleadoServiceImpl");

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

	@Override
	public Detalle detalles(int idEmpleado) {
//
		Detalle detalles = null;
		
		try (DBConexion dbConexion = new DBConexion ("root", "Temp2026"); 
				Connection connection = dbConexion.getConnection()) {
			
			ResultSet rs = dbConexion.detallesEmpleado(idEmpleado, connection);
			
			//Para recuperar el nombre del departamento tengo que recorrer el RS
			String nombreDto = null;
			
			//Para no recorrer la lista y solo leer el primer elemento
			if (rs.next())
				nombreDto = rs.getString("nombreDpto");
				
			//Para recuperar la lista de números de telefono tambien tengo que recorrer el RS pero de tipo conjunto
			Set<String> numerosTelefono = new HashSet<String>();
			
			rs.beforeFirst();
			
			while (rs.next()) {
				numerosTelefono.add(rs.getString("numeroTelefono"));
			}
			
			//Para recuperar la lista de direcciones de correo tengo que recorrer el RS
			Set<String> emails = new HashSet<String>();
			
			rs.beforeFirst();
			while (rs.next()) {
				emails.add(rs.getString("email"));
			}
			
			detalles = new Detalle(nombreDto,
					numerosTelefono,
					emails);
			LOG.info("Detalle recuperado:" + detalles);
			
		} catch (Exception e) {
			LOG.severe("Error recuperando detalles en la capa de servicio" + e.getMessage());
		}
		return detalles;

	}
	
}
	


