package com.example.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import com.example.models.Departamento;
import com.example.models.Empleado;
import com.example.models.Genero;
import com.example.services.DepartamentoService;
import com.example.services.DepartamentoServiceImpl;
import com.example.services.EmpleadoService;
import com.example.services.EmpleadoServiceImpl;

/**
 * Servlet implementation class AltaController
 */
@WebServlet("/AltaController")
public class AltaController extends HttpServlet {
	private static final Logger LOG = Logger.getLogger("AltaController");
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AltaController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		DepartamentoService departamentoService = new DepartamentoServiceImpl();
		
		List<Departamento> departamentos = null;
		
		try {
			departamentos = departamentoService.getDepartamentos();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		request.setAttribute("departamentos", departamentos);
		
		request.getRequestDispatcher("views/formularioAltaModificacion.jsp").forward(request, response);
	}
	
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//Aqui se reciben los datos procedentes de los controles del formulario
		//Tener en cuenta que toda la información llega en formato String
		
		String nombre = request.getParameter("nombre");
		String primerApellido = request.getParameter("primerApellido");
		String segundoApellido = request.getParameter("segundoApellido") == null ? 
				"" : request.getParameter("segundoApellido");
		LocalDate fechaAlta = LocalDate.parse(request.getParameter("fechaAlta"));
		Genero genero = Genero.valueOf(request.getParameter("genero"));
		BigDecimal salario = BigDecimal.valueOf(Double.valueOf(request.getParameter("salario")));
		
		int departamentos_id = Integer.parseInt(request.getParameter("departamento")) ;
		
		
		
		//Tener en cuenta que los correos y telefonos no son requeridos
		List<String> direccionesCorreos = null;
		
		
		List<String> numerosDeTelefono = null;
		
		if (request.getParameter("correos") != null) {
			
			String direccionesCorreoRecibidas = request.getParameter("correos");
			String[] arrayDirrCorreosRecibidos = direccionesCorreoRecibidas.split(";");
			
			direccionesCorreos = Arrays.asList(arrayDirrCorreosRecibidos);
			
			System.out.println("Direcciones de correo recibidas:");
			direccionesCorreos.forEach(System.out::println);
		}
		
		if (request.getParameter("telefonos") != null) {
			
			String numerosTelRecibidos = request.getParameter("telefonos");
			String[] arrayNumerosTelRecibidos = numerosTelRecibidos.split(";");
			
			numerosDeTelefono = Arrays.asList(arrayNumerosTelRecibidos);
			
			numerosDeTelefono = Arrays.asList(arrayNumerosTelRecibidos);
			
			System.out.println("Números de teléfono recibidos:");
			numerosDeTelefono.forEach(System.out::println);
			
		}
		//Crear el objeto empleados
		Empleado empleado = Empleado.builder()
				.nombre(nombre)
				.primerApellido(primerApellido)
				.segundoApellido(segundoApellido)
				.fechaAlta(fechaAlta)
				.genero(genero)
				.salario(salario)
				.departamentos_id(departamentos_id)
				.build();
				
		//Aquí se deberia llamar al servicio para que se encargue de insertar el uevo empleado en la base de datos
		EmpleadoService empleadoService = new EmpleadoServiceImpl();

		try {
		    empleadoService.altaEmpleado(
		            empleado,
		            direccionesCorreos,
		            numerosDeTelefono);

		} catch (SQLException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
		
		
		List<Empleado> empleados = empleadoService.getEmpleados();
		request.setAttribute("empleados", empleados);
		
		request.getRequestDispatcher("views/listadoEmpleados.jsp").forward(request, response);
		
		
		//comprobando
		
		LOG.info("Nombre recibido: " + nombre);
		LOG.info("Segundo apellido: " + segundoApellido);
	}

}
