<?php
require ("../config/config.php");
require ("../mysql/connect.php");
?>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Forgot Password Form</title>
<link type="text/css" href="/<? echo $source_location; ?>/css/layout.css" rel="stylesheet" media="screen" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script language="javascript" href="/<? echo $source_location; ?>/js/functions.js" type="text/javascript"></script>
</head>
<body>
<h1>Forgot Password Form</h1>

<?php
if (isset($_POST['Forgot'])) {
	echo "<META HTTP-EQUIV='Refresh' CONTENT='2; URL=../index.php'>";
	
	$random_password = md5(uniqid(rand()));
	$new_password = substr($random_password, 0, 8);	
	$new_password_md5 = md5($new_password);	
	
	$emailto = $_POST['email_to'];
	$result = mysql_query("SELECT ID,Passwd FROM Users WHERE Email=\"$emailto\"");	
	$count = mysql_num_rows($result);
	$rows=mysql_fetch_array($result);
	
	if($count==1) {
		// Update password
		mysql_query("UPDATE Users SET Passwd=\"$new_password_md5\" WHERE ID=".$rows['ID']);		
								
		// send email		
		$subject = "Lehigh Metadata Web Password Reset";
		$body = "Your new password is $new_password";
		mail($emailto,$subject,$body);
		echo "Your new password has been sent to your email address.";					
	}

	// else if $count not equal 1
	else 
		echo "Your email is not in our database";	
		
	die();		
}
 
?>
 <table align="center" border="1" cellspacing="0" cellpadding="3">
  <tr><td>
   <strong>Enter your email to have your password sent: (Only for non-Lehigh usernames)</strong></td></tr>
  <tr><td>
   <form name="forgot" method="post" action="">
    <input name="email_to" type="text" id="mail_to" size="25" />
    <input type="submit" name="Forgot" class="buttons" value="Submit" />
    <input type="button" name="Cancel" class="buttons" value="Cancel" onclick="javascript:history.go(-1)" />
   </form></td>
  </tr>
 </table>
</body>
</html>