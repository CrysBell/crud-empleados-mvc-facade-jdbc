package com.example.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import com.example.models.Empleado;

public class DBConexion implements AutoCloseable {
	
	private static final Logger LOG = Logger.getLogger("DBConexion");
	
	private String user;
	private String password;
	private Connection connection;
	
	public DBConexion(String user, String password) {
		super();
		this.user = user;
		this.password = password;
	}
	
	// Metodo que estable la conexion con la base de datos
	public Connection getConexion() throws ClassNotFoundException {
		
		String urlConnection = "jdbc:mysql://localhost:3306/empresa-crud-empleados";
		
		Properties info = new Properties();
		
		info.put("user", this.user);
		info.put("password", this.password);
		
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			this.connection = DriverManager.getConnection(urlConnection, info);
			LOG.info("Conexion establecida con la base de datos, exitosamente !!!!");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return this.connection;
	}

	@Override
	public void close() throws Exception {
		this.connection.close();
	}
	
	// Metodo que recupera todos los registros de la tabla empleados
	public ResultSet getEmpleados(Connection connection) {
		
		ResultSet rs = null;
		String query = "SELECT * FROM `empresa-crud-empleados`.empleados";
		Statement stmt = null;
		
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
			
		return rs;
	}
	
	// Metodo que recupera todos los registros de la tabla departamentos
	public ResultSet getDptos(Connection connection) {
		
		ResultSet rs = null;
		String query = "SELECT * FROM `empresa-crud-empleados`.departamentos";
		Statement stmt = null;
		
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery(query);
		} catch (SQLException e) {
			LOG.severe("Error recuperando departamentos y la causa mas probable es: "
								+ e.getMessage());
			e.printStackTrace();
		}
		
