<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<jsp:useBean id="gradjanin" type="java.util.List" scope="request"/>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Lista gradjanina</title>
	</head>
<body>
	<table class="listaGradjaninaTabela">
	
	<thead>
				<tr>
					<th>Id</th>
					<th>Ime</th>
					<th>Prezime</th>
					<th>Broj licne</th>
					<th>Datum rodjenja</th>
					<th>Pol</th>
					<th>JMBG</th>
					<th>Mesto rodjenja</th>
					<th>OpstinaRodjenja</th>
					<th>Drzava</th>
					<th>Broj telefona</th>
					<th>E-mail</th>
					
				</tr>
			</thead>
	
	<tbody>
	<c:forEach items="${gradjanin}" var="gradjanin">
	<tr>
	<td>${gradjanin.id}</td>
	<td>${gradjanin.ime}</td>
	<td>${gradjanin.prezime}</td>
	<td>${gradjanin.brojlicne}</td>
	<td>${gradjanin.datumrodjenja}</td>
	<td>${gradjanin.pol}</td>
	<td>${gradjanin.JMBG}</td>
	<td>${gradjanin.mestoRodjenja}</td>
	<td>${gradjanin.opstinaRodjenja}</td>
	<td>${gradjanin.drzava}</td>
	<td>${gradjanin.brojTelefona}</td>
	<td>${gradjanin.email}</td>
	</tr>
	</c:forEach>
	</tbody>
	</table>
</body>
</html>