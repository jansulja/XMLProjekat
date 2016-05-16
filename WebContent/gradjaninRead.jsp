<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<jsp:useBean id="gradjanin" type="java.util.List" scope="request"/>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1" charset="UTF-8">
		<link rel="stylesheet" href="http://code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min.css" />
		<script src="http://code.jquery.com/jquery-2.0.1.min.js"></script>
		<script src="http://code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min.js"></script>
<!-- 		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> -->
		<title>Lista gradjana</title>
	</head>
<body>

	<div class="ui-body-b ui-body">
		<div data-role="navbar" id="navbar-1">
			<ul>
				<li><a href="${pageContext.request.contextPath}/api/gradjanin/create">Dodavanje</a></li>
				
			</ul>
		</div>
	</div>

	<form>
	<input data-type="search" id="searchForTable-1"/>
	</form>
	<table data-role="table" id="table-1" data-filter="true" 
	        data-input="#searchForTable-1" class="ui-body-a 
	            ui-responsive">
		<thead>
			<tr class="ui-bar-a">
				
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
	<td><a href="#">Obrisi</a></td>
	<td><a href="#">Izmeni</a></td>
	</tr>
	</c:forEach>
	</tbody>
	</table>


<!-- 	<table class="listaGradjaninaTabela"> -->
	
<!-- 	<thead> -->
<!-- 				<tr> -->
<!-- 					<th>Id</th> -->
<!-- 					<th>Ime</th> -->
<!-- 					<th>Prezime</th> -->
<!-- 					<th>Broj licne</th> -->
<!-- 					<th>Datum rodjenja</th> -->
<!-- 					<th>Pol</th> -->
<!-- 					<th>JMBG</th> -->
<!-- 					<th>Mesto rodjenja</th> -->
<!-- 					<th>OpstinaRodjenja</th> -->
<!-- 					<th>Drzava</th> -->
<!-- 					<th>Broj telefona</th> -->
<!-- 					<th>E-mail</th> -->
					
<!-- 				</tr> -->
<!-- 			</thead> -->
	
<!-- 	<tbody> -->
<%-- 	<c:forEach items="${gradjanin}" var="gradjanin"> --%>
<!-- 	<tr> -->
<%-- 	<td>${gradjanin.id}</td> --%>
<%-- 	<td>${gradjanin.ime}</td> --%>
<%-- 	<td>${gradjanin.prezime}</td> --%>
<%-- 	<td>${gradjanin.brojlicne}</td> --%>
<%-- 	<td>${gradjanin.datumrodjenja}</td> --%>
<%-- 	<td>${gradjanin.pol}</td> --%>
<%-- 	<td>${gradjanin.JMBG}</td> --%>
<%-- 	<td>${gradjanin.mestoRodjenja}</td> --%>
<%-- 	<td>${gradjanin.opstinaRodjenja}</td> --%>
<%-- 	<td>${gradjanin.drzava}</td> --%>
<%-- 	<td>${gradjanin.brojTelefona}</td> --%>
<%-- 	<td>${gradjanin.email}</td> --%>
<!-- 	</tr> -->
<%-- 	</c:forEach> --%>
<!-- 	</tbody> -->
<!-- 	</table> -->
</body>
</html>