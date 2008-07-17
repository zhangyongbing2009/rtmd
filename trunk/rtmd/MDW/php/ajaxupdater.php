<?php
// Required externals
require ("../config/config.php");
require ("../mysql/connect.php");
require ("checklogin.php");	
		
// Updates the database from the AJAX InPlace Editor quickly
if (isset($_POST['TableName']) && isset($_POST['Item']) && isset($_POST['Value']) && isset($_POST['ID'])) {
	
	// Get variables
	$TableName = $_POST['TableName'];
	$Item = $_POST['Item'];
	$Value = $_POST['Value'];
	$ID = $_POST['ID'];
	
 	// Begin the insert query 
 	$sql = "UPDATE $TableName SET $Item='$Value' WHERE ID='$ID'";
 	
 	// Perform the query 	
 	mysql_query($sql); 
 	
 	// Show the value
 	if ($Value == '')
 		echo "(click to edit)";
 	else
 		echo $Value;
}
 	  
?>
