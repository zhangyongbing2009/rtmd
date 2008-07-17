<?php
// Static reference to the Files table
$TableName = "Files";

/* Request to upload new file(s) */
if (isset($_GET['Add'])) {
 	// Get the session id and all files uploaded
 	$sid = $_REQUEST['sid']; 
 	$qstr = join("",file("files/{$sid}_qstring"));
	unlink("files/{$sid}_qstring");
	parse_str($qstr);
	$k = count($file['name']); 
	// Make a directory for the new files based on the unique session id
	mkdir('files/'.$sid, 0777);	
	// Move the new files from the temp location
	for($i=0 ; $i < $k ; $i++) {
		rename($file['tmp_name'][$i], 'files/'.$sid.'/'.$file['name'][$i]);
	 
	 	// Create a reference in the database to the files on the local system
	 	$parentIDname = ($ParentUniqueName . "ID");
	 	$parentClassNameID = $ParentUniqueName . $_SESSION[$parentIDname];
	 	$sql = "INSERT INTO $TableName (Name,Filename,Filesize,SessionID,ClassName,ParentClassNameID,Project) VALUES (\"" . $file['name'][$i] ."\",\"" . $file['name'][$i] ."\",\"". $file['size'][$i] ."\",\"". $sid ."\",\"". $UniqueName . "\",\"". $parentClassNameID . "\",\"". $_SESSION['ProjectID'] . "\")"; 	
		mysql_query($sql);
 	}   
 
 	// Close the session and redirect to the parent
 	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/mysql/close.php");
 	echo "<META HTTP-EQUIV='Refresh' CONTENT='0; URL=/".$source_location."/switchboard.php?UniqueName=$ParentUniqueName&AddedUniqueName=$UniqueName&View=1&ClassID=".$_SESSION[$parentIDname]."'>";
 	exit();
} /////////////////////

