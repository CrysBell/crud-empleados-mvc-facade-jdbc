<%@page import="com.example.models.Empleado"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Listado Empleados</title>
<style>

    body{
        font-family: Arial, sans-serif;
        background-color: #f4f6f9;
        margin: 0;
        padding: 30px;
    }

    h1{
        text-align: center;
        color: #2c3e50;
        margin-bottom: 30px;
    }

    table{
        width: 100%;
        border-collapse: collapse;
        background-color: white;
        box-shadow: 0 4px 10px rgba(0,0,0,0.1);
        border-radius: 10px;
        overflow: hidden;
    }

    thead{
        background-color: #3498db;
        color: white;
    }

    th{
        padding: 15px;
        text-align: left;
        font-size: 15px;
    }

    td{
        padding: 12px 15px;
        border-bottom: 1px solid #ddd;
    }

    tbody tr:nth-child(even){
        background-color: #f9f9f9;
    }

    tbody tr:hover{
        background-color: #eaf4ff;
        transition: 0.3s;
    }

</style>
</head>
<body>
	<%
	List<Empleado> empleados = (List<Empleado>) request.getAttribute("empleados");
	%>
	<h1>Listado de Empleados</h1>
	
	<div>
		<a href="AltaController" title="Muestra el formulario de alta/modificación de empleado" >Alta de Empleado</a>
	</div>
	
	<table>
		<thead>
		    <tr>
                <th>Nombre</th>
                <th>Primer Apellido</th>
                <th>Segundo Apellido</th>
                <th>Fecha Alta</th>
                <th>Genero</th>
                <th>Salario</th>
            </tr>
		
		</thead>
		<tbody>
			<%
			for (Empleado empleado : empleados) {
			%>
				<tr>
                    <td><%= empleado.nombre() %></td>
                    <td><%= empleado.primerApellido() %></td>
                    <td><%= empleado.segundoApellido() %></td>
                    <td><%= empleado.fechaAlta() %></td>
                    <td><%= empleado.genero() %></td>
                    <td><%= empleado.salario() %></td>
                </tr>
               <%
			 }
			%>
		</tbody>
	</table>
</body>
</html>