<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="utf-8">
	<title>Welcome to CodeIgniter</title>

	<style type="text/css">

	
	</style>
</head>
<body>

<div id="container">
	<h1>Welcome to Patients!</h1>

	<?php
		foreach($patients as $patient){
			echo "$patient->FirstName <br>";
			echo "$patient->LastName <br>";
		}
		?>

	
</div>

</body>
</html>