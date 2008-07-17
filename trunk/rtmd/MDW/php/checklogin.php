<?php

if (!isset($_SESSION['logged_in']) || $_SESSION['logged_in'] == 0) {
	echo "<META HTTP-EQUIV='Refresh' CONTENT='2; URL=/".$source_location."/index.php'></head><body>";
	die('You must <a href="index.php">log in</a> before viewing this page');
}
?>