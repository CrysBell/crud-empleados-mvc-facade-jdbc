<%@page import="com.example.models.Detalle"%>
<%@page import="com.example.models.Empleado"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Detalles del empleado</title>
</head>
<body>
	<%
		Empleado empleado = (Empleado) request.getAttribute("empleado");
		Detalle detalles = (Detalle) request.getAttribute("detalles");
		
	%>
	<h1>Detalles del empleado : <%=empleado.nombre() + " " + empleado.primerApellido() + " " + empleado.segundoApellido() %></h1>
	
	<h3> Departamento: <%= detalles.nombreDpto() %></h3>
	
	<div>
		<h3> Telefonos:</h3>
		<ul>
			<%
				for (String numero : detalles.telefonos()){
		     %>
				<li><%=numero%></li>	
					
					<%
				}
			%>
		</ul>
	</div>
	<div>
		<h3> Correos:</h3>
		<ul>
			<%
				for (String correo : detalles.correos() ){
		     %>
				<li><%=correo %></li>		
			 <%
				}
			%>
		</ul>
	</div>
	
	
</body>
</html>