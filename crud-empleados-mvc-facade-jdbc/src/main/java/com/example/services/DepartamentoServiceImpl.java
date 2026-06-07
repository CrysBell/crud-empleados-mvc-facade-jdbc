package com.example.services;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.example.dao.DBConexion;
import com.example.models.Departamento;

public class DepartamentoServiceImpl implements DepartamentoService{
	private static final Logger LOG = Logger.getLogger("DepartamentosServiceImpl");
	
	@Override
	public List<Departamento> getDepartamentos() throws Exception {
		List<Departamento> departamentos = new ArrayList<Departamento>();
		
		try (DBConexion dbConexion = new DBConexion("root", "Temp2026");
				
			Connection connection = dbConexion. getConnection()){
			
				ResultSet rs = dbConexion.getDptos(connection);
				
				while (rs.next()) {
					departamentos.add(Departamento.builder()
							.id(rs.getInt("id"))
							.nombre(rs.getString("nombre"))
							.build());
				}
			
		} catch (SQLException e) {
			
			LOG.severe("Error recuperando Dptos en la capa de servicio y la causa más probable es:" + e.getMessage());
			e.printStackTrace();
		}
		
		
		return departamentos;
	}


}
