<?php	
/* Request to add new object */
if (isset($_GET['Add'])) {
 	// Begin the insert query 
 	$sql = "INSERT INTO $TableName (Name,";
 	
 	// Special case for the Project
 	if ($UniqueName == "Project") {
 		$sql .= "SharedName,isShared,ClassName) VALUES (";
 		
 		// Add the name of the new object
 		$sql .= "\"".$_GET['Name']."\",";
 		$sql .= "\"$SharedName\",";
 		$sql .= "\"0\","; 
 		$sql .= "\"$UniqueName\")"; 		 		
 	}
 	
 	// All other cases
 	else { 
	 	// Check if this is a shared object and add to the query the group name and owning project
	 	if (!empty($SharedName)) {
	 		$sql .= "SharedName,OwningProject,isShared,";
	 	}
	 
	 	// Add the Class and Parent information
	 	if (isset($_GET['AddNew']))  
			$sql .= "ClassName) VALUES (";
		else
	  		$sql .= "ClassName,ParentClassNameID) VALUES (";
	 
	 	// Add the name of the new object
	 	$sql .= "\"".$_GET['Name']."\",";
	 
	 	// Check if this is a shared object and add the name for the object group and owning project
	 	if (!empty($SharedName)) {
	 		$sql .= "\"$SharedName\",\"".$_SESSION['ProjectID']."\"".",";
	 		
		 	// Is this object allowed to be shared with other Projects
	 		if ($_GET['shared'] == "no")  		
				$sql .= "\"0\","; 	
			else 
				$sql .= "\"1\",";
	 	}
	 	 
		// Add the class and parent references 	
	 	if (!isset($_GET['AddNew'])) { 
	 		$sql .= "\"$UniqueName\","; 	
		 	$parentIDname = ($ParentUniqueName . "ID");
		 	$parentClassNameID = $ParentUniqueName . $_SESSION[$parentIDname];
		 	$sql .= "\"$parentClassNameID\")";
	 	}   
	 	else
			$sql .= "\"$UniqueName\")";
 	}
 
 	// Perform the insert into the database 	
 	mysql_query($sql); 	
  
 	// Add Project to User list if this is a Project add
 	if ($UniqueName == "Project") {  	 	
 		// Update user session with new project ID
		$_SESSION['projects'] .= "," . mysql_insert_id();
		$_SESSION['projectsadmin'] .= "," . mysql_insert_id();	 
	
		// Update the user's list with the new project ID setting the user as member and admin
 		mysql_query("UPDATE Users SET Projects='". $_SESSION['projects'] ."', ProjectsAdmin='". $_SESSION['projectsadmin'] ."' WHERE Username='" . $_SESSION['username'] ."'"); 	 
 	}    
 
 	// If this was created from a list, go back to the list view 
	if (isset($_GET['FromList'])) {
 		echo "<META HTTP-EQUIV='Refresh' CONTENT='0; URL=/".$source_location."/switchboard.php?UniqueName=$UniqueName&List=1'>";
 		exit();
 	}
	// If this was created from another object, go back to that parent object
 	if (!isset($_GET['AddNew'])) { 		
 		// Close the session
 	    require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/mysql/close.php"); 	
 		echo "<META HTTP-EQUIV='Refresh' CONTENT='0; URL=/".$source_location."/switchboard.php?UniqueName=$ParentUniqueName&AddedUniqueName=$UniqueName&View=1&ClassID=".$_SESSION[$parentIDname]."'>";
 	}   		 		
} /////////////////////

