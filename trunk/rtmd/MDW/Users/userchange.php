<?php
// Change user's membership to projects
require ("../config/config.php");
require ("../mysql/connect.php");

$Username = $_GET['Username'];
$Projects = $_GET['Projects'];
$ProjectsAdmin = $_GET['ProjectsAdmin'];

// Update the user info
mysql_query("UPDATE Users SET Projects='". $Projects ."', ProjectsAdmin='". $ProjectsAdmin ."' WHERE Username='" . $Username ."'");

// Update Session if its the admin
if ($Username == "admin") {
	$_SESSION['projects'] = $Projects;
	$_SESSION['projectsadmin'] = $ProjectsAdmin;
}

require "../mysql/close.php";
?>