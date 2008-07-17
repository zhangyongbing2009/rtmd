<?php
require ("../config/config.php");
require ("../mysql/connect.php");
require ("../php/checklogin.php");

// kill session variables
$_SESSION = array(); // reset session array
session_destroy();   // destroy session.
header('Location: ../index.php');

?>