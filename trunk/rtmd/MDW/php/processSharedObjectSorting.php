<?php
// Allow changing of display order for objects
    function processOrder($ClassName,$Table,$ParentClassNameID)
    {    	
		//$handle = fopen("/tmp/".$source_location."_log.txt", 'w') or die("can't open file");
    	
    	// Required externals
    	require ("../config/config.php");
		require ("../mysql/connect.php");
		require ("checklogin.php");
		
		// Form the Object list name
    	$ListName = $ClassName . "_objects";    	    	        	        
    	
    	// Check if its a valid array
        if (!isset($_POST[$ListName]) || !is_array($_POST[$ListName]))
            return;                   
                       
                     
 		// Get the Object items into an array and assign them the proper ranking
 		$childresult = mysql_query("SELECT SharedObjectLink.SharedObjectLinkID,".$Table.".Name,".$Table.".ID,SharedObjectLink.Ranking FROM SharedObjectLink, $Table WHERE SharedObjectLink.SharedObjectID=".$Table.".ID"." AND SharedObjectLink.ParentClassNameID=\"$ParentClassNameID\" AND SharedObjectLink.SharedObjectClass=\"$ClassName\" ORDER BY Ranking "); 		
 		//fwrite($handle,"SELECT SharedObjectLink.SharedObjectLinkID,".$Table.".Name,".$Table.".ID,SharedObjectLink.Ranking FROM SharedObjectLink, $Table WHERE SharedObjectLink.SharedObjectID=".$Table.".ID"." AND SharedObjectLink.ParentClassNameID=\"$ParentClassNameID\" AND SharedObjectLink.SharedObjectClass=\"$ClassName\" ORDER BY Ranking ");
                
 		// See if the original order was already created for this object for this page session
        $orginialOrderName = $ClassName . "OriginalOrder";
        if (!isset($_SESSION[$orginialOrderName])) {
        	// Create the object array
	        $i = 0;      
	        $objectIDArray = array();   	
	        while ($childArray = mysql_fetch_array($childresult,MYSQL_NUM)) {	 			
	            $objectIDArray[$i++] = $childArray[0];
	        }
	        $_SESSION[$orginialOrderName] = $objectIDArray;
        }   
        else                   
        	// Restore the original array
        	$objectIDArray = $_SESSION[$orginialOrderName];                	        	
        	        
        // Create a newly sorted array of objects based on the ranking from the table
        $i = 0;
        $newObjectIDArray = array();        
        foreach ($_POST[$ListName] as $Ranking) {
        	$newObjectIDArray[$i++] = $objectIDArray[$Ranking];
        }                         
        
        $i = 0;
        // Update the database
        foreach ($newObjectIDArray as $ID) {
        	mysql_query("UPDATE SharedObjectLink SET Ranking=$i WHERE SharedObjectLinkID=$ID");
        	//fwrite($handle,"UPDATE SharedObjectLink SET Ranking=$i WHERE SharedObjectLinkID=$ID\n");
        	$i++;
        }                    
        
        require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/mysql/close.php");
        
        //fclose($handle);
    }    
	
	// Functiont to call   	       	             
    processOrder($_POST['ClassName'],$_POST['Table'],$_POST['ParentClassNameID']);
?>