<?php
require ("../config/config.php");
require ("../mysql/connect.php");
require ("../php/checklogin.php");
?>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Change Password Form</title>
<link type="text/css" href="/<? echo $source_location; ?>/css/layout.css" rel="stylesheet" media="screen" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script language="javascript" href="/<? echo $source_location; ?>/js/functions.js" type="text/javascript"></script>
</head>
<body>
<h1>Change Password Form</h1>

<?php
if (isset($_POST['Change'])) {
	echo "<META HTTP-EQUIV='Refresh' CONTENT='2; URL=../index.php'>";
	
	$username = $_SESSION['username'];
	$result = mysql_query("SELECT ID,Passwd FROM Users WHERE Username=\"$username\"");	
	$count = mysql_num_rows($result);
	$rows=mysql_fetch_array($result);
			
	$currentpwd_md5 = md5($_POST['currentpwd']);
	if ($rows['Passwd'] != $currentpwd_md5) {
		die ("Incorrect password");
	}
	
	if($count==1) {
		// Update password
		$new_password = strip_tags($_POST['newpwd']);
		$new_password_md5 = md5($new_password);
		mysql_query("UPDATE Users SET Passwd=\"$new_password_md5\" WHERE ID=".$rows['ID']);		
		echo "Your password has been updated.";										
	}

	// else if $count not equal 1
	else 
		echo "There was an error retrieving your account.  Please contact the webmaster.";
	
	die();		
}
 
?>  
  <table align="center" border="1" cellspacing="0" cellpadding="3">  
   <form name="change" method="post" action="changepassword.php" onSubmit="return checkpasswords();">
    <tr><td>Current Password:</td><td> <input name="currentpwd" type="password" size="25" /></tr></td>
    <tr><td>New Password:</td><td> <input name="newpwd" type="password" size="25" /></tr></td>
    <tr><td>Retype New Password:</td><td> <input name="retypenewpwd" type="password" size="25" /></tr></td>
    <tr><td></td><td><input type="submit" name="Change" class="buttons" value="Submit" />
    <input type="button" name="Cancel" class="buttons" value="Cancel" onclick="javascript:history.go(-1)" /></tr></td>
   </form>  
 </table>
</body>
</html>