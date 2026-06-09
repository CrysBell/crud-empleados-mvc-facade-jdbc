<%@page import="java.util.stream.Collectors"%>
<%@page import="com.example.models.Genero"%>
<%@page import="com.example.models.EmpleadoUpdate"%>
<%@page import="com.example.models.Departamento"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Formulario</title>
</head>
<style>
    body{
        font-family: Arial, sans-serif;
        background-color: #f4f6f9;
        margin: 0;
        padding: 30px;
    }

    h1{
        text-align: center;
        font-size: 24px;
        color: #2c3e50;
        margin-bottom: 30px;
        
    }
    
    fieldset{
        width: 500px;
        border-collapse: collapse;
        color:   #3498db;
        background-color: peach;
        box-shadow: 0 4px 10px rgba(0,0,0,0.1);
        border-radius: 10px;
        overflow: hidden;
        margin: 0 auto; 
    }
    legend{
        font-size: 18px;
        font-weight: bold;
        color: #2c3e50;
        padding: 10px;
    }
</style>
<body>
	
	<%EmpleadoUpdate empleadoUpdate = (EmpleadoUpdate) request.getAttribute("empleadoUpdate");%>
	
	<h1>Formulario de Alta/Modificación de Empleado</h1>


	<fieldset>
	
		<legend>Datos del empleado</legend>

		<form action="AltaController" method="post">
		<!--El valor del campo oculto será 0 si es un alta nueva y de lo contrario tendrá el id del empleado a actualizar  -->
	    <input type="hidden" name="idEmpleado" value="<%=empleadoUpdate == null ? 0 : empleadoUpdate.id() %>">
			<div>
				<label for="nombre">Nombre:</label> 
				<input type="text" id="nombre" name="nombre"
					placeholder="Escribe tu nombre aquí" required 
					value="<%=empleadoUpdate != null ? empleadoUpdate.nombreEmpleado() : ' ' %>">
			</div>
			<div>
				<label for="primerApellido">Primer apellido:</label> 
				<input type="text" id="primerApellido" name="primerApellido"
					placeholder="Escribe tu primer apellido aquí" required
					value="<%=empleadoUpdate != null ? empleadoUpdate.primerApellido() : ' ' %>">
			</div>
			<div>
				<label for="segundoApellido">Segundo apellido:</label> 
				<input type="text" id="segundoApellido" name="segundoApellido"
					placeholder="Escribe tu segundo apellido aquí"
					value="<%=empleadoUpdate != null ? empleadoUpdate.segundoApellido() : ' ' %>">
			</div>
			<div>
				<label for="fechaAlta">Fecha de Alta</label>
				<input id="fechaAlta" name="fechaAlta" type="date" required 
                    value="<%=empleadoUpdate != null ? empleadoUpdate.fechaAlta() : ' ' %>"> 
			</div>
			<div>
				<fielset>
				  <legend>Genero</legend>
				  <label for="hombre">Hombre</label>
				  <input type="radio" id="hombre" name="genero" value="HOMBRE" <%=empleadoUpdate != null && empleadoUpdate.genero().equals(Genero.HOMBRE) ? "checked" : "" %> required>
				  <label for="mujer">Mujer</label>
				  <input type="radio" id="mujer" name="genero" value="MUJER" <%=empleadoUpdate != null && empleadoUpdate.genero().equals(Genero.MUJER) ? "checked" : "" %> required>
				  <label for="otro">Otro</label>
				  <input type="radio" id="otro" name="genero" value="OTRO" <%=empleadoUpdate != null && empleadoUpdate.genero().equals(Genero.OTRO) ? "checked" : "" %> required> 
				</fielset>
			</div>
			<div>
			<label for="salario">Salario</label>
			<input type="text" id="salario" name="salario" placeholder="Escribe tu salario aquí" required
			value="<%=empleadoUpdate != null ? empleadoUpdate.salario() : ' ' %>">
			</div>
			
				<div>
				  <%
				  	List<Departamento> departamentos = (List<Departamento>) request.getAttribute("departamentos");
				  %>
				<label for="departamento">Selecciona el departamento</label>
			<select name="departamento" id="departamento" required>
		    	<option value=""></option>
		
		   		 <% for (Departamento departamento : departamentos) { %>
		
		        <option value="<%= departamento.id() %>"
		            <%= (empleadoUpdate != null && empleadoUpdate.idDpto() == departamento.id())
		                    ? "selected='selected'"
		                    : "" %>>
		            <%= departamento.nombre() %>
		        </option>
		
		  	  <% } %>
		
			</select>
				</div>
			
			<div>
				<label for="correos">Correos:</label>
				<input type="text" id="correos" name="correos"
					placeholder="Uno o varios separados ;" 
				
					value="<%=empleadoUpdate != null && 
					! empleadoUpdate.emails().contains(null) ? 
					empleadoUpdate.emails().stream()
					.collect(Collectors.joining(";")) : ' ' %>" />
			</div>
			
			<div>
				<label for="telefonos">Números de Telefono:</label>
				<input type="text" id="telefonos" name="telefonos"
					placeholder="Uno o varios separados ;"
					value="<%=empleadoUpdate != null && 
					! empleadoUpdate.numerosTelefono().contains(null) ? empleadoUpdate.numerosTelefono().stream().collect(Collectors.joining(";")) : ' ' %>" />
			</div>
			
			<br>
			<br>
			
			<input type="submit" value="Enviar">
			
		
		</form>

	</fieldset>
	


</body>
</html>