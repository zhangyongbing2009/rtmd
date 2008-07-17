<?php
require ("../config/config.php");
require ("../mysql/connect.php");
?>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Register a Lehigh Data Model Account</title>
<link type="text/css" href="/<? echo $source_location; ?>/css/layout.css" rel="stylesheet" media="screen" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script language="javascript" href="/<? echo $source_location; ?>/js/functions.js" type="text/javascript"></script>

<?php // if form has been submitted
if (isset($_POST['submit'])) { 	
	// check if username exists in database.
	if (!get_magic_quotes_gpc()) {
		$_POST['uname'] = addslashes($_POST['uname']);
	}
	$name_check = $db_object->query("SELECT Username FROM Users WHERE Username = '".$_POST['uname']."'");	
	if (DB::isError($name_check)) {
		die($name_check->getMessage());
	}
	$name_checkk = $name_check->numRows();
	if ($name_checkk != 0) {
		echo "<META HTTP-EQUIV='Refresh' CONTENT='2; URL=register.php'></head><body>";	
		die('Sorry, the username: <strong>'.$_POST['uname'].'</strong> is already taken, please pick another one.');
	}
	
	// check if email exists in database.
	if (!get_magic_quotes_gpc()) {
		$_POST['email'] = addslashes($_POST['email']);
	}
	$email_check = $db_object->query("SELECT Email FROM Users WHERE Email = '".$_POST['email']."'");	
	if (DB::isError($email_check)) {
		die($email_check->getMessage());
	}
	$email_checkk = $email_check->numRows();
	if ($email_checkk != 0) {
		echo "<META HTTP-EQUIV='Refresh' CONTENT='2; URL=register.php'></head><body>";	
		die('Sorry, the email: <strong>'.$_POST['email'].'</strong> is already in use.');
	}

	// check passwords match
	if ($_POST['passwd'] != $_POST['passwd_again']) {
		echo "<META HTTP-EQUIV='Refresh' CONTENT='2; URL=register.php'></head><body>";
		die('Passwords did not match.');
	}

	// check e-mail format
	if (!preg_match("/.*@.*..*/", $_POST['email']) | preg_match("/(<|>)/", $_POST['email'])) {
		echo "<META HTTP-EQUIV='Refresh' CONTENT='2; URL=register.php'></head><body>";
		die('Invalid e-mail address.');
	}
	// no HTML tags in username, name, password
	$_POST['uname'] = strip_tags($_POST['uname']);
	$_POST['passwd'] = strip_tags($_POST['passwd']);
	$_POST['fname'] = strip_tags($_POST['fname']);
	$_POST['lname'] = strip_tags($_POST['lname']);
	

	// now add them to the database
	// encrypt password
    $_POST['passwd'] = md5($_POST['passwd']);

	if (!get_magic_quotes_gpc()) {
		$_POST['passwd'] = addslashes($_POST['passwd']);
		$_POST['email'] = addslashes($_POST['email']);
	}

	$insert = "INSERT INTO Users (
			Username, 
			Passwd, 			
			Email,
			Firstname,
			Lastname, 			 
			LastLogin) 
			VALUES (
			'".$_POST['uname']."', 
			'".$_POST['passwd']."', 			
			'".$_POST['email']."',
			'".$_POST['fname']."', 
			'".$_POST['lname']."',  
			'Never')";

	$add_member = $db_object->query($insert);

	if (DB::isError($add_member)) {
		echo "<META HTTP-EQUIV='Refresh' CONTENT='2; URL=register.php'>";
		die($add_member->getMessage());
	}
	$db_object->disconnect();
?>

</head>
<body>
<h1>Registered</h1>
<p>Thank you, <?php echo $_POST['fname'] ." ". $_POST['lname']; ?>, your information has been added to the database, you may now <strong><a href="../index.php" title="Login">log in</a></strong>.</p>

<?php
} else {	// Show registration form
?>
</head>
<body>
<h1>Register</h1>
Lehigh users do not have to register.  You can use your Lehigh email to log in.
<form action="<?php echo $_SERVER['PHP_SELF']; ?>" method="post" onsubmit="return (isNotEmpty(uname) && isNotEmpty(passwd) && isNotEmpty(email) && isNotEmpty(fname) && isNotEmpty(lname));">
 <table align="center" border="1" cellspacing="0" cellpadding="3">
  <tr><td>Username:</td><td><input type="text" name="uname" maxlength="40" /></td></tr>
  <tr><td>Password:</td><td><input type="password" name="passwd" maxlength="50" /></td></tr>
  <tr><td>Confirm Password:</td><td><input type="password" name="passwd_again" maxlength="50" /></td></tr>
  <tr><td>E-Mail:</td><td><input type="text" name="email" maxlength="100" /></td></tr> 
  <tr><td>First Name:</td><td><input type="text" name="fname" maxlength="50" /></td></tr>
  <tr><td>Last Name:</td><td><input type="text" name="lname" maxlength="50" /></td></tr> 
  <tr><td><input type="submit" name="submit" class='buttons' value="Register" /></td><td><input type='button' name='Cancel' class='buttons' value='Cancel' onclick="window.location.href='../index.php'" /></td></tr>
 </table>
</form>

<?php
}
?>
</body>
</html>