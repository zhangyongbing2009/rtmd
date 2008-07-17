<?php
require ("../config/config.php");

// Change these three variables
$admin_email = "your@email";
$admin_firstname = "your first name";
$admin_lastname = "your last name";


/********************************************/
//require the PEAR::DB classes.
require_once 'DB.php';

// Connect to the DB
$mysql = mysql_connect($hostname, $username, $password)
	or die("Unable to connect to MySQL");

// Create the DB	
$sql = "CREATE database `$dbname`;";
mysql_query($sql);

// Connect to the DB
$db = mysql_select_db($dbname,$mysql)
	or die("Could not select". $dbname);	
$datasource = "mysql://$username:$password@$hostname/$dbname";

$db_object = DB::connect($datasource);
if(DB::isError($db_object)) {
	die($db_object->getMessage());
}
$db_object->setFetchMode(DB_FETCHMODE_ASSOC);


$sql = "CREATE TABLE `BasicClass` (
  `ID` int(10) unsigned NOT NULL auto_increment,
  `Name` text,
  `Text1` text,
  `Text2` text,
  `Text3` text,
  `Text4` text,
  `Text5` text,
  `Text6` text,
  `Text7` text,
  `Text8` text,
  `Text9` text,
  `DateTime1` datetime default '0000-00-00 00:00:00',
  `DateTime2` datetime default '0000-00-00 00:00:00',
  `ClassName` varchar(100) default NULL,
  `ParentClassNameID` varchar(100) default NULL,
  `SharedName` varchar(100) default NULL,
  `Project` int(10) unsigned default NULL,
  `isShared` int(1) unsigned default NULL,
  `OwningProject` int(10) unsigned default NULL,
  `Ranking` int(11) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1;";
mysql_query($sql);

$sql = "CREATE TABLE `Files` (
  `ID` int(10) unsigned NOT NULL auto_increment,
  `Name` text,
  `Filename` varchar(100) default NULL,
  `Filesize` varchar(100) default NULL,
  `Text1` text,
  `Text2` text,
  `Text3` text,
  `Text4` text,
  `Text5` text,
  `SessionID` varchar(100) default NULL,
  `ClassName` varchar(100) default NULL,
  `ParentClassNameID` varchar(100) default NULL,
  `Project` int(10) unsigned default NULL,
  `Ranking` int(10) unsigned default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1;";
mysql_query($sql);

$sql = "CREATE TABLE `SharedObjectLink` (
  `SharedObjectLinkID` int(10) unsigned NOT NULL auto_increment,
  `ParentClassNameID` varchar(100) default NULL,
  `SharedObjectClass` varchar(100) default NULL,
  `SharedObjectID` int(10) unsigned default NULL,
  `Ranking` int(10) unsigned default NULL,
  PRIMARY KEY  (`SharedObjectLinkID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1;";
mysql_query($sql);
		
$sql = "CREATE TABLE `Users` (
  `ID` int(10) NOT NULL auto_increment,
  `Username` varchar(40) default NULL,
  `Passwd` varchar(50) default NULL,
  `Email` varchar(100) default NULL,
  `LastLogin` varchar(20) default NULL,
  `Firstname` varchar(50) default NULL,
  `Lastname` varchar(50) default NULL,
  `Projects` varchar(100) default NULL,
  `ProjectsAdmin` varchar(100) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;
";
mysql_query($sql);

$sql = "INSERT INTO Users (
			Username, 
			Passwd, 			
			Email,
			Firstname,
			Lastname, 			 
			LastLogin) 
			VALUES (
			'admin', 
			'21232f297a57a5a743894a0e4a801fc3', 			
			'$admin_email',
			'$admin_firstname', 
			'$admin_lastname',  
			'Never')";
mysql_query($sql);			

// Close the session
require ("../mysql/close.php");
?>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Register a Lehigh Data Model Account</title>
<link type="text/css" href="/<? echo $source_location; ?>/css/layout.css" rel="stylesheet" media="screen" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
</head>
<body>
<h1>Done</h1>
<p>Delete your createmysql.php file now.</p>
<p>The user is <b>admin</b> and the password is <b>admin</b></p>
<p><a href="http://<?echo $baseweburl."/".$source_location; ?>">Click here to go to the application now</a></p>
</body>
</head>