/* Request to show Add page */
if (isset($_GET['ShowAdd'])) {
	// Generate a random Session ID
 	$sid = md5(uniqid(rand()));
 	
	echo 
	"<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n".
	"<html xmlns=\"http://www.w3.org/1999/xhtml\">\n".
	"<head>\n".
	"<link type=\"text/css\" href=\"/".$source_location."/css/layout.css\" rel=\"stylesheet\" media=\"screen\" />\n".
	"<title>Add $DisplayName</title>\n".
	"<link rel=\"shortcut icon\" href=\"/".$source_location."/images/favicon.ico\" type=\"image/x-icon\" />\n".
	"<script language=\"javascript\" src=\"/".$source_location."/js/functions.js\" type=\"text/javascript\"></script>\n".
	"<script language=\"javascript\" src=\"/".$source_location."/js/prototype.js\" type=\"text/javascript\"></script>\n".
	"<script language=\"javascript\" src=\"/".$source_location."/js/scriptaculous.js\" type=\"text/javascript\"></script>\n".
	"<script language=\"javascript\" src=\"/".$source_location."/js/effects.js\" type=\"text/javascript\"></script>\n".
	"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n".
	"</head>\n".
	"<body>\n".
	"<div id=\"container\">\n".
 	" <div id=\"banner\"><a href=\"/".$source_location."\"><img src=\"/".$source_location."/images/header.jpg\" alt=\"\" border=\"0\" /></a>\n".
    "  <div id=\"addressbar\"><h1><a href=\"switchboard.php?UniqueName=Project&List=1\">Projects</a> >> "; echo showProjectName(); echo "</h1></div>\n".
    "  <div align=\"right\" id=\"loginbar\"><h3>";require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/php/showlogin.php");echo "</h3></div>\n";    
    echo 
 	" </div>\n". 	
    " <div id=\"content\">\n".
    "  <h2>Add $DisplayName</h2>\n".
    "  <p />\n".        
    "  <form name=\"files\" method=\"post\" action=\"/cgi-bin/mdw-upload.cgi?sid=$sid&amp;Path=http://$baseweburl/$source_location/switchboard.php?&amp;UniqueName=$UniqueName\" enctype=\"multipart/form-data\">\n".
    "   <input type=\"hidden\" name=\"sessionid\" value=\"$sid\" />\n".      
    "   <table border=\"0\">\n".
    "    <tr><td valign=\"top\"><b>Filename: </b></td><td><div><input type=\"file\" name=\"file[0]\"' /></div></td></tr>\n";
    if (isset($_GET['Multiple'])) {
    	echo
	    "    <tr><td valign=\"top\"><b>Filename: </b></td><td><div><input type=\"file\" name=\"file[1]\"' /></div></td></tr>\n".
	    "    <tr><td valign=\"top\"><b>Filename: </b></td><td><div><input type=\"file\" name=\"file[2]\"' /></div></td></tr>\n".
	    "    <tr><td valign=\"top\"><b>Filename: </b></td><td><div><input type=\"file\" name=\"file[3]\"' /></div></td></tr>\n".
	    "    <tr><td valign=\"top\"><b>Filename: </b></td><td><div><input type=\"file\" name=\"file[4]\"' /></div></td></tr>\n";        	    
    }
    echo 
    "    <tr><td><div class=\"buttons\"><button type=\"submit\" class=\"add\" onclick=\"new Effect.Appear('uploading');postIt(this.form,'http://$baseweburl/$source_location')\"/><img src=\"images/add.png\" alt=\"\"/>Add</button></div></td></tr>\n";
    // Remove the navigation stack options from a Cancel button 
    $previousURL = $_SERVER['HTTP_REFERER'];
    $returnURL = str_replace("GoingToSharedObject","",$previousURL);
    $returnURL = str_replace("ComingFromSharedObject","",$returnURL);
    echo
    "        <td><div class=\"buttons\"><button type=\"button\" class=\"cancel\" onclick=\"javascript:window.location.href = '$returnURL'\" /><img src=\"images/cancel.png\" alt=\"\"/>Cancel</button></div></td></tr>\n".
    "   </table>\n".
  	"  </form>\n".
  	"  <div id=\"uploading\" style=\"display: none;\">Uploading... Please wait for page to reload</div>\n".
    " </div>\n";
    require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/php/footer.php");
    echo 
    " </div>\n".
	"</div>\n".
	"</body></html>\n";
	
	// Close the session
    require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/mysql/close.php");
} /////////////////////


/* Request to update file description */
if (isset($_POST['Update'])) {
 // Get the class and parent information
 $classIDname = ($UniqueName . "ID");  
 $parentIDname = ($ParentUniqueName . "ID");
 $parentClassNameID = $ParentUniqueName . $_SESSION[$parentIDname];
 
 // Begin the insert query
 $sql = "UPDATE $TableName SET Name=\"".$_POST['Name']."\",";
 
 // Get all the attributes for this object
 $i = 0;    
 for ($i = 0; $i < sizeof($Items); $i++) {    	
    $itemArray = split(":", $Items[$i]);    
    if ($itemArray[0] == "text" || $itemArray[0] == "textarea" || $itemArray[0] == "radio" ) {
	    // Add the object reference that will be updated
    	$sql .= $itemArray[2]."='".$_POST[$itemArray[2]]."',";
    }
    else if ($itemArray[0] == "datetime") {
    	$DateTime = $_POST["$itemArray[2]Year"]. "-" .$_POST["$itemArray[2]Month"]. "-" .$_POST["$itemArray[2]Day"]. " " .$_POST["$itemArray[2]Hour"]. ":" .$_POST["$itemArray[2]Minute"]. ":00";
		// Add the object reference that will be updated
	    $sql .= $itemArray[2]."='$DateTime',";
    }
 } 
 
 // trim the last comma
 $sql = rtrim($sql,",");
 
 // Add the object reference 
 $classIDname = ($UniqueName . "ID");
 $sql .= "WHERE ID='".$_SESSION[$classIDname]."'";
  		
 // Perform the query  		
 mysql_query($sql); 
 
 // Close the session
 require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/mysql/close.php");
 
 // Redirect back to the object  
 echo "<META HTTP-EQUIV='Refresh' CONTENT='0; URL=/".$source_location."/switchboard.php?UniqueName=$UniqueName&View=1&ClassID=".$_SESSION[$classIDname]."'>";
 exit();		
} /////////////////////

