<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Página de inicio</title>
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
    div{
        width: 250px;
        border-collapse: collapse;
        color:   #3498db;
        background-color: white;
        box-shadow: 0 4px 10px rgba(0,0,0,0.1);
        border-radius: 10px;
        overflow: hidden;
        margin: 0 auto;
        
    }
    a{
        padding: 15px;
        text-align: left;
        font-size: 15px;
        background-color: #f9f9f9;
        display: block;
        text-align: center;
    }
    div :hover{
    background-color:  #3498db;
        transition: 0.3s;
        color: white;
    }
    
</style>
</head>
<body>
	<h1>Bienvenido a la aplicación de gestión de empleados</h1>
   
    <div>
   		<a href="MainController">Mostrar lista de empleados</a>
    </div>
</body>
</html>