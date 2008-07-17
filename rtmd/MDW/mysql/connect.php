<?php
// Supposidly improves performance
ob_start();

// Start the session
session_start();

//require the PEAR::DB classes.
require_once 'DB.php';

// Connect to the DB
$mysql = mysql_connect($hostname, $username, $password)
	or die("Unable to connect to MySQL");
$db = mysql_select_db($dbname,$mysql)
	or die("Could not select". $dbname);	
$datasource = "mysql://$username:$password@$hostname/$dbname";

$db_object = DB::connect($datasource);
if(DB::isError($db_object)) {
	die($db_object->getMessage());
}
$db_object->setFetchMode(DB_FETCHMODE_ASSOC);

?>