/* Request to show add page */
if (isset($_GET['ShowAdd'])) {
	echo 
	"<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n".
	"<html xmlns=\"http://www.w3.org/1999/xhtml\">\n".
	"<head>\n".
	"<link type=\"text/css\" href=\"/".$source_location."/css/layout.css\" rel=\"stylesheet\" media=\"screen\" />\n".
	"<title>Add $DisplayName</title>\n".
	"<link rel=\"shortcut icon\" href=\"/".$source_location."/images/favicon.ico\" type=\"image/x-icon\" />\n".
	"<script language=\"javascript\" src=\"/".$source_location."/js/functions.js\" type=\"text/javascript\"></script>\n".
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
    "  <form name=\"add\" method=\"get\" action=\"/".$source_location."/switchboard.php\" onSubmit=\"return isNotEmpty(Name);\">\n".    
    "   <table border=\"0\">\n".
    "    <tr><td valign=\"top\"><b>Name: </b></td>\n" .
    "        <td><input type=\"text\" name=\"Name\" size=\"50\" /></td></tr>\n";
    // If this is not a Project object and its allowed to be shared, show the sharing options    
    if ($UniqueName!="Project" && isset($AllowSharing) && $AllowSharing=="Yes") {
     echo
     "    <tr><td><input type=\"radio\" name=\"shared\" value=\"no\" checked=\"checked\" title=\"New object won't be shared with other Projects\"/>Don't share with other projects<br />\n".
     "            <input type=\"radio\" name=\"shared\" value=\"yes\" title=\"Object will be available to other Projects\"/>Share with other projects<br /></td></tr>\n";
    }
    else
     echo "       <input type=\"hidden\" name=\"shared\" value=\"no\" />\n";
    if (isset($_GET['FromList'])) 
     echo "       <input type=\"hidden\" name=\"FromList\" value=\"1\" />\n";
    echo    
    "    <tr><td><div class=\"buttons\"><button type=\"submit\" class=\"add\" /><img src=\"images/add.png\" alt=\"\"/>Add</button></div></td></tr>\n";
    // If this isn't a project, then allow stack navigation, otherwise, its a project so go back to the main project list
	if ($UniqueName!="Project") {
	    // Remove the navigation stack options from a Cancel button 
	    $previousURL = $_SERVER['HTTP_REFERER'];
	    $returnURL = str_replace("GoingToSharedObject","",$previousURL);
	    $returnURL = str_replace("ComingFromSharedObject","",$returnURL);
	    echo
	    "        <td><div class=\"buttons\"><button type=\"button\" class=\"cancel\" onclick=\"javascript:window.location.href = '$returnURL'\" /><img src=\"images/cancel.png\" alt=\"\"/>Cancel</button></div></td></tr>\n";
	}
	else    
		echo "        <td><div class=\"buttons\"><button type=\"button\" class=\"cancel\" onclick=\"javascript:window.location.href = 'switchboard.php?UniqueName=Project&List=1'\" /><img src=\"images/cancel.png\" alt=\"\"/>Cancel</button></div></td></tr>\n";
	echo
    "    <input type=\"hidden\" name=\"UniqueName\" value=\"$UniqueName\" />\n".  
    "    <input type=\"hidden\" name=\"Add\" value=\"Add\" />\n".
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


/* Request to update an object */
if (isset($_POST['Update'])) {
 	// Begin the insert query 
 	$sql = "UPDATE $TableName SET ";  
 
 	// Get all the attributes of the object
 	$i = 0;    
 	for ($i = 0; $i < sizeof($Items); $i++) {    	
    	$itemArray = split(":", $Items[$i]);
    	if ($itemArray[0] == "text" || $itemArray[0] == "textarea" || $itemArray[0] == "radio" ) {
	    	// Add the attribute data
    		$sql .= $itemArray[2]."='".$_POST[$itemArray[2]]."',";
    	}
    	else if ($itemArray[0] == "datetime") {
    		$DateTime = $_POST["$itemArray[2]Year"]. "-" .$_POST["$itemArray[2]Month"]. "-" .$_POST["$itemArray[2]Day"]. " " .$_POST["$itemArray[2]Hour"]. ":" .$_POST["$itemArray[2]Minute"]. ":00";
			// Add the attribute data
		    $sql .= $itemArray[2]."='$DateTime',";
    	}    
 	}
 
 	// If this is meant to be shared
 	if (isset($_POST['shared'])) {
 		if ($_POST['shared'] == "no")  		
 			$sql .= "isShared=\"0\","; 	
		else 
 			$sql .= "isShared=\"1\",";
 		
 		// Set owning project
 		$sql .= "OwningProject=\"".$_POST['OwnedBy']."\" "; 	
 	}
 	else {
 		$sql = rtrim($sql,","); 	
 	} 
 
 	// Add the object reference to update against
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

/* Request to show the Edit page */
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
	"<title>Edit: ".$ObjectDataArray{'Name'}."</title>\n".
	"<link rel=\"shortcut icon\" href=\"/".$source_location."/images/favicon.ico\" type=\"image/x-icon\" />\n".
	"<script language=\"javascript\" src=\"/".$source_location."/js/functions.js\" type=\"text/javascript\"></script>\n".
	"<script language=\"javascript\" type=\"text/javascript\" src=\"/".$source_location."/tinymce/jscripts/tiny_mce/tiny_mce.js\"></script>\n".
	"<script language=\"javascript\" type=\"text/javascript\">
	tinyMCE.init({
        theme : \"advanced\",
        mode : \"textareas\",
        extended_valid_elements : \"a[href|target|name]\",
        theme_advanced_disable : \"removeformat,formatselect,styleselect,justifyleft,justifycenter,justifyright,justifyfull \",
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
    "  <div id=\"addressbar\"><h1><a href=\"switchboard.php?UniqueName=Project&List=1\">Projects</a> >> "; echo showProjectName(); echo "</h1></div>\n".
    "  <div align=\"right\" id=\"loginbar\"><h3>";require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/php/showlogin.php");echo "</h3></div>\n";    
    echo 
 	" </div>\n". 	
    " <div id=\"content\">\n".
    "  <h2>Edit $DisplayName: ".$ObjectDataArray{'Name'}."</h2>\n".
    "  <form name=\"edit\" method=\"post\" action=\"/".$source_location."/switchboard.php?Update=1&UniqueName=$UniqueName\" onSubmit=\"return isNotEmpty(Name);\">\n".    
    "   <table border=\"0\">\n";
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
		    "         <select name=\"$itemArray[2]Hour\">"; echo getHours(date("H", strtotime($ObjectDataArray{$itemArray[2]}))); echo "</select>:\n".
		    "         <select name=\"$itemArray[2]Minute\">"; echo getMinutes(date("i", strtotime($ObjectDataArray{$itemArray[2]}))); echo "</select> ET (mm/dd/yyyy)\n".
		    "        </td>\n" .
		    "    </tr>\n";		    
    	}    	
    }        
    // If this object can be shared, show the option to share or not
    if (isset($AllowSharing) && $AllowSharing=="Yes") {
    	if ($ObjectDataArray{'isShared'} == "1")
    		echo
			"    <tr><td><input type=\"radio\" name=\"shared\" value=\"no\" title=\"New object won't be shared with other Projects\"/>Don't share with other projects<br />\n".
     		"            <input type=\"radio\" name=\"shared\" value=\"yes\" checked=\"checked\" title=\"Object will be available to other Projects\"/>Share with other projects<br /></td></tr>\n";
     	else
     		echo
			"    <tr><td><input type=\"radio\" name=\"shared\" value=\"no\" checked=\"checked\" title=\"New object won't be shared with other Projects\"/>Don't share with other projects<br />\n".
     		"            <input type=\"radio\" name=\"shared\" value=\"yes\" title=\"Object will be available to other Projects\"/>Share with other projects<br /></td></tr>\n";
     	
     	// Set which project owns it     	     	
     	$OwningProjectArray = mysql_query("SELECT ID,Name FROM BasicClass WHERE ClassName=\"Project\" AND ID=".$ObjectDataArray{'OwningProject'}."");
		$OwningProject = mysql_fetch_array($OwningProjectArray,MYSQL_ASSOC);
		echo 		
		"    <tr><td><b>Managed By: </b></td><td><select name=\"OwnedBy\">\n";
		$Projects = mysql_query("SELECT ID,Name FROM BasicClass WHERE ClassName=\"Project\"");		
    	while ($ProjectInfo = mysql_fetch_array($Projects,MYSQL_ASSOC)) {
    		// List only projects allowed to user
  			$ProjectAdmins = explode(",",$_SESSION['projectsadmin']);   
		  	if (in_array($ProjectInfo{'ID'},$ProjectAdmins)) {
	    		if ($OwningProject{'ID'} == $ProjectInfo{'ID'})
	    			echo "<option value=".$ProjectInfo{'ID'}." selected='selected'>".$ProjectInfo{'Name'}."</option>";
				else
					echo "<option value=".$ProjectInfo{'ID'}.">".$ProjectInfo{'Name'}."</option>";
		  	}       				
    	}     		
    	echo "</selected></td></tr>";	     		     		     		
    }
    // Remove the navigation stack options from a Cancel button 
    if(isset($_SERVER['HTTP_REFERER'])){
	    $previousURL = $_SERVER['HTTP_REFERER'];
	    $returnURL = str_replace("GoingToSharedObject","",$previousURL);
	    $returnURL = str_replace("ComingFromSharedObject","",$returnURL);
    }
    echo        
    "    <tr><td><div class=\"buttons\"><button type=\"submit\" class=\"add\" /><img src=\"images/accept.png\" alt=\"\"/>Update</button></div></td></tr>\n".
    "        <td><div class=\"buttons\"><button type=\"button\" class=\"cancel\" onclick=\"javascript:window.location.href = '$returnURL'\" /><img src=\"images/cancel.png\" alt=\"\"/>Cancel</button></div></td></tr>\n".
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

/* Request for the View Page */
if (isset($_GET['View'])) {	
	// Get the database information for this object
	$results = mysql_query("SELECT * FROM $TableName WHERE ID=".$_GET['ClassID']);
	$ObjectDataArray = mysql_fetch_array($results,MYSQL_ASSOC);
	$ProjectAdmins = explode(",",$_SESSION['projectsadmin']); 
	
	// Create a SESSION variable for this page
	if (!isset($SharedName)) {
		$parentIDname = ($ParentUniqueName . "ID");
		if (!isset($_SESSION[$parentIDname])) {
			$_SESSION[$parentIDname] = preg_replace('/[^0-9*]/', '', $ObjectDataArray['ParentClassNameID']);			
		}
	} 		
	$classIDname = ($UniqueName . "ID");	
	$_SESSION[$classIDname] = $_GET['ClassID'];
	// Form the identifying alphanumeric ID for this class used to pull data from child objects
	$classNameID = $UniqueName . $_SESSION[$classIDname];	
	
	// Remove the navigation stack options from the previous URL now
	if(isset($_SERVER['HTTP_REFERER'])){
    	$previousURL = $_SERVER['HTTP_REFERER'];
    	$previousURL = str_replace("&GoingToSharedObject=1","",$previousURL);
    	$previousURL = str_replace("&ComingFromSharedObject=1","",$previousURL);
	}
	
	// If the previous page directed to a shared object, use the shared object navigation stack
	if (isset($_GET['GoingToSharedObject'])) {
		// Track movements to shared objects in a navigation stack		
		array_push($_SESSION['SharedObjectNav'],$previousURL . "^" . $_GET['ParentDisplayName']);					
	}
	
	// If this was from a shared object, pop its url from the navigation stack.  
	if (isset($_GET['ComingFromSharedObject'])) {
		array_pop($_SESSION['SharedObjectNav']);				
	}	
			
	// Track movements for breadcrumb trail	
	array_push($_SESSION['Breadcrumbs'], $DisplayName);							
	
	echo 
	"<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n".
	"<html xmlns=\"http://www.w3.org/1999/xhtml\">\n".
	"<head>\n".
	"<link type=\"text/css\" href=\"/".$source_location."/css/layout.css\" rel=\"stylesheet\" media=\"screen\" />\n".
	"<title>$DisplayName: ".$ObjectDataArray{'Name'}."</title>\n".
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
    "  <h2>$DisplayName: ".$ObjectDataArray{'Name'}."</h2>\n";
    
    // Navigation control to go to the Parent reference but can't navigate back from Project
    if ($UniqueName != "Project") {
    	if (empty($SharedName)) {	
	    	echo
		    "  <form class=\"buttonforms\" name=\"Navigate\" action=\"\">\n".
		    "   <div class=\"buttons\"><button type=\"button\" class=\"add\" onclick=window.location.href=\"/".$source_location."/switchboard.php?UniqueName=$ParentUniqueName&View=1&ClassID=".$_SESSION[$parentIDname]."\" /><img src=\"images/arrow_left.png\" alt=\"\"/>Back To $ParentDisplayName</button></div>\n".			
		    "  </form>\n";
    	}
    	// Shared Object navigation is from an URL stack in memory
    	else {    		    		
    		$returnInformation = explode("^",end($_SESSION['SharedObjectNav']));
    		$returnURL = $returnInformation[0];
    		$returnDisplayName = $returnInformation[1];
    		
    		echo
		    "  <form class=\"buttonforms\" name=\"Navigate\" action=\"\">\n".   
		    "   <div class=\"buttons\"><button type=\"button\" class=\"add\" onclick=window.location.href=\"$returnURL&ComingFromSharedObject=1\" /><img src=\"images/arrow_left.png\" alt=\"\"/>Back To $returnDisplayName</button></div>\n".   
		    "  </form>\n";
    	}	   	    
    }
    echo
    "  <table border=\"0\">";
    // Display all the attribute items               	    	
	for ($i = 0; $i < sizeof($Items); $i++) {    	
    	$itemArray = split(":", $Items[$i]);
		// Get the attribute type  
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
    	if ($itemArray[0] == "radio") {
	    	echo 
	    		"   <tr><td valign=\"top\"><b>$itemArray[1]: </b></td>\n" .
	    		"       <td><span id=".$itemArray[2].">".$ObjectDataArray{$itemArray[2]}."</span>" .	    		
	    		"       </td></tr>\n";
    	}
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
    }
    echo 
    "  </table>\n".
    "  <p />\n";            

	// To show the child and shared objects in the subsection, create a new array of all non attribute items
    $NewItems = array();
    for ($i = 0; $i < sizeof($Items); $i++) {
    	$itemArray = split(":", $Items[$i]);
    	if ($itemArray[0] == "object" || $itemArray[0] == "sharedobject" || $itemArray[0] == "objectlink" || $itemArray[0] == "break") 
    		//$NewItems[$NewItemsArrayCounter++] .= $Items[$i];
    		array_push($NewItems,$Items[$i]); 		
    }		    	      	    
           
    // Keep a count of all objects that are displayed to properly align them
    $objectCount = 0;
    
    // Maximum length for object header
	$SUBLEVELHEADER_MAXLEN = 50; /*23*/

	// Display each object in the list as a subsection box 
	for ($i = 0; $i < ceil(sizeof($NewItems)); $i++) {    	        
    	$itemArray = split(":", $NewItems[$i]);
    	// Check if there is a break here and show a horizontal line and reset the DIV styling properties
    	if ($itemArray[0] == "break") {
    		$objectCount = 0;
    		echo "<div style=\"clear: both;\"></div><hr width=\"98%\">";
    		continue;
    	}    	
    	
    	// Gravity comes from the left so all boxes will slide around and align nicely
		$side = "style=\"float:left\"";    	    	    	
    	// This is a child object with only one entry
    	if ($itemArray[0] == "object" && $itemArray[4] == "s") {
    		$childresult = mysql_query("SELECT * FROM $itemArray[2] WHERE ClassName=\"".$itemArray[3]."\" AND ParentClassNameID=\"$classNameID\"");
    		$childArray = mysql_fetch_array($childresult,MYSQL_NUM);
    		// Get the object name and fit to the sublevel box    
			$sublevelheaderlength = strlen($itemArray[1]);
			if ($sublevelheaderlength < $SUBLEVELHEADER_MAXLEN)
				$sublevelheader = $itemArray[1];
			else {  				
				$sublevelheader = substr($itemArray[1],0,$SUBLEVELHEADER_MAXLEN) . "...";
			}
    		if (mysql_num_rows($childresult) > 0) {
    			echo "    <div $side class=\"sublevel\"><span style=\"float:left\" id=\"".$itemArray[1]."_arrow\"><img src='/".$source_location."/images/arrow_closed.gif' onclick='toggleArrow(\"".$itemArray[1]."\",\"$source_location\");Effect.toggle(\"".$itemArray[1]."_content\",\"blind\",{duration: 0.2})' /></span><span title=\"".$itemArray[1]."\" class=\"subcontentheader\"><strong>$sublevelheader</strong></span>&nbsp;\n";
    		}
			else
				if (in_array($_SESSION{'ProjectID'},$ProjectAdmins) && empty($childArray))					
					echo "    <div $side class=\"sublevel\"><span title=\"".$itemArray[1]."\" class=\"subcontentheader\">$sublevelheader</span><span style=\"float:right;margin-right:2px;\"><a href=\"/".$source_location."/switchboard.php?UniqueName=".$itemArray[3]."&ShowAdd=1\" title=\"Add ".$itemArray[1]."\"/><img class=\"addobject\" src=\"images/add.png\" alt=\"\"/></a></span>&nbsp;\n";
				else							
					echo "    <div $side class=\"sublevel\"><span title=\"".$itemArray[1]."\" class=\"subcontentheader\">$sublevelheader</span>&nbsp;\n";
			if (isset($_GET['AddedUniqueName']) && $_GET['AddedUniqueName']==$itemArray[3]) 							
				echo "     <div class=\"subcontent\" id=\"".$itemArray[1]."_content\" style=\"display: block\">\n";			
			else
				echo "     <div class=\"subcontent\" id=\"".$itemArray[1]."_content\" style=\"display: none\">\n"; 		    		    									
			if (!empty($childArray))   	
				echo "      <ul class=\"single-list\"><li><a class=\"objectitem\" href='/".$source_location."/switchboard.php?UniqueName=".$itemArray[3]."&View=1&ClassID=".$childArray[0]."'>".$childArray[1]."</a></li></ul>\n";			
			echo     
			"     <p />".        
	   		"     </div>\n".
	 		"     </div>\n";		    			    	
    	}
    	// This is a shared object reference	
		if (($itemArray[0] == "sharedobject" || $itemArray[0] == "objectlink") && $itemArray[4] == "s") {			
			$childresult = mysql_query("SELECT SharedObjectLink.SharedObjectLinkID,".$itemArray[2].".Name,".$itemArray[2].".ID FROM SharedObjectLink, $itemArray[2] WHERE SharedObjectLink.SharedObjectID=".$itemArray[2].".ID"." AND SharedObjectLink.ParentClassNameID=\"$classNameID\" AND SharedObjectLink.SharedObjectClass=\"$itemArray[3]\"");
			$childArray = mysql_fetch_array($childresult,MYSQL_NUM);
			// Get the object name and fit to the sublevel box    
			$sublevelheaderlength = strlen($itemArray[1]);
			if ($sublevelheaderlength < $SUBLEVELHEADER_MAXLEN)
				$sublevelheader = $itemArray[1];
			else {  				
				$sublevelheader = substr($itemArray[1],0,$SUBLEVELHEADER_MAXLEN) . "...";
			}
    		if (mysql_num_rows($childresult) > 0) {
    			echo "    <div $side class=\"sublevel\"><span style=\"float:left\" id=\"".$itemArray[1]."_arrow\"><img src='/".$source_location."/images/arrow_closed.gif' onclick='toggleArrow(\"".$itemArray[1]."\",\"$source_location\");Effect.toggle(\"".$itemArray[1]."_content\",\"blind\",{duration: 0.2})' /></span><span title=\"".$itemArray[1]."\" class=\"subcontentheader\"><strong>$sublevelheader</strong></span>&nbsp;\n";
    		}				
			else
				if (in_array($_SESSION{'ProjectID'},$ProjectAdmins) && empty($childArray))					
					echo "    <div $side class=\"sublevel\"><span title=\"".$itemArray[1]."\" class=\"subcontentheader\">$sublevelheader</span><span style=\"float:right;margin-right:2px;\"><a href=\"/".$source_location."/switchboard.php?UniqueName=".$itemArray[3]."&ShowAddID=1&&ParentUniqueName=$UniqueName\" title=\"Add ".$itemArray[1]."\"/><img class=\"addobject\" src=\"images/add.png\" alt=\"\"/></a></span>&nbsp;\n";
				else					
					echo "    <div $side class=\"sublevel\"><span title=\"".$itemArray[1]."\" class=\"subcontentheader\">$sublevelheader</span>&nbsp;\n";
			if (isset($_GET['AddedUniqueName']) && $_GET['AddedUniqueName']==$itemArray[3]) 							
				echo "     <div class=\"subcontent\" id=\"".$itemArray[1]."_content\" style=\"display: block\">\n";			
			else
				echo "     <div class=\"subcontent\" id=\"".$itemArray[1]."_content\" style=\"display: none\">\n";		 		    																							
			if (!empty($childArray)) {
		 		echo "      <ul class=\"single-list\"><li><a class=\"objectitem\" href='/".$source_location."/switchboard.php?UniqueName=".$itemArray[3]."&View=1&ClassID=".$childArray[2]."&ParentClassNameID=$classNameID&ParentDisplayName=$DisplayName&GoingToSharedObject=1'>".$childArray[1]."</a>\n";			 
		 		if (in_array($_SESSION{'ProjectID'},$ProjectAdmins))			 	
		 			echo "      <a title=\"Delete Link\" onclick=\"return confirmdelete('link')\" href='/".$source_location."/switchboard.php?UniqueName=".$itemArray[3]."&DeleteLink=1&ClassID=".$childArray[0]."&ParentUniqueName=$UniqueName'><font color=\"red\"><img class=\"deletelink\" src=\"images/cross.png\" alt=\"&nbsp;x\"/></font></a>\n";			
		 		echo "</li></ul>";
			}			
			echo     
			"      <p />".			
	   		"     </div>\n".
	 		"     </div>\n";		    	    	    
    	}  		
    	// This is a child object with multiple entries
    	if ($itemArray[0] == "object" && $itemArray[4] == "m") {	 			
    		$childresult = mysql_query("SELECT * FROM $itemArray[2] WHERE ClassName=\"".$itemArray[3]."\" AND ParentClassNameID=\"$classNameID\" ORDER BY Ranking ");
    		// Get the object name and fit to the sublevel box    
			$sublevelheaderlength = strlen($itemArray[1]);
			if ($sublevelheaderlength < $SUBLEVELHEADER_MAXLEN)
				$sublevelheader = $itemArray[1];
			else {  				
				$sublevelheader = substr($itemArray[1],0,$SUBLEVELHEADER_MAXLEN) . "...";
			}
    		if (mysql_num_rows($childresult) > 0)
				if (in_array($_SESSION{'ProjectID'},$ProjectAdmins))					
					echo "    <div $side class=\"sublevel\"><span style=\"float:left\" id=\"".$itemArray[1]."_arrow\"><img src='/".$source_location."/images/arrow_closed.gif' onclick='toggleArrow(\"".$itemArray[1]."\",\"$source_location\");Effect.toggle(\"".$itemArray[1]."_content\",\"blind\",{duration: 0.2})' /></span><span title=\"".$itemArray[1]."\" class=\"subcontentheader\"><strong>$sublevelheader</strong></span><span style=\"float:right;margin-right:2px;\"><a href=\"/".$source_location."/switchboard.php?UniqueName=".$itemArray[3]."&ShowAdd=1\" title=\"Add ".$itemArray[1]."\" /><img class=\"addobject\" src=\"images/add.png\" /></a></span>&nbsp;\n";					
				else
					echo "    <div $side class=\"sublevel\"><span style=\"float:left\" id=\"".$itemArray[1]."_arrow\"><img src='/".$source_location."/images/arrow_closed.gif' onclick='toggleArrow(\"".$itemArray[1]."\",\"$source_location\");Effect.toggle(\"".$itemArray[1]."_content\",\"blind\",{duration: 0.2})' /></span><span title=\"".$itemArray[1]."\" class=\"subcontentheader\"><strong>$sublevelheader</strong></span>&nbsp;\n";
			else
				if (in_array($_SESSION{'ProjectID'},$ProjectAdmins))					
					echo "    <div $side class=\"sublevel\"><span title=\"".$itemArray[1]."\" class=\"subcontentheader\">$sublevelheader</span><span style=\"float:right;margin-right:2px;\"><a href=\"/".$source_location."/switchboard.php?UniqueName=".$itemArray[3]."&Multiple=1&ShowAdd=1\" title=\"Add ".$itemArray[1]."\" /><img class=\"addobject\" src=\"images/add.png\" /></a></span>&nbsp;\n";										
				else					
					echo "    <div $side class=\"sublevel\"><span title=\"".$itemArray[1]."\" class=\"subcontentheader\">$sublevelheader</span>&nbsp;\n";
			if (isset($_GET['AddedUniqueName']) && $_GET['AddedUniqueName']==$itemArray[3]) 							
				echo "     <div class=\"subcontent\" id=\"".$itemArray[1]."_content\" style=\"display: block\">\n";			
			else
				echo "     <div class=\"subcontent\" id=\"".$itemArray[1]."_content\" style=\"display: none\">\n";				
			echo 		    						
			"      <ul class=\"sortable-list\" id=\"".$itemArray[3]."_objects\">";
			$objectcount = 0;							
			while ($childArray = mysql_fetch_array($childresult,MYSQL_NUM)) {			 
			 if ($objectcount % 2 == 1)
			 	echo "      <li id=\"".$itemArray[3]."_$objectcount\"><a class=\"objectitem2\" href='/".$source_location."/switchboard.php?UniqueName=".$itemArray[3]."&View=1&ClassID=".$childArray[0]."'>".$childArray[1]."</a><img class=\"moveobject\" title=\"Click and drag to reorder object\" src=\"images/arrows_updown.png\"/></li>\n";
			 else
			 	echo "      <li id=\"".$itemArray[3]."_$objectcount\"><a class=\"objectitem\" href='/".$source_location."/switchboard.php?UniqueName=".$itemArray[3]."&View=1&ClassID=".$childArray[0]."'>".$childArray[1]."</a><img class=\"moveobject\" title=\"Click and drag to reorder object\" src=\"images/arrows_updown.png\"/></li>\n";
			 $objectcount = $objectcount + 1;
			}
			echo 
            "    </ul>\n".
            "    <script type=\"text/javascript\">\n".
            "    function update".$itemArray[3]."() {\n".    		    		
    		"     var options = {\n".
   			"       method : 'post',\n".
   			"		postBody: 'ClassName=".$itemArray[3]."&Table=".$itemArray[2]."&ParentClassNameID=$classNameID&'+Sortable.serialize('".$itemArray[3]."_objects')\n".    		
            "     };\n".
 			"     new Ajax.Request('php/processSorting.php', options);\n". 			     
			"    }\n".
			"    try {Sortable.create('".$itemArray[3]."_objects', { onUpdate:update".$itemArray[3]." });} catch (err) {window.location.reload();}\n".
			"    </script>";			
			echo     
			"     <p />".        
	   		"     </div>\n".
	 		"     </div>\n";
	 		// Unset the original order because a new one will be created
	 		$orginialObjectOrderName = $itemArray[3] . "OriginalOrder";
        	unset($_SESSION[$orginialObjectOrderName]);			
    	}
    	// This is a shared object reference with multiple entries
    	if (($itemArray[0] == "sharedobject" || $itemArray[0] == "objectlink") && $itemArray[4] == "m") {				
    		$childresult = mysql_query("SELECT SharedObjectLink.SharedObjectLinkID,".$itemArray[2].".Name,".$itemArray[2].".ID,SharedObjectLink.Ranking FROM SharedObjectLink, $itemArray[2] WHERE SharedObjectLink.SharedObjectID=".$itemArray[2].".ID"." AND SharedObjectLink.ParentClassNameID=\"$classNameID\" AND SharedObjectLink.SharedObjectClass=\"$itemArray[3]\" ORDER BY Ranking ");    		    	
    		// Get the object name and fit to the sublevel box    
			$sublevelheaderlength = strlen($itemArray[1]);			
			if ($sublevelheaderlength < $SUBLEVELHEADER_MAXLEN)
				$sublevelheader = $itemArray[1];
			else {  				
				$sublevelheader = substr($itemArray[1],0,$SUBLEVELHEADER_MAXLEN) . "...";
			}
			if (mysql_num_rows($childresult) > 0) 
				if (in_array($_SESSION{'ProjectID'},$ProjectAdmins))
					echo "    <div $side class=\"sublevel\"><span style=\"float:left\" id=\"".$itemArray[1]."_arrow\"><img src='/".$source_location."/images/arrow_closed.gif' onclick='toggleArrow(\"".$itemArray[1]."\",\"$source_location\");Effect.toggle(\"".$itemArray[1]."_content\",\"blind\",{duration: 0.2})' /></span><span title=\"".$itemArray[1]."\" class=\"subcontentheader\"><strong>$sublevelheader</strong></span><span style=\"float:right;margin-right:2px;\"><a href=\"/".$source_location."/switchboard.php?UniqueName=".$itemArray[3]."&ShowAddID=1&Multiple=1&ParentUniqueName=$UniqueName\" title=\"Add ".$itemArray[1]."\"/><img class=\"addobject\" src=\"images/add.png\" alt=\"\"/></a></span>&nbsp;\n";
				else
					echo "    <div $side class=\"sublevel\"><span style=\"float:left\" id=\"".$itemArray[1]."_arrow\"><img src='/".$source_location."/images/arrow_closed.gif' onclick='toggleArrow(\"".$itemArray[1]."\",\"$source_location\");Effect.toggle(\"".$itemArray[1]."_content\",\"blind\",{duration: 0.2})' /></span><span title=\"".$itemArray[1]."\" class=\"subcontentheader\"><strong>$sublevelheader</strong></span>&nbsp;\n";
			else
				if (in_array($_SESSION{'ProjectID'},$ProjectAdmins))					
					echo "    <div $side class=\"sublevel\"><span title=\"".$itemArray[1]."\" class=\"subcontentheader\">$sublevelheader</span><span style=\"float:right;margin-right:2px;\"><a href=\"/".$source_location."/switchboard.php?UniqueName=".$itemArray[3]."&ShowAddID=1&Multiple=1&ParentUniqueName=$UniqueName\" title=\"Add ".$itemArray[1]."\"/><img class=\"addobject\" src=\"images/add.png\" alt=\"\"/></a></span>&nbsp;\n";
				else					
					echo "    <div $side class=\"sublevel\"><span title=\"".$itemArray[1]."\" class=\"subcontentheader\">$sublevelheader</span>&nbsp;\n";
			if (isset($_GET['AddedUniqueName']) && $_GET['AddedUniqueName']==$itemArray[3]) 							
				echo "     <div class=\"subcontent\" id=\"".$itemArray[1]."_content\" style=\"display: block\">\n";			
			else
				echo "     <div class=\"subcontent\" id=\"".$itemArray[1]."_content\" style=\"display: none\">\n";
			echo 			 		    								
			"      <ul class=\"sortable-list\" id=\"".$itemArray[3]."_objects\">";
			$objectcount = 0;						
			while ($childArray = mysql_fetch_array($childresult,MYSQL_NUM)) {
				if ($objectcount % 2 == 1)
			 		echo "      <li id=\"".$itemArray[3]."_$objectcount\"><a class=\"objectitem2\" href='/".$source_location."/switchboard.php?UniqueName=".$itemArray[3]."&View=1&ClassID=".$childArray[2]."&ParentClassNameID=$classNameID&ParentDisplayName=$DisplayName&GoingToSharedObject=1'>".$childArray[1]."</a><img class=\"moveobject\" title=\"Click and drag to reorder object\" src=\"images/arrows_updown.png\"/>\n";
			 	else
					echo "      <li id=\"".$itemArray[3]."_$objectcount\"><a class=\"objectitem\" href='/".$source_location."/switchboard.php?UniqueName=".$itemArray[3]."&View=1&ClassID=".$childArray[2]."&ParentClassNameID=$classNameID&ParentDisplayName=$DisplayName&GoingToSharedObject=1'>".$childArray[1]."</a><img class=\"moveobject\" title=\"Click and drag to reorder object\" src=\"images/arrows_updown.png\"/>\n";			 					 
			 	if (in_array($_SESSION{'ProjectID'},$ProjectAdmins))			 	
			 		echo "      <a title=\"Delete Link\" onclick=\"return confirmdelete('link')\" href='/".$source_location."/switchboard.php?UniqueName=".$itemArray[3]."&DeleteLink=1&ClassID=".$childArray[0]."&ParentUniqueName=$UniqueName'><font color=\"red\"><img class=\"deletelink\" src=\"images/cross.png\" alt=\"&nbsp;x\"/></font></a>\n";
			 	echo "</li>";
				$objectcount = $objectcount + 1;
			}
			echo 
            "    </ul>\n".
            "    <script type=\"text/javascript\">\n".
            "    function update".$itemArray[3]."() {\n".    		    		
    		"     var options = {\n".
   			"       method : 'post',\n".
   			"		postBody: 'ClassName=".$itemArray[3]."&Table=".$itemArray[2]."&ParentClassNameID=$classNameID&'+Sortable.serialize('".$itemArray[3]."_objects')\n".    		
            "     };\n".
 			"     new Ajax.Request('php/processSharedObjectSorting.php', options);\n". 			     
			"    }\n".
			"    try {Sortable.create('".$itemArray[3]."_objects', { onUpdate:update".$itemArray[3]." });} catch (err) {window.location.reload();}\n".
			"    </script>";
			echo     
			"      <p />".
	   		"     </div>\n".
	 		"     </div>\n";	
	 		// Unset the original order because a new one will be created
	 		$orginialObjectOrderName = $itemArray[3] . "OriginalOrder";
        	unset($_SESSION[$orginialObjectOrderName]);			
    	}        		    	
    } 
    // Reset the DIV styling so the footer is formed correctly
    echo "<div style=\"clear: both;\">";
        
    echo 
	"</div>\n".
	"<div style=\"clear:both;\"></div>\n";    	         	  
	
	// Show shared object owner
	if (!empty($SharedName)) {
		if ($SharedName != "Projects") {
			$OwningProjectArray = mysql_query("SELECT Name FROM BasicClass WHERE ClassName=\"Project\" AND ID=".$ObjectDataArray{'OwningProject'}."");
			$OwningProject = mysql_fetch_array($OwningProjectArray,MYSQL_ASSOC);
			if (isset($AllowSharing) && $AllowSharing == "Yes") {
				echo 
				"  <table border=\"0\">\n".
				"   <tr><td><b>Managed By: </b></td><td>".$OwningProject{'Name'}."</td></tr>\n".
				"  </table>";
			}
			
			// Edit/delete options should be available only to project admins for the shared objects owner
			if (in_array($ObjectDataArray{'OwningProject'},$ProjectAdmins)) {
				echo
		    	"  <form class=\"buttonforms\" name=\"edit\" method=\"get\" action=\"/".$source_location."/switchboard.php\">\n".        	
		    	"   <div class=\"buttons\"><button type=\"submit\" class=\"edit\" /><img src=\"images/page_edit.png\" alt=\"\"/>Edit</button></div>\n".    
		    	"   <input type=\"hidden\" name=\"UniqueName\" value=\"$UniqueName\" />\n".
		    	"   <input type=\"hidden\" name=\"Edit\" value=\"Edit\" />\n".
		    	"  </form>\n".
		    	"  <form class=\"buttonforms\" name=\"delete\" method=\"get\" action=\"/".$source_location."/switchboard.php\">\n".       
		    	"   <div class=\"buttons\"><button type=\"submit\" class=\"delete\" onclick=\"return confirmdelete('object')\" /><img src=\"images/cross.png\" alt=\"\"/>Delete</button></div>\n".    	
		    	"   <input type=\"hidden\" name=\"UniqueName\" value=\"$UniqueName\" />\n";
		    	if (!empty($SharedName)) {
		    		echo "   <input type=\"hidden\" name=\"ReturnURL\" value=\"$returnURL\" />\n";
		    	}
		    	echo
		    	"   <input type=\"hidden\" name=\"Delete\" value=\"Delete\" />\n".
		    	"  </form>\n"; 
			}
		}
	}	
	
	// Edit/delete options should be available only to project admins for non-shared Objects
	if (in_array($_SESSION{'ProjectID'},$ProjectAdmins) && (empty($SharedName) || $SharedName=="Projects")) {
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
 	" </div>\n";     	 	  
 	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/php/footer.php");
 	echo 
 	"</div>\n".  	
	"</body>\n".
	"</html>\n";    
	
	// Close the session
    require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/mysql/close.php");	
} /////////////////////

/* Request to delete object */
if (isset($_GET['Delete'])) {
 	// Delete the Object
 	$classIDname = ($UniqueName . "ID"); 
 	mysql_query("DELETE FROM $TableName WHERE ID=$_SESSION[$classIDname]");
 	 
 	// Remove all references in the User lists
 	$Users = mysql_query("SELECT * FROM Users");
 	// Go through all the users
	while ($UserInfo = mysql_fetch_array($Users,MYSQL_ASSOC)) {
		// Get the projects
		$ProjectIDs = $UserInfo{'Projects'};
		// Remove the project ID 
		$ProjectIDs = str_replace($_SESSION[$classIDname],"",$ProjectIDs);
		// Update the User
		$sql = "UPDATE Users SET Projects='$ProjectIDs' WHERE Username='".$UserInfo['Username']."'";				
		mysql_query($sql);	
		
		// Get the project admins
		$ProjectAdminIDs = $UserInfo{'ProjectsAdmin'};
		// Remove the project ID 
		$ProjectAdminIDs = str_replace($_SESSION[$classIDname],"",$ProjectAdminIDs);
		// Update the User
		$sql = "UPDATE Users SET ProjectsAdmin='$ProjectAdminIDs' WHERE Username='".$UserInfo['Username']."'";
		mysql_query($sql);							
	}
	
	// Remove this now invalid SESSION variable
 	unset($_SESSION[$classIDname]);
 
 	// Close the session
 	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/mysql/close.php");

 	// Go back to the parent or List, depending on where the object was deleted from  	
 	if ($UniqueName == "Project")
 		echo "<META HTTP-EQUIV='Refresh' CONTENT='0; URL=/".$source_location."/switchboard.php?UniqueName=$UniqueName&List=1'>";
 	else {
 		if (!empty($SharedName)) 			
			echo "<META HTTP-EQUIV='Refresh' CONTENT='0; URL=".$_GET['ReturnURL']."'>";
		else {
			$parentIDname = ($ParentUniqueName . "ID");
			echo "<META HTTP-EQUIV='Refresh' CONTENT='0; URL=/".$source_location."/switchboard.php?UniqueName=$ParentUniqueName&View=1&ClassID=".$_SESSION[$parentIDname]."'>";
		}
 	} 		
 	  	 
 	exit();	
} /////////////////////

/* Request to show a list of a specific object, an example is the Project list */
if (isset($_GET['List'])) {
	// Kill the Project variable if here for Projects
	if ($UniqueName == "Project") {
		unset($_SESSION['ProjectID']);
		
		// Reset breadcrumb tracker
		unset($_SESSION['Breadcrumbs']);
		$_SESSION['Breadcrumbs'][] = array();
	}
	 	
	// Create a session variable to take back to the calling page for shared objects
	else {		
		if (isset($_GET['ParentUniqueName'])) {  
			$parentIDname = $_GET['ParentUniqueName'] . "ID";  
		 	$_SESSION['ReturnTo'] = $_GET['ParentUniqueName'];
		}
		else 
			$parentIDname = $_SESSION['ReturnTo'] . "ID";
	}
	 	
	// Get the database information for this object 	
	$classIDname = ($UniqueName . "ID");
	$ProjectAdmins = explode(",",$_SESSION['projectsadmin']); 

 	echo 
	"<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n".
	"<html xmlns=\"http://www.w3.org/1999/xhtml\">\n".
	"<head>\n".
	"<link type=\"text/css\" href=\"css/layout.css\" rel=\"stylesheet\" media=\"screen\" />\n".
	"<title>$SharedName</title>\n".
	"<link rel=\"shortcut icon\" href=\"images/favicon.ico\" type=\"image/x-icon\" />\n".
	"<script language=\"javascript\" src=\"/".$source_location."/js/functions.js\" type=\"text/javascript\"></script>\n".
	"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n".
	"</head>\n".
	"<body>\n".
	"<div id=\"container\">\n".
 	" <div id=\"banner\" ><a href=\"index.php\"><img src=\"images/header.jpg\" alt=\"\" border=\"0\" /></a>\n".
  	"  <div id=\"addressbar\"><h1><a href=\"switchboard.php?UniqueName=Project&List=1\">Projects</a> >> "; echo showProjectName(); echo "</h1></div>\n".
  	"  <div align=\"right\" id=\"loginbar\"><h3>";require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/php/showlogin.php");echo "</h3></div>\n".  	
 	" </div>\n". 	
 	" <div id=\"content\">\n".
  	"  <h2>$SharedName</h2>\n".
  	"  <p />\n".
  	"  <ul class=\"list\">\n";
  	$childresult = mysql_query("SELECT * FROM $TableName WHERE SharedName=\"$SharedName\" ORDER BY Name");
  	while ($childArray = mysql_fetch_array($childresult,MYSQL_ASSOC)) {  
  		// List only projects allowed to user
  		if ($UniqueName == "Project") {
			$ProjectMembers = explode(",",$_SESSION['projects']);   
		  	if (in_array($childArray{'ID'},$ProjectMembers))
		  		echo "<li class=\"listrow\"><a class=\"listitem\" href=\"switchboard.php?UniqueName=$UniqueName&View=1&ClassID=". $childArray{'ID'} ."\">". $childArray{'Name'} . "</a></li>";
	  		else
	  			echo "<li class=\"listrownonmember\">".$childArray{'Name'}."<input type='button' class='buttons' value='Request' name='' onclick=\"requestproject('".$childArray{'Name'}."','". $_SESSION['username'] ."')\" /></li>";
  		}
  		// These are shared objects then.  Only list the ones that belong to the project
  		else {
  			if ($childArray{'Project'} != 0) {
  				if ($childArray{'Project'} == $_SESSION['ProjectID'])
  					echo "<li class=\"listrow\"><a class=\"listitem\" href=\"switchboard.php?UniqueName=$UniqueName&View=1&ClassID=". $childArray{'ID'} ."\">". $childArray{'Name'} . "</a></li>";
			}
	  		else 
				echo "<li class=\"listrow\"><a class=\"listitem\" href=\"switchboard.php?UniqueName=$UniqueName&View=1&ClassID=". $childArray{'ID'} ."\">". $childArray{'Name'} . "</a></li>";
		} 
 	}
 	echo 
 	"  </ul>\n".
  	" <p />\n";
  	if ($UniqueName == "Project" || in_array($_SESSION{'ProjectID'},$ProjectAdmins)) 		
		echo " <div class=\"buttons\"><button type=\"button\" class=\"add\" name=\"ShowAdd\" value=\"Add\" onclick=window.location.href=\"/".$source_location."/switchboard.php?UniqueName=$UniqueName&ShowAdd=1&FromList=1\" /><img src=\"images/add.png\" alt=\"\"/>Add Project</button></div>\n";
  	echo
  	" <p />\n";
  	if ($UniqueName != "Project")  		
  		echo " <div class=\"buttons\"><button type=\"button\" class=\"add\" name=\"Return\" value=\"Return\" \"/".$source_location."/switchboard.php?UniqueName=".$_SESSION['ReturnTo']."&View=1&ClassID=".$_SESSION[$parentIDname]."\" /><img src=\"images/arrow_left.png\" alt=\"\"/>Return</button></div>\n"; 	
 	echo " </div>\n";
 	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/php/footer.php");
 	echo
 	"</div>\n".
	"</div>\n".
	"</body></html>\n";	
	
	// Close the session
    require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/mysql/close.php");
}

/* Request to create a reference. This emulates Foreign Key relationships */
if (isset($_GET['AddID'])) { 
 	// Get the parent reference
 	$parentIDname = $_GET['ParentUniqueName'] . "ID";	
 	$parentClassNameID = $_GET['ParentUniqueName'] . $_SESSION[$parentIDname];
 
 	// Allows multiple items to be added at one time
 	if (!isset($_GET['AddNew'])) { 
	 	for ($i = 0; $i < sizeof($_GET['ID']); $i++ ) {
			$sql =  "INSERT INTO SharedObjectLink (ParentClassNameID,SharedObjectClass,SharedObjectID)".
	     			"VALUES (\"$parentClassNameID\",\"$UniqueName\",\"".$_GET['ID'][$i]."\")";
			mysql_query($sql);	
	 	}
 	}
 	else {
 		$ID = mysql_insert_id();
 		$sql =  "INSERT INTO SharedObjectLink (ParentClassNameID,SharedObjectClass,SharedObjectID)".
	     			"VALUES (\"$parentClassNameID\",\"$UniqueName\",\"$ID\")";
		mysql_query($sql);			
 	} 
 
 	// Close the session
 	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/mysql/close.php");
 	
 	// Return to the parent page
 	echo "<META HTTP-EQUIV='Refresh' CONTENT='0; URL=/".$source_location."/switchboard.php?UniqueName=".$_GET['ParentUniqueName']."&AddedUniqueName=$UniqueName&View=1&ClassID=".$_SESSION[$parentIDname]."'>";
 	exit();  
}
///////////////////

/* Request to show add object reference page */
if (isset($_GET['ShowAddID'])) {		
	// Get the class information
	$classIDname = ($UniqueName . "ID");
		 
	echo 
	"<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n".
	"<html xmlns=\"http://www.w3.org/1999/xhtml\">\n".
	"<head>\n".
	"<link type=\"text/css\" href=\"/".$source_location."/css/layout.css\" rel=\"stylesheet\" media=\"screen\" />\n".
	"<title>Add $DisplayName</title>\n".
	"<link rel=\"shortcut icon\" href=\"/".$source_location."/images/favicon.ico\" type=\"image/x-icon\" />\n".
	"<script language=\"javascript\" src=\"/".$source_location."/js/functions.js\" type=\"text/javascript\"></script>\n".
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
    "  <form name=\"add\" method=\"get\" action=\"/".$source_location."/switchboard.php\">\n".    
    "   <table border=\"0\">\n";
    // Adding a shared object reference
    if (!empty($SharedName)) {
	    //Allows multiple items to be added
	    if (isset($_GET['Multiple'])) {
	    	echo
			"    <tr><td valign=\"top\"><b>Name: </b></td><td><SELECT Name=\"ID[]\" MULTIPLE>";	    	
		 	$childresult = mysql_query("SELECT * FROM $TableName WHERE SharedName=\"$SharedName\" ORDER BY Name"); 	
		  	while ($childArray = mysql_fetch_array($childresult,MYSQL_ASSOC)) {  
		  		if ($childArray{'isShared'} == 1 || $childArray{'OwningProject'} == $_SESSION['ProjectID'])
		   			echo "<option value='" . $childArray{'ID'} ."'>". $childArray{'Name'} ."</option>";
		  	}
	    }	    
	    // Just a single item
	    else {
	    	echo
	    	"    <tr><td valign=\"top\"><b>Name: </b></td><td><SELECT Name=\"ID[]\">";	    	
		 	$childresult = mysql_query("SELECT * FROM $TableName WHERE SharedName=\"$SharedName\""); 	
		  	while ($childArray = mysql_fetch_array($childresult,MYSQL_ASSOC)) {  
		  		if ($childArray{'isShared'} == 1 || $childArray{'OwningProject'} == $_SESSION['ProjectID'])
		   			echo "<option value='" . $childArray{'ID'} ."'>". $childArray{'Name'} ."</option>";
		  	}
	    }
	    echo 
	    "     </select></td></tr>".    	    
	    "    <tr><td /><td><div class=\"buttons\"><button type=\"submit\" class=\"add\" /><img src=\"images/add.png\" alt=\"\"/>Add Link(s)</button></div></td></tr>\n".    	
	    "    <input type=\"hidden\" name=\"UniqueName\" value=\"$UniqueName\" />\n".
	    "    <input type=\"hidden\" name=\"ParentUniqueName\" value=\"".$_GET['ParentUniqueName']."\" />\n".
	    "    <input type=\"hidden\" name=\"AddID\" value=\"AddID\" />\n".                               
	    "   </table>\n".
	    "  </form>\n".
	    "  <form name=\"addnew\" method=\"get\" action=\"/".$source_location."/switchboard.php\" onSubmit=\"return isNotEmpty(Name);\">\n".    
	    "   <table border=\"0\">\n".
		"    <tr><td>Or Add New: </td><td><input type=\"text\" name=\"Name\" /></td></tr>";
		if ($UniqueName!="Project" && isset($AllowSharing) && $AllowSharing=="Yes") {
     		echo
			"    <tr><td><input type=\"radio\" name=\"shared\" value=\"no\" checked=\"checked\" title=\"New object won't be shared with other Projects\" />Don't share with other projects<br />\n".
	     	"            <input type=\"radio\" name=\"shared\" value=\"yes\" title=\"Object will be available to other Projects\"/>Share with other projects<br /></td></tr>\n";
		}
		else
     		echo "       <input type=\"hidden\" name=\"shared\" value=\"no\" />\n";
		// Remove the navigation stack options from a Cancel button 
		if(isset($_SERVER['HTTP_REFERER'])){
		    $previousURL = $_SERVER['HTTP_REFERER'];
		    $returnURL = str_replace("GoingToSharedObject","",$previousURL);
		    $returnURL = str_replace("ComingFromSharedObject","",$returnURL);
		}     		
		echo 
		"    <tr><td><div class=\"buttons\"><button type=\"submit\" class=\"add\" /><img src=\"images/add.png\" alt=\"\"/>Add New</button></div></td></tr>\n".
    	"        <td><div class=\"buttons\"><button type=\"button\" class=\"cancel\" onclick=\"javascript:window.location.href = '$returnURL'\" /><img src=\"images/cancel.png\" alt=\"\"/>Cancel</button></div></td></tr>\n".    
	    "    <input type=\"hidden\" name=\"AddNew\" value=\"1\" />\n".
	    "    <input type=\"hidden\" name=\"AddID\" value=\"AddID\" />\n".    
	    "    <input type=\"hidden\" name=\"Add\" value=\"1\" />\n".
	    "    <input type=\"hidden\" name=\"UniqueName\" value=\"$UniqueName\" />\n".
	    "    <input type=\"hidden\" name=\"ParentUniqueName\" value=\"".$_GET['ParentUniqueName']."\" />\n";	    
	    if (isset($_GET['Multiple']))
	    	echo "    <input type=\"hidden\" name=\"URL\" value=\"".$_SERVER['PHP_SELF']."?UniqueName=$UniqueName&ShowAddID=1&Multiple=".$_GET['Multiple']."&ParentUniqueName=".$_GET['ParentUniqueName']."\" />\n";
	    else
			echo "    <input type=\"hidden\" name=\"URL\" value=\"".$_SERVER['PHP_SELF']."?UniqueName=$UniqueName&ShowAddID=1&ParentUniqueName=".$_GET['ParentUniqueName']."\" />\n";	            
	    echo 
		"    <input type=\"hidden\" name=\"UniqueName\" value=\"$UniqueName\" />\n".       
	    "   </table>\n".
	    "  </form>\n".
	    " </div>\n";
    }
    // Or adding just a link to a regular object
    else {
    	echo
    	"    <tr><td valign=\"top\"><b>Name: </b></td><td><SELECT Name=\"ID[]\">";	    	
	 	$childresult = mysql_query("SELECT * FROM $TableName WHERE ClassName=\"$UniqueName\"");	 	
	  	while ($childArray = mysql_fetch_array($childresult,MYSQL_ASSOC)) {  
	  		if ($childArray{'Project'} == 0 || $childArray{'Project'} == $_SESSION['ProjectID'])
	   			echo "<option value='" . $childArray{'ID'} ."'>". $childArray{'Name'} ."</option>";
	  	}
	  	// Remove the navigation stack options from a Cancel button 
	    $previousURL = $_SERVER['HTTP_REFERER'];
	    $returnURL = str_replace("GoingToSharedObject","",$previousURL);
	    $returnURL = str_replace("ComingFromSharedObject","",$returnURL);	    	    
	    echo 
	    "     </select></td></tr>".    
	    "    <tr><td><input type=\"submit\" name=\"AddID\" class=\"buttons\" value=\"Add\" /></td>\n".
	    "        <td><input type=\"button\" name=\"Cancel\" class=\"buttons\" value=\"Cancel\" onclick=\"javascript:window.location.href = '$returnURL'\" /></td></tr>\n".
	    "    <input type=\"hidden\" name=\"UniqueName\" value=\"$UniqueName\" />\n".
	    "    <input type=\"hidden\" name=\"ParentUniqueName\" value=\"".$_GET['ParentUniqueName']."\" />\n".                               
	    "   </table>\n".
	    "  </form>\n".	    
	    " </div>\n";
    }
    require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/php/footer.php");
    echo 
    " </div>\n".
	"</div>\n".
	"</body></html>\n";
	
	// Close the session
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/mysql/close.php");
} /////////////////////

/* Request to delete link to shared object */
if (isset($_GET['DeleteLink'])) {
 	// Delete the reference
 	mysql_query("DELETE FROM SharedObjectLink WHERE SharedObjectLinkID=".$_GET['ClassID']);
 
 	// Close the session
 	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/mysql/close.php");
 	
 	// Return to the parent
 	$parentIDname = $_GET['ParentUniqueName'] . "ID";
 	echo "<META HTTP-EQUIV='Refresh' CONTENT='0; URL=/".$source_location."/switchboard.php?UniqueName=".$_GET['ParentUniqueName']."&View=1&ClassID=".$_SESSION[$parentIDname]."'>";
 	exit();
} /////////////////////
?>
