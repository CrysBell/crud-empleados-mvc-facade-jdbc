package com.example.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import com.example.models.Detalle;
import com.example.models.Empleado;
import com.example.services.EmpleadoService;
import com.example.services.EmpleadoServiceImpl;

/**
 * Servlet implementation class DetallesController
 */
@WebServlet("/DetallesController")
public class DetallesController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final Logger LOG = Logger.getLogger("DetallesController");
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DetallesController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Recibir el ID del empleado que es el parámetro que me envian con la petición (requets), añadimos comentario
			
		int idEmpleado = Integer.parseInt(request.getParameter("idEmpleado"));
		
		//Podriamos comprobar si se esta recibiendo correctamente el ID mostrandolo en la consola, añadimos comentario
		LOG.info("Id Empleado recibido: " + idEmpleado);
		
		//Conectar con la capa DAO
		EmpleadoService empleadoService = new EmpleadoServiceImpl();
		
		//Recuperamos todos los empleados y lo filtramos para obtener el empleado cuyo id se ha recibido , añadimos comentario
		
		List<Empleado> empleados = empleadoService.getEmpleados();
		
		Empleado empleado = empleados.stream()
				.filter ( e->e.id() == idEmpleado )
				.findFirst()
				.orElseThrow( () -> new RuntimeException("Empleado no encontrado") );
		
		request.setAttribute("empleado", empleado);
		
		Detalle detalles = empleadoService.detalles(idEmpleado);
		
		//Mostrar la vista detallesEmpleados.jsp
		
		request.setAttribute("detalles", detalles);
		
		request.getRequestDispatcher("views/detallesEmpleado.jsp")
			.forward(request, response);
		
	}

	private Object findFirst() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

}
