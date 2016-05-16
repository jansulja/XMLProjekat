<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet" href="http://code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min.css" />
	<script src="http://code.jquery.com/jquery-2.0.1.min.js"></script>
	<script src="http://code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Dodavanje Gradjanina</title>
</head>
<body>
	<div data-role="page" id="page-1" data-theme="a">
		
		<div data-role="header" data-theme="a">
			<h1>Dodavanje Gradjanina</h1>
			<div class="ui-grid-b">
				<div class="ui-block-a"></div>
				<div class="ui-block-b"></div>
				<div class="ui-block-c"></div>
				<div class="ui-block-a"></div>
				<div class="ui-block-b"><div data-role="content">
			
			<form id="form-1" action="${pageContext.request.contextPath}/api/gradjanin/create" method="post">
			
				<label for="input-1">Ime:</label>
				<input id="input-1" name="ime" autofocus="true" required="true" type="text"/>
				<label for="input-2">Prezime:</label>
				<input id="input-2" name="prezime" required="true" type="text"/>
				<label for="input-3">Broj Licne Karte:</label>
				<input id="input-3" name="brojLicne" required="true" type="text"/>
				<label for="input-4">Datum Rodjenja:</label>
				<input id="input-4" name="datumrodjenja" required="true" type="text"/>
				<label for="input-5">Pol:</label>
				<input id="input-5" name="pol" required="true" type="text"/>
				<label for="input-6">JMBG:</label>
				<input id="input-6" name="JMBG" required="true" type="text"/>
				<label for="input-7">Mesto Rodjenja:</label>
				<input id="input-7" name="mestoRodjenja" required="true" type="text"/>
				<label for="input-8">Opstina Rodjenja:</label>
				<input id="input-8" name="opstinaRodjenja" required="true" type="text"/>
				<label for="input-9">Drzava:</label>
				<input id="input-9" name="drzava" required="true" type="text"/>
				<label for="input-10">Broj telefona:</label>
				<input id="input-10" name="brojTelefona" required="true" type="text"/>
				<label for="input-11">E-mail:</label>
				<input id="input-11" name="email" required="true" type="text"/>
				
				<div class="ui-input-btn ui-btn ui-corner-all ui-btn-b">
					Dodaj Gradjanina
					<input data-enhanced="true" type="submit" value="Dodaj Gradjanina" id="button-1"/>
				</div>
			</form>
		</div></div>
				<div class="ui-block-c"></div>
				<div class="ui-block-a"></div>
				<div class="ui-block-b"></div>
				<div class="ui-block-c"></div>
			</div>
		</div>
		
		
		
		
		
	</div>

</body>
</html>