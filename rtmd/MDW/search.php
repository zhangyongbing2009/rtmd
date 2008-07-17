<?php
// Required externals
require ("config/config.php");
require ("mysql/connect.php");
require ("php/checklogin.php");

//// View Page
if (isset($_GET['Search'])) {					   
	$searchval = $_GET['term'];
	echo 
	"<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n".
	"<html xmlns=\"http://www.w3.org/1999/xhtml\">\n".
	"<head>\n".
	"<link type=\"text/css\" href=\"/".$source_location."/css/layout.css\" rel=\"stylesheet\" media=\"screen\" />\n".
	"<title>Search: ".$_GET['term']."</title>\n".
	"<link rel=\"shortcut icon\" href=\"/".$source_location."/images/favicon.ico\" type=\"image/x-icon\" />\n".
	"<script language=\"javascript\" src=\"/".$source_location."/js/functions.js\" type=\"text/javascript\"></script>\n".
	"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n".
	"</head>\n".
	"<body>\n".
 	"<div id=\"container\">\n".
 	" <div id=\"banner\"><a href=\"/".$source_location."\"><img src=\"/".$source_location."/images/header.jpg\" alt=\"\" border=\"0\" /></a>\n".
    "  <div id=\"addressbar\"><h1>Search</h1></div>\n".
    "  <div align=\"right\" id=\"loginbar\"><h3>";require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/php/showlogin.php");echo "</h3></div>\n".
    "  <div id=\"breadcrumbbar\"><h1><a href=\"switchboard.php?UniqueName=Project&List=1\">Projects</a> >></h1></div>";
    echo 
 	" </div>\n".  	
    " <div id=\"content\">\n".    
    "  <h2>Searched for ".$_GET['term']."</h2>\n".
    "  <table border=\"0\">\n".   
	"<p />";	
    // Search  	
   	// Get all tables
   	$rs = mysql_query( "SHOW TABLES" );
    while( $table = mysql_fetch_array( $rs ) ) {
    	$tblName = $table[ 0 ];
    	// Ignore Users and SharedObjectLink
    	if ($tblName != "Users" || $tblName != "SharedObjectLink") {
	        $rs2 = mysql_query( "DESCRIBE $tblName");
	        $tblFields = array();
	        // Get all fields in the table
	        while( $fields = mysql_fetch_array( $rs2,MYSQL_BOTH ) ) {	        	
	        	if ($fields[0] != "ID" && $fields[0] != "ClassName" && $fields[0] != "ParentClassNameID" && $fields[0] != "SessionID" && $fields[0] != "Project")	        	
	        		$tblFields[] = $fields[0];	        	  		        		      
	        }	    	        
	        // Get the ID and Name from the table if found
	        $sql = "SELECT ID,Name,ClassName,ParentClassNameID FROM $tblName WHERE (" . join( " LIKE '%$searchval%' OR ", $tblFields ) . " LIKE '%$searchval%') AND (Project=".$_SESSION['ProjectID']." OR Project=0)";	        	        
	        $query = mysql_query( $sql );
	        if ($query) {	        
		        while( $result = mysql_fetch_array( $query ) ) {	        	
		        	$p_name = ereg_replace("([0-9])", "", $result[3]);	        	
		        	$p_id = ereg_replace("([A-Za-z])", "", $result[3]);
		        	$sql = "SELECT Name FROM BasicClass WHERE ClassName=\"$p_name\" AND ID=\"$p_id\"";	        	
		        	$pquery = mysql_query( $sql );	
		        	$presult = mysql_fetch_array( $pquery );
		        	if ($presult != "")
				    	echo "<tr valign=top><td>$presult[0]: <a href=\"/".$source_location."/switchboard.php?UniqueName=$result[2]&ClassID=$result[0]&View=1\"><b>$result[1]</b></a></td><tr>";
			    	else
			    		echo "<tr valign=top><td><a href=\"/".$source_location."/switchboard.php?UniqueName=$result[2]&ClassID=$result[0]&View=1\"><b>$result[1]</b></a></td><tr>";
				    	    			
		        }
		        mysql_free_result( $query );
		        mysql_free_result( $rs2 );
	        }
    	}
	}
    mysql_free_result( $rs );   				
	
    echo 
    "  </table>\n".
    "  <p />\n";        
    echo
    "  <form name=\"Navigate\" action=\"\">\n".   
    "   <input type=\"button\" class=\"buttons\" value=\"<-\" onclick=window.location.href=\"/".$source_location."/switchboard.php?UniqueName=".$_GET['UniqueName']."&View=1&ClassID=".$_GET['ClassID']."\" /> Back To Project\n".   
    "  </form>\n".
 	" </div>\n";     	 	  
 	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/php/footer.php");
 	echo 
 	"</div>\n".  	
	"</body>\n".
	"</html>\n";    	
} /////////////////////
?>