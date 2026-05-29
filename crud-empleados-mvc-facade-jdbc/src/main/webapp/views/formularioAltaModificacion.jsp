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
<body>
	<h1>Formulario de Alta/Modificación de Empleado</h1>


	<fieldset>
		<leyend>Formulario de gestión de empleados</leyend>

		<form action="AltaController" method="post">
			<div>
				<label for="nombre">Nombre:</label> 
				<input type="text" id="nombre" name="nombre"
					placeholder="Escribe tu nombre aquí" required>
			</div>
			<div>
				<label for="primerApellido">Primer apellido:</label> 
				<input type="text" id="primerApellido" name="primerApellido"
					placeholder="Escribe tu primer apellido aquí" required>
			</div>
			<div>
				<label for="segundoApellido">Segundo apellido:</label> 
				<input type="text" id="segundoApellido" name="segundoApellido"
					placeholder="Escribe tu segundo apellido aquí">
			</div>
			<div>
				<label for="fechaAlta">Fecha de Alta</label>
				<input id="fechaAlta" name="fechaAlta" type="date" required> 
			</div>
			<div>
				<fielset>
				  <legend>Genero</legend>
				  <label for="hombre">Hombre</label>
				  <input type="radio" id="hombre" name="genero" value="HOMBRE" required>
				  <label for="mujer">Mujer</label>
				  <input type="radio" id="mujer" name="genero" value="MUJER" required>
				  <label for="otro">Otro</label>
				  <input type="radio" id="otro" name="genero" value="OTRO" required> 
				</fielset>
			</div>
			<div>
			<label for="salario">Salario</label>
			<input type="text" id="salario" name="salario" placeholder="Escribe tu salario aquí" required>
			</div>
			
				<div>
				  <%
				  	List<Departamento> departamentos = (List<Departamento>) request.getAttribute("departamentos");
				  %>
				<label for="departamento">Selecciona el departamento</label>
				<select name="departamento" id="departamento" required>
					<option></option>
					
					<%
						for (Departamento departamento : departamentos) {
					%>
						<option value="<%=departamento.id()%>"> <%=departamento.nombre()  %></option>
					<%
						}
					%>
	
				</select>
				</div>
			
			<div>
				<label for="correos">Correos:</label>
				<input type="text" id="correos" name="correos"
					placeholder="Uno o varios separados ;"/>
			</div>
			
			<div>
				<label for="telefonos">Númeos de Telefono:</label>
				<input type="text" id="telefonos" name="telefonos"
					placeholder="Uno o varios separados ;"/>
			</div>
			
			<br>
			<br>
			<input type="submit" value="Actualizar">






			
		</form>

	</fieldset>


</body>
</html>