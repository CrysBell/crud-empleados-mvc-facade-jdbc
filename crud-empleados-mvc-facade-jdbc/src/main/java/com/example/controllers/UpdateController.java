package com.example.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import com.example.models.Departamento;
import com.example.models.EmpleadoUpdate;
import com.example.services.DepartamentoService;
import com.example.services.DepartamentoServiceImpl;
import com.example.services.EmpleadoService;
import com.example.services.EmpleadoServiceImpl;

/**
 * Servlet implementation class UpdateController
 */
@WebServlet("/UpdateController")
public class UpdateController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int idEmpleado = Integer.parseInt(request.getParameter("idEmpleado"));
		
		// Con el id del empleado, conectamos con la capa de servicios 
		// y recuperamos todo lo necesario para mostrar el formulario 
		// de actualización con los datos del empleado
		
		EmpleadoService empleadoService = new EmpleadoServiceImpl();
		
		// Recuperamos toda la información del empleado a actualizar, 
		// para mostrarla en el formulario de actualización
		
		EmpleadoUpdate empleadoUpdate = empleadoService.getEmpleadoById(idEmpleado);
		
		// Establecemos el empleado a actualizar como atributo de la request, 
		// para que el formulario de actualización pueda mostrar sus datos
		
		request.setAttribute("empleadoUpdate", empleadoUpdate);
		
		// Redirigimos a la vista del formulario de actualización
		
		// Necesitamos conectarnos con el servicio de Dpto para recuperar todos los
		// departamentos
		
		DepartamentoService departamentoService = new DepartamentoServiceImpl();
		
		try {
			List<Departamento> departamentos = departamentoService.getDepartamentos();
			request.setAttribute("departamentos", departamentos);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		request.getRequestDispatcher("views/formularioAltaModificacion.jsp")
			.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

}