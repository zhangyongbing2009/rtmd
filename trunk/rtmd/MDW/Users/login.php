<?php
require ("../config/config.php");
require ("../mysql/connect.php");
 
if(isset($_SESSION['logged_in']) && $_SESSION['logged_in'] == 1) {	
	die('You are already logged in, '.$_SESSION['username'].'.');
	echo "<META HTTP-EQUIV='Refresh' CONTENT='2; URL=../index.php'>";
}


?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Login</title>
<link type="text/css" href="/<? echo $source_location; ?>/css/layout.css" rel="stylesheet" media="screen" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script language="javascript" href="/<? echo $source_location; ?>/js/functions.js" type="text/javascript"></script>

<?php

if (isset($_POST['submit'])) { // if form has been submitted
	// Check if a Lehigh user
	if (strpos($_POST['uname'],"@lehigh.edu")) {
		// Check if Lehigh password authenticates, otherwise bad password
		define("AD_SERVER",   "ldap://ad.lehigh.edu");		
		$ad = ldap_connect(AD_SERVER)
	        or die("<META HTTP-EQUIV='Refresh' CONTENT='2; URL=../index.php'></head><body><h1>Error</h1>Connection to Lehigh AD Failed!");
		ldap_set_option($ad, LDAP_OPT_PROTOCOL_VERSION, 3);
		$user = explode("@",$_POST['uname']);
		$user = $user[0] . "@ad.lehigh.edu";
		$bd = ldap_bind($ad,$user,$_POST['passwd'])
        	or die("<META HTTP-EQUIV='Refresh' CONTENT='2; URL=../index.php'></head><body><h1>Error</h1>Incorrect password and/or username.  Please try again.");			
		ldap_unbind($ad);		
		// If here, user is Authenticated								

		// Check if in RTMD Database and set SESSION variable
		$check = $db_object->query("SELECT Username FROM Users WHERE Username = '".$_POST['uname']."'");				
		if (DB::isError($check) || $check->numRows() == 0) {
			// If not in database, create a new entry getting Name and Email from LDAP
			define("LD_SERVER",   "ldap.lehigh.edu");
			$ld = ldap_connect(LD_SERVER)
		        or die("<META HTTP-EQUIV='Refresh' CONTENT='2; URL=../index.php'></head><body><h1>Error</h1>Connection to Lehigh LDAP Failed!");
			$lbd = ldap_bind($ld)
			 	or die("<META HTTP-EQUIV='Refresh' CONTENT='2; URL=../index.php'></head><body><h1>Error</h1>Connection to Lehigh LDAP Failed!");
			$lookfor = array("sn", "givenname", "mail");
			$sr=ldap_search($ld, "dc=lehigh,dc=edu","mail=".$_POST['uname'],$lookfor);
			$userinfo = ldap_get_entries($ld, $sr);
			ldap_unbind($ld);
		
			$insert = "INSERT INTO Users (
			Username, 
			Passwd, 			
			Email,
			Firstname,
			Lastname, 			 
			LastLogin) 
			VALUES (
			'".$_POST['uname']."', 
			'(lehigh user)', 			
			'".$_POST['uname']."',
			'".$userinfo[0]['givenname'][0]."', 
			'".$userinfo[0]['sn'][0]."',
			'Never')";

			$add_member = $db_object->query($insert);

			if (DB::isError($add_member)) {
				echo "<META HTTP-EQUIV='Refresh' CONTENT='2; URL=register.php'>";
				die($add_member->getMessage());
			}
		}
		
		// At this point there should be a Lehigh user in the RTMD database
		$check = $db_object->query("SELECT Username, Passwd, Projects, ProjectsAdmin FROM Users WHERE Username = '".$_POST['uname']."'");
		if (DB::isError($check) || $check->numRows() == 0) {				
			echo "<META HTTP-EQUIV='Refresh' CONTENT='2; URL=../index.php'></head><body><h1>Error</h1>That username does not exist in our database";
			die();
			
		}
		$info = $check->fetchRow();
		// if we get here username and password are correct, 
		//register session variables and set last login time.
		$_SESSION['logged_in'] = 1;
	
		$date = date('m d, Y');
		$update_login = $db_object->query("UPDATE Users SET LastLogin = '$date' WHERE Username = '".$_POST['uname']."'");
		
		$_SESSION['username'] = $_POST['uname'];
		$_SESSION['projects'] = $info['Projects'];
		$_SESSION['projectsadmin'] = $info['ProjectsAdmin'];
		$db_object->disconnect();						
	}
	else {
		// authenticate non-lehigh user
		if (!get_magic_quotes_gpc()) {
			$_POST['uname'] = addslashes($_POST['uname']);
		}
	
		$check = $db_object->query("SELECT Username, Passwd, Projects, ProjectsAdmin FROM Users WHERE Username = '".$_POST['uname']."'");
		if (DB::isError($check) || $check->numRows() == 0) {				
			echo "<META HTTP-EQUIV='Refresh' CONTENT='2; URL=../index.php'></head><body><h1>Error</h1>That username does not exist in our database";
			die();
			
		}
		$info = $check->fetchRow();
	
		// check passwords match
		$_POST['passwd'] = stripslashes($_POST['passwd']);
		$info['Passwd'] = stripslashes($info['Passwd']);
		$_POST['passwd'] = md5($_POST['passwd']);
	
		if ($_POST['passwd'] != $info['Passwd']) {				
			echo "<META HTTP-EQUIV='Refresh' CONTENT='2; URL=../index.php'></head><body><h1>Error</h1>Incorrect password and/or username.  Please try again.";
			$_SESSION['logged_in'] = 0;
			die();
		}
	
		// if we get here username and password are correct, 
		//register session variables and set last login time.
		$_SESSION['logged_in'] = 1;
	
		$date = date('m d, Y');
		$update_login = $db_object->query("UPDATE Users SET LastLogin = '$date' WHERE Username = '".$_POST['uname']."'");
	        
		$_POST['uname'] = stripslashes($_POST['uname']);
		$_SESSION['username'] = $_POST['uname'];
		//$_SESSION['password'] = $_POST['passwd'];
		$_SESSION['projects'] = $info['Projects'];
		$_SESSION['projectsadmin'] = $info['ProjectsAdmin'];
		$db_object->disconnect();
	}
	
	// Shared object navigation tracker 		
	$_SESSION['SharedObjectNav'][] = array();
	
	// Breadcrumb tracker
	$_SESSION['Breadcrumbs'][] = array();
?>
<META HTTP-EQUIV='Refresh' CONTENT='2; URL=../index.php'>
</head>
<body>
<h1>Logged in</h1>
<p>Welcome back <?php echo $_SESSION['username']; ?>, you are logged in.  Redirecting to Welcome page.</p>

<?php

} else {	// if form hasn't been submitted

?>
</head>
<body>
<h1>Login</h1>
 <form action="<?php echo $_SERVER['PHP_SELF']?>" method="post" onsubmit="return (isNotEmpty(uname) && isNotEmpty(passwd));">
  <table align="center" border="1" cellspacing="0" cellpadding="3">
   <tr><td>Username:</td><td><input type="text" name="uname" maxlength="40" /></td></tr>
   <tr><td>Password:</td><td><input type="password" name="passwd" maxlength="50" /></td></tr>
   <tr><td><input type="submit" name="submit" class="buttons" value="Login" /></td><td><input type='button' name='Cancel' class='buttons' value='Cancel' onclick="window.location.href='../index.php'" /></td></tr>
  </table>  
  <center><p><a href='Users/forgot.php'>Forgot password?</a></center></p>
 </form>
<?php
}
?>
</body>
</html>