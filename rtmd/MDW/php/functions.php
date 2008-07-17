<?php
// Convert special characters
function convert_specials($string) 
{ 	
    $search = array(
    				"'",
					'&',
					'<',
					'>',
					'"',					
					);
	$replace = array(
					'&quot;',
					'&amp;',
					'&lt;',
					'&gt;',
					'&quot;',					
					);
								
    return str_replace($search, $replace, $string); 
}

// Trim the address part of the page so the address doesn't wrap
function showProjectName() {
 if (isset($_SESSION['ProjectID'])) {
	$Results = mysql_query("SELECT Name FROM BasicClass WHERE SharedName=\"Projects\" AND ID=". $_SESSION['ProjectID']);
	$ObjectArray = mysql_fetch_array($Results,MYSQL_ASSOC);
	$name = $ObjectArray{'Name'};
	$MAXLEN = 85;
	$length = strlen($name);
	if ($length < $MAXLEN)
		echo $name;
	else {  
		echo "...";
		echo substr($name,$length-$MAXLEN);
	}
 }
 else 
  echo "&nbsp;";
} 

// Show and trim the breadcrumb bar
function showBreadcrumbs() {	  
 if(isset($_SESSION['Breadcrumbs'])) {
 	$breadcrumbs = $_SESSION['Breadcrumbs'];
 	$trail = "";
 	for ($i = 1; $i < sizeof($breadcrumbs); $i++) { 		
 		$trail = $trail . $breadcrumbs[$i] .  " > " ;
 	} 	
	$MAXLEN = 90;
	$length = strlen($trail);
	if ($length < $MAXLEN)
		echo $trail;
	else {  
		echo "...";
		echo substr($trail,$length-$MAXLEN);
	}
 }
 else 
  echo "";
} 
?>
