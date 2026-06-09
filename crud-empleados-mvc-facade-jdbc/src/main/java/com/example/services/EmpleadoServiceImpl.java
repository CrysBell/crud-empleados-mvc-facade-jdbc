package com.example.services;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import com.example.dao.DBConexion;
import com.example.models.Detalle;
import com.example.models.Empleado;
import com.example.models.EmpleadoUpdate;
import com.example.models.Genero;

public class EmpleadoServiceImpl implements EmpleadoService {
	
	private static final Logger LOG = Logger.getLogger("EmpleadoServiceImpl");

	@Override
	public boolean isConnectionOk() throws Exception {
		
		
		
		boolean connectionOk = false;
		
		
		try (DBConexion dbConexion = new DBConexion("root", "Temp2026");
			  Connection connection = dbConexion.getConexion())  {
			
			if (connection != null) 
				connectionOk = true;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return connectionOk;
	}

	@Override
	public List<Empleado> getEmpleados() {
		
		List<Empleado> empleados = new ArrayList<Empleado>();
		
		try (DBConexion dbConexion = new DBConexion("root", "Temp2026");
				Connection connection = dbConexion.getConexion()) {
			
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
	public void altaEmpleado(Empleado empleado, 
			List<String> emails, List<String> nTelefonos) throws SQLException {
		
		try (DBConexion dbConexion = new DBConexion("root", "Temp2026");
				Connection connection = dbConexion.getConexion()) {
			dbConexion.altaEmpleado(empleado, emails, nTelefonos, connection);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Detalle detalles(int idEmpleado) {
		
		Detalle detalles = null;
		
		try (DBConexion dbConexion = new DBConexion("root", "Temp2026");
				Connection connection = dbConexion.getConexion()) {
			
			ResultSet rs = dbConexion.detallesEmpleado(idEmpleado, connection);
			
			// Para recuperar el nombre del Dpto
			String nombreDpto = null;
			
			if (rs.next())
				nombreDpto = rs.getString("nombreDpto");
			
			// Para recuperar la lista de numeros de Telefono
			Set<String> numerosTelefono = new HashSet<String>();
		
			rs.beforeFirst();
			
			while (rs.next()) {
				numerosTelefono.add(rs.getString("numeroTelefono"));
			}
			
			
			// Para recuperar la lista de direcciones de correos
			Set<String> emails = new HashSet<String>();
			
			rs.beforeFirst();
			
			while (rs.next()) {
				emails.add(rs.getString("email"));
			}
			
			detalles = new Detalle(nombreDpto,
					numerosTelefono, 
					emails);
			
			// Mostrar el record detalles en la consola
			LOG.info("Detalle recuperado: " + detalles);
			
			
		} catch (Exception e) {
			LOG.severe("Error recuperando detalles en la capa de servicios");
		}
		
		return detalles;
	}

	@Override
	public EmpleadoUpdate getEmpleadoById(int idEmpleado) {
		
		EmpleadoUpdate empleadoUpdate = null;
		
		try (DBConexion dbConexion = new DBConexion("root", "Temp2026");
				Connection connection = dbConexion.getConexion()) {
			
			ResultSet rs = dbConexion.getEmpleadoById(idEmpleado, connection);
			
			int idEmp = 0;
			String nombreEmpleado = null;
			String primerApellido = null;
			String segundoApellido = null;
			LocalDate fechaAlta = null;
			Genero genero = null;
			BigDecimal salario = new BigDecimal(0);
			int idDpto = 0;
			String nombreDpto = null;
			
			Set<String> numerosTelefono = new HashSet<String>();
			Set<String> emails = new HashSet<String>();
			
			if (rs.next()) {
				idEmp = rs.getInt("idEmpleado");
				nombreEmpleado = rs.getString("nombreEmpleado");
				primerApellido = rs.getString("primerApellido");
				segundoApellido = rs.getString("segundoApellido");
				fechaAlta = rs.getDate("fechaAlta").toLocalDate();
				genero = Genero.valueOf(rs.getString("genero"));
				salario =  new BigDecimal(rs.getDouble("salario"));
				idDpto = rs.getInt("idDpto");
				nombreDpto = rs.getString("nombreDpto");
			}
			
			rs.beforeFirst();
			
			while (rs.next()) {
				numerosTelefono.add(rs.getString("numero"));
			}
			
			rs.beforeFirst();
			
			while (rs.next()) {
				emails.add(rs.getString("email"));
			}
			
			empleadoUpdate = new EmpleadoUpdate(idEmp, nombreEmpleado,
					primerApellido, segundoApellido, fechaAlta, genero, salario, 
					idDpto, nombreDpto, numerosTelefono, emails);
			
		} catch (Exception e) {
			// TODO: handle exception
			LOG.severe("Error recuperando empleado por id en "
					+ "la capa de servicios " + e.getMessage());
			e.printStackTrace();
		}
		
		return empleadoUpdate;
	}

	@Override
	public void updateEmpleado(Empleado empleado, List<String> emails, List<String> telefonos) {
		// TODO Auto-generated method stub
		try (DBConexion dbConexion = new DBConexion("root", "Temp2026");
				Connection connection = dbConexion.getConexion()) {
			dbConexion.updateEmpleado(empleado, emails, telefonos, connection);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

}











