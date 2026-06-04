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

	// Metodo que se encarga de establecer la conexion a la base de datos
	public Connection getConnection() throws ClassNotFoundException {

		String urlConnection = "jdbc:mysql://localhost:3306/empresa-crud-empleados";

		Properties info = new Properties();
		info.put("user", this.user);
		info.put("password", this.password);

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			this.connection = DriverManager.getConnection(urlConnection, info);
			LOG.info("Conexion establecida a la base de datos");
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

	// La aplicación no puede consumir los metodos directamente en la capa DAO, por
	// lo que se deben crear metodos que se encarguen de recuperar los datos de la
	// base de datos y devolverlos a la capa de servicios, en la capa servicio
	// creamos una interface
	// dentro de la interface desarrollamos la List
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
			LOG.severe("Error recuperando departamentos y la causa mas probable es: " + e.getMessage());
			e.printStackTrace();
		}

		return rs;
	}

	
	// metodo que inserta empleado y sus correos y telefonos en la base de datos
	public void altaEmpleado(Empleado empleado, 
			List<String> dirCorreos,
			List<String> numerosTelefono,
			Connection connection) throws SQLException{

		// Inserta empleado y devuelve el last inserted id en la tabla de empleados
		String query1 = "INSERT INTO `empleados` (`nombre`, "
		        + " `primerApellido`, `segundoApellido`, "
		        + " `fechaAlta`, `genero`, `salario`, "
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
		 * cion sera lo mas rapido, eficiente, y seguro posible */

		// Con el id del empleado, tenemos que insertar sus corros y
		// sus telefonos correspondientes
		
		
				String query2 = "INSERT INTO `correos` (`email`, `empleados_id`) "
				        + "VALUES (?, ?)";

				// Inserta telefonos
				String query3 = "INSERT INTO `telefonos` (`numero`, `empleados_id`) "
				        + "VALUES (?, ?)";

	      // Tanto insertar el empleado como sus correos y telefonos tiene que
		  //hacerse en el marco de una transaccion 

		try {
			//iniciamos la transacción
			connection.setAutoCommit(false);
			
			PreparedStatement stmt1 = connection.prepareStatement(query1, 
					Statement.RETURN_GENERATED_KEYS); 
			
			stmt1.setString(1, empleado.nombre() );
			stmt1.setString(2, empleado.primerApellido());			
	//considerar que el segundo apellido puede ser null, no es necesario porque ya lo hemos tenido en cuenta en el AltaController
			stmt1.setString(3, empleado.segundoApellido());
			stmt1.setDate(4,Date.valueOf(empleado.fechaAlta()));
			stmt1.setString(5, empleado.genero().name());
			stmt1.setDouble(6, empleado.salario().doubleValue() );
			stmt1.setInt(7, empleado.departamentos_id());
			
			//Lanzar la consulta preparada
			int totalFilas = stmt1.executeUpdate();
			
			if (totalFilas != 0) {

				//Recuperar el último id ensertado en la tabla de empleados. Para ir a las tablas de telefono y correo
				long lastInsertedId = 0L;
				
			    ResultSet rs =	stmt1.getGeneratedKeys();
			    
			   
			    if (rs.next()) 
			    	lastInsertedId = rs.getLong(1);
			    
			    
			    // Insertar correos si me los han proporcionado
			    if (dirCorreos != null && dirCorreos.size() > 0 ) {
			    	
			    	PreparedStatement stmt2 = connection.prepareStatement(query2);
			    	
			    	stmt2.setInt(2, Math.toIntExact(lastInsertedId));
			    	
			    	/* El codigo siguiente funciona pero no es nada eficiente,
			    	 * porque por cada correo va a realizar una conexion a la base
			    	 * de datos, lo cual consume recursos, por lo cual lo mejor es
			    	 * tener el lote completo de los correos y enviarlo todo
			    	 * de golpe */
			    	
			    	for (String email : dirCorreos ) {
			    		stmt2.setString(1, email);
			    		stmt2.addBatch();
			    	}
			    	stmt2.executeBatch();
			    }
			    	
			    // Insertar telefonos si me los han proporcionado
			    if (numerosTelefono != null && numerosTelefono.size() > 0 ) {
			    	
			    	PreparedStatement stmt3 = connection.prepareStatement(query3);
			    	
			    	stmt3.setInt(2, Math.toIntExact(lastInsertedId));
			    	
			    	/* El codigo siguiente funciona pero no es nada eficiente,
			    	 * porque por cada telefono va a realizar una conexion a la base
			    	 * de datos, lo cual consume recursos, por lo cual lo mejor es
			    	 * tener el lote completo de los telefonos y enviarlo todo
			    	 * de golpe */
			    	
			    	for (String numero : numerosTelefono ) {
			    		stmt3.setString(1, numero);
			    		stmt3.addBatch();
			    	}
			    	stmt3.executeBatch();
			    }
			}
			
			connection.commit();
		} catch (Exception e) {
			LOG.severe("Error insertando empleado y la causa mas probable es: " + e.getMessage());
			e.printStackTrace();
			connection.rollback();
			LOG.info(" Transaccion revertida, no se ha insertado el nuevo empleado");
			
		} finally {
			connection.setAutoCommit(true);
		}
		
		

		
		
		
	}
	
	
	/*Metodo que recupera los detalles (Nombre del departamento, los telefonos y los correos cuyo id se recibe como parámetro*/
	public ResultSet detallesEmpleado(int idEmpleado, Connection connection) {
		
		ResultSet rs = null;
		String query = "select dep.nombre nombreDpto, tel.numero numeroTelefono, "
				+ "cor.email email\r\n"
				+ "from empleados emp left join departamentos dep on \r\n"
				+ "emp.departamentos_id = dep.id left join telefonos tel on \r\n"
				+ "emp.id = tel.empleados_id left join correos cor on\r\n"
				+ "emp.id = cor.empleados_id\r\n"
				+ "where emp.id = ?";
		PreparedStatement stmt1 = null;
		
		try {
			stmt1 = connection.prepareStatement(query, 
					ResultSet.TYPE_SCROLL_INSENSITIVE, 
					ResultSet.CONCUR_UPDATABLE);
			
			stmt1.setInt(1, idEmpleado);
			rs = stmt1.executeQuery();
		} catch (SQLException e) {
			LOG.severe("Error recuperando detalles del empleado" + e.getMessage());
			e.printStackTrace();
		}
		return rs;
	}
}
