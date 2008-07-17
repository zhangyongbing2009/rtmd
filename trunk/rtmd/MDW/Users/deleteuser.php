<?php
// Requirements
require ("../config/config.php");
require ("../mysql/connect.php");

if ($_SESSION['username'] == 'admin') {
	// Get username
	$Username = $_GET['Username'];
	
	// Update the user info
	mysql_query("DELETE FROM Users WHERE Username='$Username'");	
}	

// Close the session
require ("../mysql/close.php");
	
echo "<META HTTP-EQUIV='Refresh' CONTENT='0; URL=../index.php'>";

?>