		return rs;
	}
	
	// Metodo que inserta empleado y sus correos y telefonos en la base de datos, 
	// en el marco de una transaccion
	public void altaEmpleado(Empleado empleado, 
			List<String> dirCorreos, 
			List<String> numerosTelefono,
			Connection connection) throws SQLException {
		
		// Inserta empleado y devuelve el last inserted id en la tabla de empleados
		String query1 = "INSERT INTO `empleados` (`nombre`,"
				+ " `primerApellido`, `segundoApellido`,"
				+ " `fechaAlta`, `genero`, `salario`,"
				+ " `departamentos_id`) VALUES "
				+ "(?, ?, ?, ?, ?, ?, ?)";
		
		/* ¿Que son las sentencias preparadas? (Prepared Statements) 
		 * 
		 * Son la primera linea de defensa contra los ataques de inyeccion de SQL
		 * Separan la parte fija de la consulta de los parametros que recibe
		 * la misma. 
		 * El rendimiento es muy similar al de los procedimientos almacenados
		 * porque una vez que se ejecuta la consulta, el componente analizador 
		 * de consulta u optimizador de consulta no tiene que analizar 
		 * nuevamente el plan de ejecucion de la consulta, y la misma el compilada
		 * y guardada en el servidor, de forma tal que la proxima solamente hay que
		 * pasarle los parametros variables a la consulta para ejecutarla y la ejecu
		 * cion sera lo mas rapido, eficiente, y seguro posible
		 * 
		 * Nota: Los parametros que se le pasan la consulta preparada, comienzan en
		 * el valor 1, no cero. */
		
		// Con el id del empleado, tenemos que insertar sus corros y 
		// sus telefonos correspondientes
		// Inserta correos
		String query2 = "INSERT INTO `correos` (`email`, `empleados_id`) "
				+ "VALUES (?, ?)";
		
		
		// Inserta telefonos
		String query3 = "INSERT INTO `telefonos` (`numero`, `empleados_id`) "
				+ "VALUES (?, ?)";
		
		/* Tanto insertar el empleado como sus correos y telefonos tiene que 
		 * hacerse en el marco de una transaccion */
		
		try {
			// Iniciamos la transaccion
			connection.setAutoCommit(false);
			
			PreparedStatement stmt1 = connection.prepareStatement(query1,
							Statement.RETURN_GENERATED_KEYS);
			stmt1.setString(1, empleado.nombre());
			stmt1.setString(2, empleado.primerApellido());
			
			// Considerar que el segundo apellido no es requerido, no es necesario
			// porque ya lo hemos tenido en cuenta en el AltaController
			stmt1.setString(3, empleado.segundoApellido());
			stmt1.setDate(4, Date.valueOf(empleado.fechaAlta()));
			stmt1.setString(5, empleado.genero().name());
			stmt1.setDouble(6, empleado.salario().doubleValue());
			// stmt1.setBigDecimal(6, empleado.salario());
			stmt1.setInt(7, empleado.departamentos_id());
			
			// Lanzar la consulta preparada
			
		    int totalFilas = stmt1.executeUpdate();
		    
		    if (totalFilas != 0) {
		    	
		    	// Recuperamos el Id del empleado insertado, para ir 
		    	// a las tablas de telefonos y correos
		    	
		    	long lastInsertedId = 0L;
		    	
		    	ResultSet rs = stmt1.getGeneratedKeys();
		    	
		    	if (rs.next()) 
		    		lastInsertedId = rs.getLong(1);
		    	
		    	// Insertar correos si es que me los han proporcionado
		    	if (dirCorreos != null && dirCorreos.size() > 0) {
		    		
		    		PreparedStatement stmt2 = connection.prepareStatement(query2);
		    		
		    		stmt2.setInt(2, Math.toIntExact(lastInsertedId));
		    		
		    		/* El codigo siguiente funciona pero no es nada eficiente,
		    		 * porque por cada correo va a realizar una conexion a la base
		    		 * de datos, lo cual consume recursos, por lo cual lo mejor es
		    		 * tener el lote completo de los correos y enviarlo todo 
		    		 * de golpe */
//		    		for (String email : dirCorreos) {
//		    			stmt2.setString(1, email);
//		    			
//		    			stmt2.executeUpdate();
//		    		}
		    		
		    		for (String email : dirCorreos) {
		    			stmt2.setString(1, email);
		    			stmt2.addBatch();
		    		}
		    		
		    		stmt2.executeBatch();
		    	}
		    	
		    	// Insertar telefonos si es que me los han proporcionado
		    	if (numerosTelefono != null && numerosTelefono.size() > 0) {
		    		
		    		PreparedStatement stmt3 = connection.prepareStatement(query3);
		    		
		    		stmt3.setInt(2, Math.toIntExact(lastInsertedId));
		    		
		    		/* El codigo siguiente funciona pero no es nada eficiente,
		    		 * porque por cada correo va a realizar una conexion a la base
		    		 * de datos, lo cual consume recursos, por lo cual lo mejor es
		    		 * tener el lote completo de los correos y enviarlo todo 
		    		 * de golpe */
//		    		for (String numero : numerosTelefono) {
//		    			stmt3.setString(1, numero);
//		    			
//		    			stmt3.executeUpdate();
//		    		}
		    		
		    		for (String numero : numerosTelefono) {
		    			stmt3.setString(1, numero);
		    			stmt3.addBatch();
		    		}
		    		
		    		stmt3.executeBatch();
		    	}
		    	
		    	
		    }
			
			
			
			
			
			
			connection.commit();
		} catch (Exception e) {
			LOG.severe("Error insertando el nuevo empleado y la causa mas probable es: "
								+ e.getMessage());
			e.printStackTrace();
			connection.rollback();
			LOG.info("Transaccion revertida, no se ha insertado el nuevo empleado");
		} finally {
				connection.setAutoCommit(true);
		}
	}
	
	/* Metodo que recupera los detalles (Nombre del Dpto, los telefonos  
	 * y los correos) de un empleado cuyo id se recibe como parametro */
	public ResultSet detallesEmpleado(int idEmpleado, Connection connection) {
		
		ResultSet rs = null;
		String query = "select dep.nombre nombreDpto, tel.numero numeroTelefono, "
				+ "cor.email email\n"
				+ "from empleados emp left join departamentos dep on\n"
				+ "	emp.departamentos_id = dep.id left join telefonos tel on\n"
				+ "		emp.id = tel.empleados_id left join correos cor on \n"
				+ "			emp.id = cor.empleados_id\n"
				+ "where emp.id = ?";
		
		PreparedStatement stmt1 = null;
		
		try {
			stmt1 = connection.prepareStatement(query, 
					ResultSet.TYPE_SCROLL_INSENSITIVE, 
					ResultSet.CONCUR_UPDATABLE);
			
			stmt1.setInt(1, idEmpleado);
			rs = stmt1.executeQuery();
		} catch (SQLException e) {
			LOG.severe("Error recuperando detalles del empleado");
			e.printStackTrace();
		}
		
		return rs;
	}
	
	// Metodo que recupera toda la informacion del empleado que se va a actualizar
	public ResultSet getEmpleadoById(int idEmpleado, Connection connection) {
		
		ResultSet rs = null;
		String query = "select emp.id idEmpleado,\n"
				+ "		emp.nombre nombreEmpleado, \n"
				+ "		emp.primerApellido,\n"
				+ "        emp.segundoApellido,\n"
				+ "        emp.fechaAlta,\n"
				+ "        emp.genero,\n"
				+ "        emp.salario,\n"
				+ "        emp.departamentos_id,\n"
				+ "        dep.id idDpto,\n"
				+ "        dep.nombre nombreDpto,\n"
				+ "        tel.numero,\n"
				+ "        co.email\n"
				+ "from empleados emp left join departamentos dep on\n"
				+ "	emp.departamentos_id = dep.id left join correos co on \n"
				+ "		emp.id = co.empleados_id left join telefonos tel on \n"
				+ "			emp.id = tel.empleados_id\n"
				+ "where emp.id = ?";
		
		try {
			PreparedStatement stmt1 = connection.prepareStatement(query, 
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			stmt1.setInt(1, idEmpleado);
			rs = stmt1.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return rs;
	}
	
	
	// Metodo que actualiza un empleado
	public void updateEmpleado(Empleado empleado,
			List<String> emails, 
			List<String> telefonos,
			Connection connection) {
		
		String query1 = "UPDATE `empleados`"
				+ " SET `nombre` = ?, `primerApellido` = ?, "
				+ "`segundoApellido` = ?, `fechaAlta` = ?, "
				+ "`genero` = ?, `salario` = ?, `departamentos_id` = "
				+ "? WHERE (`id` = ?)";
		
		String eliminarTelefonos = "delete from telefonos where empleados_id = ?";
		String insertarTelefonos = "INSERT INTO `telefonos` (`numero`, `empleados_id`) "
		+ "VALUES (?, ?)";

		String eliminarCorreos = "delete from correos where empleados_id = ?";
		String insertarCorreos = "INSERT INTO `correos` (`email`, `empleados_id`) "
		+ "VALUES (?, ?)";
		
		try {
			PreparedStatement stmt1 = connection.prepareStatement(query1);
			stmt1.setString(1, empleado.nombre());
			stmt1.setString(2, empleado.primerApellido());
			stmt1.setString(3, empleado.segundoApellido());
			stmt1.setDate(4,  Date.valueOf(empleado.fechaAlta()));
			stmt1.setString(5, empleado.genero().name());
			stmt1.setBigDecimal(6, empleado.salario());
			stmt1.setInt(7, empleado.departamentos_id());
			stmt1.setInt(8, empleado.id());
			stmt1.executeUpdate();
			
			// Eliminar los correos y los telefonos para el empleado 
			// e insertar los nuevos recibidos
			
			PreparedStatement stmtEliminarTelefonos = connection.prepareStatement(eliminarTelefonos);
			stmtEliminarTelefonos.setInt(1, empleado.id());
			stmtEliminarTelefonos.executeUpdate();
			
			PreparedStatement stmtInsertarTelefonos = connection.prepareStatement(insertarTelefonos);
			
			stmtInsertarTelefonos.setInt(2, empleado.id());
			 
			for (String numero : telefonos) {
				stmtInsertarTelefonos.setString(1, numero);
				stmtInsertarTelefonos.addBatch();
			}
			
			stmtInsertarTelefonos.executeBatch();
			
			// Lo mismo para los correos
			
			
			PreparedStatement stmtEliminarCorreos = connection.prepareStatement(eliminarCorreos);
			stmtEliminarCorreos.setInt(1, empleado.id());
			stmtEliminarCorreos.executeUpdate();
			
			PreparedStatement stmtInsertarCorreos = connection.prepareStatement(insertarCorreos);
			
			stmtInsertarCorreos.setInt(2, empleado.id());
			 
			for (String email : emails) {
				stmtInsertarCorreos.setString(1, email);
				stmtInsertarCorreos.addBatch();
			}
			
			stmtInsertarCorreos.executeBatch();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}














