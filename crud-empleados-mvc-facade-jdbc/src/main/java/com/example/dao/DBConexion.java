package com.example.dao;

import java.sql.Connection;
import java.sql.DriverManager;
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
	public void altaEmpleado(Empleado empleado, List<String> dirCorreos, List <String> numerosTelefono) {
		
	//Inserta empleado y devuelve el last inserted id en la tabla de empleados
		String query1;
		
	//Con el id del empleado tenemos que insertar sus correos y sus telefonos correspondientes
		
	//Esta query inserta correos
		String query2;
		
	//Esta query inserta telefonos
		String query3;
		
	}
}
