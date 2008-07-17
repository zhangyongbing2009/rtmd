<?php
require ("../config/config.php");
require ("../mysql/connect.php");
?>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Project Membership Request</title>
<link type="text/css" href="../css/layout.css" rel="stylesheet" media="screen" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
</head>
<body>
<h1>Project Membership Request</h1>

<?php
if (isset($_GET['Project'])) {
	echo "<META HTTP-EQUIV='Refresh' CONTENT='2; URL=../index.php'>";	
		
	if (isset($_SESSION['username'])) {										
		// Get admin email
		$adminresult = mysql_query("SELECT Email FROM Users WHERE Username=\"admin\"");
		$adminrow = mysql_fetch_array($adminresult);		
		$emailto = $adminrow['Email'];
				
		// Get user's id
		$requesting_user = $_SESSION['username'];
		
		// Get request 		
		$requestedproject = escapeshellarg("\n" . $_GET['Project']);		
					
		// Send email to admin		
		$subject = "Metadata Web Project membership request";
		$body = "User $requesting_user requests membership to project $requestedproject";
		mail($emailto,$subject,$body);		
		echo "Your request has been submitted to the administrator.";					
	}

	// User not logged in
	else 
		echo "Not logged in";		
	die();		
}
 
?> 
</body>
</html>