/* Request to show Edit page */
if (isset($_GET['Edit'])) {
	// Get the database information for this object
	$classIDname = ($UniqueName . "ID");	
	$results = mysql_query("SELECT * FROM $TableName WHERE ID=".$_SESSION[$classIDname]);	
	$ObjectDataArray = mysql_fetch_array($results,MYSQL_ASSOC);			
	
	echo 
	"<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n".
	"<html xmlns=\"http://www.w3.org/1999/xhtml\">\n".
	"<head>\n".
	"<link type=\"text/css\" href=\"/".$source_location."/css/layout.css\" rel=\"stylesheet\" media=\"screen\" />\n".
	"<title>Edit: ".$ObjectDataArray{'Filename'}."</title>\n".
	"<link rel=\"shortcut icon\" href=\"/".$source_location."/images/favicon.ico\" type=\"image/x-icon\" />\n".
	"<script language=\"javascript\" src=\"/".$source_location."/js/functions.js\" type=\"text/javascript\"></script>\n".	
	"<script language=\"javascript\" type=\"text/javascript\" src=\"/".$source_location."/tinymce/jscripts/tiny_mce/tiny_mce.js\"></script>\n".
	"<script language=\"javascript\" type=\"text/javascript\">
	tinyMCE.init({
        theme : \"advanced\",
        mode : \"textareas\",
        extended_valid_elements : \"a[href|target|name]\",
        theme_advanced_disable : \"removeformat,formatselect,styleselect \",
        plugins : \"table,paste\",
        theme_advanced_buttons3_add : \"pastetext,pasteword,selectall\",
        paste_create_paragraphs : false,
        paste_create_linebreaks : false,
        paste_use_dialog : true,
        paste_auto_cleanup_on_paste : true,
        paste_convert_middot_lists : false,
        paste_unindented_list_class : \"unindentedList\",
        paste_convert_headers_to_strong : true,
        paste_insert_word_content_callback : \"convertWord\",
        theme_advanced_buttons3_add_before : \"tablecontrols,separator\",        
        relative_urls : false,
        convert_urls : false,
        cleanup : false,
        debug : false
	});</script>".			
	"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n".
	"</head>\n".
	"<body>\n".
	"<div id=\"container\">\n".
 	" <div id=\"banner\"><a href=\"/".$source_location."\"><img src=\"/".$source_location."/images/header.jpg\" alt=\"\" border=\"0\" /></a>\n".
    "  <div id=\"addressbar\"><h1><a href=\"switchboard.php?UniqueName=Project&List=1\">Projects</a> >>"; echo showProjectName(); echo "</h1></div>\n".
    "  <div align=\"right\" id=\"loginbar\"><h3>";require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/php/showlogin.php");echo "</h3></div>\n";    
    echo 
 	" </div>\n". 	
    " <div id=\"content\">\n".
    "  <h2>Edit $DisplayName: ".$ObjectDataArray{'Name'}."</h2>\n".
    "  <form name=\"edit\" method=\"post\" action=\"/".$source_location."/switchboard.php?Update=1&UniqueName=$UniqueName\" onSubmit=\"return isNotEmpty(Name);\">\n".    
    "   <table border=\"0\">\n".
    "    <tr><td valign=\"top\"><b>Name: </b></td>\n" .          
    "        <td><input type=\"text\" name=\"Name\" size=\"50\" value=\"".$ObjectDataArray{'Name'}."\" /></td></tr>\n";
    // Display all the editable attribute items    
    for ($i = 0; $i < sizeof($Items); $i++) {    	
    	$itemArray = split(":", $Items[$i]);
    	// Get the input types
    	if ($itemArray[0] == "text") {
    		echo 
    		"    <tr><td valign=\"top\"><b>$itemArray[1]: </b></td>\n" .
    		"        <td><input type=\"text\" name=\"$itemArray[2]\" size=\"50\" value=\"".$ObjectDataArray{$itemArray[2]}."\" /></td></tr>\n";
    	}
    	if ($itemArray[0] == "textarea") {
    		echo
    		"    <tr><td valign=\"top\"><b>$itemArray[1]: </b></td>\n" .
    		"        <td><textarea name=\"$itemArray[2]\" cols=\"50\" rows=\"20\">".$ObjectDataArray{$itemArray[2]}."</textarea></td></tr>\n";
    	}
    	if ($itemArray[0] == "radio") {
    		echo
    		"    <tr><td valign=\"top\"><b>$itemArray[1]? </b></td>\n" .
    		"        <td>";    		
    		for ($j = 3; $j < sizeof($itemArray); $j = $j+2) {
    			echo "<input type=\"radio\" name=\"$itemArray[2]\" value=\"$itemArray[$j]\"";if ($ObjectDataArray{$itemArray[2]} == $itemArray[$j]) echo "checked='checked'"; echo "/> $itemArray[$j]<br />";    			          			
    		}
    		echo 
    		"</td></tr>\n";
    	}    	
    	if ($itemArray[0] == "datetime") {
    		echo
    		" 	 <tr><td valign=\"top\"><b>$itemArray[1]: </b></td>\n" .
		    "        <td>\n" .
		    "         <select name=\"$itemArray[2]Month\">"; echo getMonths(date("m", strtotime($ObjectDataArray{$itemArray[2]}))); echo "</select>\n".
			"         <select name=\"$itemArray[2]Day\">"; echo getDays(date("d", strtotime($ObjectDataArray{$itemArray[2]}))); echo "</select>\n".	
		    "         <select name=\"$itemArray[2]Year\">"; echo getYears(date("Y", strtotime($ObjectDataArray{$itemArray[2]}))); echo "</select>&nbsp;&nbsp;\n".
		    "         <select name=\"$itemArray[2]Hour\">"; echo getHours(date("G", strtotime($ObjectDataArray{$itemArray[2]}))); echo "</select>:\n".
		    "         <select name=\"$itemArray[2]Minute\">"; echo getMinutes(date("i", strtotime($ObjectDataArray{$itemArray[2]}))); echo "</select> ET (mm/dd/yyyy)\n".
		    "        </td>\n" .
		    "    </tr>\n"; 
    	}
    }
    // Remove the navigation stack options from a Cancel button 
    $previousURL = $_SERVER['HTTP_REFERER'];
    $returnURL = str_replace("GoingToSharedObject","",$previousURL);
    $returnURL = str_replace("ComingFromSharedObject","",$returnURL);    
    echo
    "    <tr><td /><td><div class=\"buttons\"><button type=\"submit\" class=\"add\" /><img src=\"images/accept.png\" alt=\"\"/>Update</button></div></td></tr>\n".
    "        <td /><td><div class=\"buttons\"><button type=\"button\" class=\"cancel\" onclick=\"javascript:window.location.href = '$returnURL'\" /><img src=\"images/cancel.png\" alt=\"\"/>Cancel</button></div></td></tr>\n".
    "    <input type=\"hidden\" name=\"UniqueName\" value=\"$UniqueName\" />\n". 
    "    <input type=\"hidden\" name=\"Update\" value=\"Update\" />\n".  
    "   </table>\n".
    "  </form>\n".
    " </div>\n";
    require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/php/footer.php");
    echo 
    " </div>\n".
	"</div>\n".
	"</body></html>\n";	
	
	// Close the session
 	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/mysql/close.php");
} /////////////////////

