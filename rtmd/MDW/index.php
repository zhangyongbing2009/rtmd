<?php
// Required externals
require ("config/config.php");
require ("mysql/connect.php");
?>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link type="text/css" href="css/layout.css" rel="stylesheet" media="screen" />
<title>Metadata Web</title>
<link rel="shortcut icon" href="images/favicon.ico" type="image/x-icon" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script language="javascript" src="js/functions.js" type="text/javascript"></script>
<script language="javascript" src="js/userchange.js" type="text/javascript"></script>
</head>
<body>
<div id="container">
 <div id="banner" ><a href="index.php"><img src="images/header.jpg" alt="" border="0" /></a>
  <div id="addressbar"><h1>Welcome >> <a href="switchboard.php?UniqueName=Project&List=1">Go To Projects</a></h1></div>
  <div align="right" id="loginbar"><h3><?php require ("php/showlogin.php"); ?></h3></div>  
 </div>
 <div id="content">
  <h2>Welcome</h2>
  <p class="indented">
  Welcome to Version 1.4 of the Metadata Web.  Lehigh users can use their existing Lehigh email to log in without registering.  If you do not have a Lehigh email, you need to register by clicking the Not Registered link on the left.  
  Once logged in, click on "Go To Projects" to view projects that you are a member of.
  </p>
  <p class="indented">
  Please see the section below on requesting view or admin membership to existing projects.   
  </p>      
  <?php 
  if (!isset($_SESSION['logged_in']) || $_SESSION['logged_in'] == 0) {
   echo "
   <p />		   
   <form action='Users/login.php' method='post' onsubmit='return \"(isNotEmpty(uname) && isNotEmpty(passwd));\"'>
   	<h4 style=\"width:180px\">Login</h4>
    <table border='1' cellspacing='0' cellpadding='3'>
     <tr><td>Username:<br>(or Lehigh email):</td></tr><tr><td><input type='text' name='uname' maxlength='40' /></td></tr>
     <tr><td>Password:</td></tr><tr><td><input type='password' name='passwd' maxlength='50' /></td></tr>
     <tr><td><input type='submit' name='submit' class='buttons' value='Login' /></td></tr>
    </table>
    <p><a href='Users/forgot.php'>Forgot password?</a></p>		    
    <p><a href='Users/register.php'>Not Registered?</a> (Lehigh users do not have to register)</p>
   </form>";   
  }
  else {
   echo      
    "<p class=\"indented\"><a href=\"switchboard.php?UniqueName=Project&List=1\"><u>Go To Projects</u></a></p><br />\n".
    "<div style=\"margin-left:10px;\">"; echo "</div>\n";
  }
  ?>  		
  <?php  
   // Administration section 
   if (isset($_SESSION['logged_in']) && $_SESSION['logged_in'] == 1) {
    echo "<h2>Administration</h2>";
    // If a lehigh user, don't allow password changes here
    if (!strpos($_SESSION['username'],"@lehigh.edu")) 
    	echo "<p class=\"indented\"><a href=\"Users/changepassword.php\">Change Password</a></p>";
    
   	if ($_SESSION['username'] == 'admin') {   	     		
   		echo "<p /><b>Projects</b><table border='1' cellspacing='0' cellpadding='3'><tr><th>Project</th><th>Project ID</th></tr>";
    	$Projects = mysql_query("SELECT * FROM BasicClass WHERE ClassName=\"Project\"");
    	while ($ProjectInfo = mysql_fetch_array($Projects,MYSQL_ASSOC)) {
     	echo "<tr><td>".$ProjectInfo{'Name'}."</td><td>".$ProjectInfo{'ID'}."</td></tr>";
    	}
    	echo "<form name='users' action='' method='get'></table><p />";    
    	echo "<b>Users</b><table border='1' cellspacing='0' cellpadding='3'><tr><th>User</th><th>Project Member</th><th>Project Admin</th><th>&nbsp;</th><th>&nbsp;</th></tr>";
    	$Users = mysql_query("SELECT * FROM Users");
    	while ($UserInfo = mysql_fetch_array($Users,MYSQL_ASSOC)) {
     	echo "<tr><td>".$UserInfo{'Username'}."<br><a href=\"mailto:".$UserInfo{'Email'}."\">".$UserInfo{'Firstname'}." ".$UserInfo{'Lastname'}."</a></td>" .
 			 "    <td><input type='text' name='' id='".$UserInfo{'Username'}."' value='". $UserInfo{'Projects'} ."' /> </td>" .
	 		 "    <td><input type='text' name='' id='".$UserInfo{'Username'}."Admin' value='". $UserInfo{'ProjectsAdmin'} ."' /></td>" .
 			 "    <td><input type='button' class='buttons' value='Update' name='' onclick=\"sendRequest('". $UserInfo{'Username'} ."','". $UserInfo{'Username'} ."Admin');\" /></td>" ;
 			 if ($UserInfo{'Username'} != "admin")
 			 	echo "    <td><input type='button' class='buttons' value='Delete' name='' onclick=\"confirmdeleteuser('". $UserInfo{'Username'} ."')\" /></td>";
 			 else
 			 	echo "    <td>&nbsp;</td>";
 			 echo "</tr>";
    	}
    	echo "</table>";
   	}
   }          		
  ?>
 </div>
 <?php include "php/footer.php"; ?>
 </div>
</div>
</body>
</html>