/* Request to View Page */
if (isset($_GET['View'])) {
	// Form ParentID name for navigation later
	$parentIDname = ($ParentUniqueName . "ID");

	// Get the database information for this object 	
	$results = mysql_query("SELECT * FROM $TableName WHERE ID=".$_GET['ClassID']);		
	$ObjectDataArray = mysql_fetch_array($results,MYSQL_ASSOC);
	$ProjectAdmins = explode(",",$_SESSION['projectsadmin']);		
	
	// Create a SESSION variable for this page
	$classIDname = ($UniqueName . "ID");	
	$_SESSION[$classIDname] = $_GET['ClassID'];
	
	// Create the parent SESSION variable to get back (needed for searching)
	$IDarray = explode($ParentUniqueName,$ObjectDataArray{'ParentClassNameID'});
	$_SESSION[$parentIDname] = $IDarray[1];		
	
	echo 
	"<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n".
	"<html xmlns=\"http://www.w3.org/1999/xhtml\">\n".
	"<head>\n".
	"<link type=\"text/css\" href=\"/".$source_location."/css/layout.css\" rel=\"stylesheet\" media=\"screen\" />\n".
	"<title>$DisplayName: ".$ObjectDataArray{'Filename'}."</title>\n".
	"<link rel=\"shortcut icon\" href=\"/".$source_location."/images/favicon.ico\" type=\"image/x-icon\" />\n".
	"<script language=\"javascript\" src=\"/".$source_location."/js/functions.js\" type=\"text/javascript\"></script>\n".
	"<script language=\"javascript\" src=\"/".$source_location."/js/prototype.js\" type=\"text/javascript\"></script>\n".
	"<script language=\"javascript\" src=\"/".$source_location."/js/scriptaculous.js\" type=\"text/javascript\"></script>\n".
	"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n".
	"</head>\n".
	"<body>\n".
	"<div id=\"container\">\n".
 	" <div id=\"banner\"><a href=\"/".$source_location."\"><img src=\"/".$source_location."/images/header.jpg\" alt=\"\" border=\"0\" /></a>\n".
    "  <div id=\"addressbar\"><h1><a href=\"switchboard.php?UniqueName=Project&List=1\">Projects</a> >> "; echo showProjectName(); echo "</h1></div>\n".
    "  <div align=\"right\" id=\"loginbar\"><h3>";require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/php/showlogin.php");echo "</h3></div>\n";    
    echo 
 	" </div>\n". 	
    " <div id=\"content\">\n".
    "  <h2>$DisplayName: ".$ObjectDataArray{'Name'}."</h2>\n".
    "  <table border=\"0\">\n".
    "   <tr><td valign=\"top\"><b>Name: </b></td>\n";	
	echo 	
	"       <td><span id=Name>".$ObjectDataArray{'Name'}."&nbsp;</span>";
	if (in_array($_SESSION{'ProjectID'},$ProjectAdmins)) {
		echo
		"       <script type=\"text/javascript\"> " .
		"	 		new Ajax.InPlaceEditor('Name', 'php/ajaxupdater.php',{callback: function(form, value) { return 'TableName=$TableName&Item=Name&Value=' + escape(value)+'&ID=".$_SESSION[$classIDname]."'}});" .
		"		</script>";
	}
	echo
	"       </td></tr>\n".
 	"   <tr><td valign=\"top\"><b>Filename: </b></td>\n" .
	"       <td>".$ObjectDataArray{'Filename'}."</td></tr>\n" .
	"   <tr><td valign=\"top\"><b>Size: </b></td>\n" .
	"       <td>".$ObjectDataArray{'Filesize'}." bytes</td></tr>\n";
	// Display all the attribute items            
    for ($i = 0; $i < sizeof($Items); $i++) {    	    	
    	$itemArray = split(":", $Items[$i]);
    	// text fields
    	if ($itemArray[0] == "text") {    		
	    	echo 
	    		"   <tr><td valign=\"top\"><b>$itemArray[1]: </b></td>\n" .
	    		"       <td><span id=".$itemArray[2].">".$ObjectDataArray{$itemArray[2]}."&nbsp;</span>";
	    		if (in_array($_SESSION{'ProjectID'},$ProjectAdmins)) {
	    			echo
	    			"       <script type=\"text/javascript\"> " .
 					"	 		new Ajax.InPlaceEditor('".$itemArray[2]."', 'php/ajaxupdater.php',{callback: function(form, value) { return 'TableName=$TableName&Item=".$itemArray[2]."&Value=' + escape(value)+'&ID=".$_SESSION[$classIDname]."'}});" .
					"		</script>";
	    		}
				echo
	    		"       </td></tr>\n";
    	}    	
    	// radio button
    	if ($itemArray[0] == "radio") {    		
	    	echo 
	    		"   <tr><td valign=\"top\"><b>$itemArray[1]: </b></td>\n" .
	    		"       <td>".$ObjectDataArray{$itemArray[2]}."</td></tr>\n";
    	}
    	// Textarea
    	if ($itemArray[0] == "textarea") {
    		echo 
	    		"   <tr><td valign=\"top\"><b>$itemArray[1]: </b></td>\n" ;
	    		// This will create a (more) function for larger text areas	    			    			    
	    		if (strlen($ObjectDataArray{$itemArray[2]}) > 200) {
	    		$textareatext = $ObjectDataArray{$itemArray[2]};
	    		    echo "       <td>";
	    			echo "       <div style=\"padding:0px;margin:0px;display:block;text-align:justify;\" id=\"".$itemArray[2]."textareasmall\">".substr($textareatext,0,200)."... <a style=\"cursor : pointer;\" onClick='Effect.toggle(\"".$itemArray[2]."textareasmall\",\"slide\");Effect.toggle(\"".$itemArray[2]."\",\"appear\");'>[<u>more</u>]</a></div>\n";	    			
	    			echo "       <div style=\"padding:0px;margin:0px;display:none;text-align:justify;\" id=\"".$itemArray[2]."\">$textareatext</div>\n";
	    			if (in_array($_SESSION{'ProjectID'},$ProjectAdmins)) {
	    				echo
	    				 "       <script type=\"text/javascript\"> " .
 						 " 	 		new Ajax.InPlaceEditor('".$itemArray[2]."', 'php/ajaxupdater.php',{rows:15,cols:40, callback: function(form, value) { return 'TableName=$TableName&Item=".$itemArray[2]."&Value=' + escape(value)+'&ID=".$_SESSION[$classIDname]."'}});" .
						 "		 </script>";
	    			}
					echo "       </td></tr>";
	    		}
				else {								
					echo "       <td><div style=\"padding:0px;margin:0px;\" id=\"".$itemArray[2]."\">".$ObjectDataArray{$itemArray[2]}." &nbsp;</div>";
					if (in_array($_SESSION{'ProjectID'},$ProjectAdmins)) {
						echo
						 "       <script type=\"text/javascript\"> " .
 						 " 	 		new Ajax.InPlaceEditor('".$itemArray[2]."', 'php/ajaxupdater.php',{rows:15,cols:40, callback: function(form, value) { return 'TableName=$TableName&Item=".$itemArray[2]."&Value=' + escape(value)+'&ID=".$_SESSION[$classIDname]."'}});" .
						 "		 </script>" ;
					}
					echo "       </td></tr>\n";
				}	    			
    	}    	 
    	// Date and Time property
    	if ($itemArray[0] == "datetime") {
	    	echo 
	    		"   <tr><td valign=\"top\"><b>$itemArray[1]: </b></td>\n" .
	    		"       <td><span id=".$itemArray[2].">".$ObjectDataArray{$itemArray[2]}."&nbsp;</span>";
	    		if (in_array($_SESSION{'ProjectID'},$ProjectAdmins)) {
	    			echo
		    		"       <script type=\"text/javascript\"> " .
	 				"	 		new Ajax.InPlaceEditor('".$itemArray[2]."', 'php/ajaxupdater.php',{callback: function(form, value) { return 'TableName=$TableName&Item=".$itemArray[2]."&Value=' + escape(value)+'&ID=".$_SESSION[$classIDname]."'}});" .
					"		</script>";
	    		}
				echo
	    		"       </td><td>(yyyy-mm-dd HH:MM:SS)</tr>\n";
    	}
    }       
    echo
	"   <tr><td><a title=\"Download\" href=\"http://$baseweburl/$source_location/files/".$ObjectDataArray{'SessionID'}."/".$ObjectDataArray{'Filename'}."\"><img class=\"download\" src=\"images/disk.png\" /></a></td></tr>\n".
    "  </table>\n".
    "  <p />\n";
    // Show edit/delete control for Project Admins only
    if (in_array($_SESSION{'ProjectID'},$ProjectAdmins)) {	    
		echo
    	"  <form class=\"buttonforms\" name=\"edit\" method=\"get\" action=\"/".$source_location."/switchboard.php\">\n".        	
    	"   <div class=\"buttons\"><button type=\"submit\" class=\"edit\" /><img src=\"images/page_edit.png\" alt=\"\"/>Edit</button></div>\n".    
    	"   <input type=\"hidden\" name=\"UniqueName\" value=\"$UniqueName\" />\n".
    	"   <input type=\"hidden\" name=\"Edit\" value=\"Edit\" />\n".
    	"  </form>\n".
    	"  <form class=\"buttonforms\" name=\"delete\" method=\"get\" action=\"/".$source_location."/switchboard.php\">\n".       
    	"   <div class=\"buttons\"><button type=\"submit\" class=\"delete\" onclick=\"return confirmdelete('object')\" /><img src=\"images/cross.png\" alt=\"\"/>Delete</button></div>\n".    	
    	"   <input type=\"hidden\" name=\"UniqueName\" value=\"$UniqueName\" />\n".
    	"   <input type=\"hidden\" name=\"Delete\" value=\"Delete\" />\n".
    	"  </form>\n";    	
	}
	echo
    "  <form class=\"buttonforms\" name=\"Navigate\" action=\"\">\n".   
    "   <div class=\"buttons\"><button type=\"button\" class=\"add\" onclick=window.location.href=\"/".$source_location."/switchboard.php?UniqueName=$ParentUniqueName&View=1&ClassID=".$_SESSION[$parentIDname]."\" /><img src=\"images/arrow_left.png\" alt=\"\"/>Back To $ParentDisplayName</button></div>\n".   
    "  </form>\n".
 	" </div>\n";     	 	  
 	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/php/footer.php");
 	echo 
 	"</div>\n".  	
	"</body>\n".
	"</html>\n";    
	
	// Close the session
    require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/mysql/close.php");
} /////////////////////

/* Request to Delete Object */
if (isset($_GET['Delete'])) {
 // Get the object to delete
 $classIDname = ($UniqueName . "ID");
 
 // Get the database information for this object 	
 $results = mysql_query("SELECT * FROM $TableName WHERE ID=".$_SESSION[$classIDname]);	
 $ObjectDataArray = mysql_fetch_array($results,MYSQL_ASSOC);		

 // Delete the record  
 mysql_query("DELETE FROM $TableName WHERE ID=$_SESSION[$classIDname]");
 
 // Delete the file and directory
 unlink("files/".$ObjectDataArray{'SessionID'}."/".$ObjectDataArray{'Filename'}); 
 
 // Remove this now invalid SESSION variable
 unset($_SESSION[$classIDname]);
 
 // Close the session
 require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/mysql/close.php");

 // Redirect back to the parent
 $parentIDname = ($ParentUniqueName . "ID");
 echo "<META HTTP-EQUIV='Refresh' CONTENT='0; URL=/".$source_location."/switchboard.php?UniqueName=$ParentUniqueName&View=1&ClassID=".$_SESSION[$parentIDname]."'>";
 exit();
} /////////////////////
?>
