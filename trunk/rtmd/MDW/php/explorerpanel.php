<?php
// This is for a tree-like view of the Project.  No longer supported 
function refreshExplorerPanel($UniqueName,$TableName,$ID) {
	// Search 
	$classIDname = ($UniqueName . "ID");		
	echo
	"<table border=\"0\">\n". 
	" <form name=\"search\" action=\"search.php\" method=\"get\">\n".
  	"  <tr><td><input type=\"text\" name=\"term\" /></td></tr>\n".
  	"  <tr><td><div class=\"buttons\"><button type=\"submit\" class=\"search\"><img src=\"images/magnifier.png\" alt=\"\"/>Search (beta)</button></div></td></tr>\n".
  	"  <input type=\"hidden\" name=\"UniqueName\" value=\"$UniqueName\" />\n".
  	"  <input type=\"hidden\" name=\"ClassID\" value=\"".$_SESSION[$classIDname]."\" />\n".
  	"  <input type=\"hidden\" name=\"Search\" value=\"Search\" />\n".   	  	
	"  </form>\n".
	"</table>";
		 	

	//if ($_SESSION['username'] != 'admin') 
	//echo "{Explorer not implemented yet}<br>";
	//else {
	
	/*
	// Project	
	$Results = mysql_query("SELECT Name FROM Project WHERE ID=". $_SESSION['ProjectID']);
 	$ObjectArray = mysql_fetch_array($Results,MYSQL_ASSOC);
	if ($UniqueName == "Project") {  		
 		echo "<span style='cursor:default;' id='label". $ObjectArray{'Name'} ."' onclick=\"hideshow('". $ObjectArray{'Name'} ."')\">+</span> <a href='http://www.nees.lehigh.edu/".$source_location."/switchboard.php?UniqueName=Project&View=1&ClassID=".$_SESSION['ProjectID']."'><b>". $ObjectArray{'Name'} . "</b></a><p />";
  		echo "<div style='display:none;margin-left:10px;' id='". $ObjectArray{'Name'} ."'>";
	}	
	else {			
		echo "<span style='cursor:default;' id='label". $ObjectArray{'Name'} ."' onclick=\"hideshow('". $ObjectArray{'Name'} ."')\">&#151;</span> <a href='http://www.nees.lehigh.edu/".$source_location."/switchboard.php?UniqueName=Project&View=1&ClassID=".$_SESSION['ProjectID']."'>". $ObjectArray{'Name'} . "</a><p />";
  		echo "<div style='display:block;margin-left:10px;' id='". $ObjectArray{'Name'} ."'>";
	}
		
 	// Typical Experimental Tasks 	
 	$Children = array ("TestCondition");
 	$Results = mysql_query("SELECT Name,ID FROM BasicClass WHERE ClassName=\"TypicalExperimentalTask\" AND ParentClassNameID=\"$ParentClassNameID\"");
 	echo  "SELECT Name,ID FROM BasicClass WHERE ClassName=\"TypicalExperimentalTask\" AND ParentClassNameID=\"$ParentClassNameID\"";
 	$ObjectArray = mysql_fetch_array($Results,MYSQL_ASSOC);
 	if ($UniqueName == "TypicalExperimentalTask") { 		 
 		echo " <span style='cursor:default;' id='label". $ObjectArray{'Name'} ."' onclick=\"hideshow('". $ObjectArray{'Name'} ."')\">&#151;</span> <a href='http://www.nees.lehigh.edu/".$source_location."/switchboard.php?UniqueName=TypicalExperimentalTask&View=1&ClassID=".$ObjectArray{'ID'}."'><b>". $ObjectArray{'Name'} . "</b></a><p />";
  		echo " <div style='display:none;margin-left:10px;' id='". $ObjectArray{'Name'} ."'>";
 	}
 	else if (in_array($Children,$UniqueName)) {		
		echo " <span style='cursor:default;' id='label". $ObjectArray{'Name'} ."' onclick=\"hideshow('". $ObjectArray{'Name'} ."')\">&#151;</span> <a href='http://www.nees.lehigh.edu/".$source_location."/switchboard.php?UniqueName=TypicalExperimentalTask&View=1&ClassID=".$ObjectArray{'ID'}."'>". $ObjectArray{'Name'} . "</a><p />";
  		echo " <div style='display:block;margin-left:10px;' id='". $ObjectArray{'Name'} ."'>";
 	}
 	else {		
		echo " <span style='cursor:default;' id='label". $ObjectArray{'Name'} ."' onclick=\"hideshow('". $ObjectArray{'Name'} ."')\">+</span> <a href='http://www.nees.lehigh.edu/".$source_location."/switchboard.php?UniqueName=TypicalExperimentalTask&View=1&ClassID=".$ObjectArray{'ID'}."'>". $ObjectArray{'Name'} . "</a><p />";
  		echo " <div style='display:none;margin-left:10px;' id='". $ObjectArray{'Name'} ."'>";
 	}
 	$ParentClassNameID = $ObjectArray{'ParentClassNameID'};
 	
 	// Test Condition
 	$Children = array ("TestConditionTest");
 	$Results = mysql_query("SELECT Name,ID FROM BasicClass WHERE ClassName=\"TestCondition\" AND ParentClassNameID=\"$ParentClassNameID\"");
 	echo "SELECT Name,ID FROM BasicClass WHERE ClassName=\"TestCondition\" AND ParentClassNameID=\"$ParentClassNameID\""; 
 	$ObjectArray = mysql_fetch_array($Results,MYSQL_ASSOC);
 	if ($UniqueName == "TestCondition") { 		 
 		echo " <span style='cursor:default;' id='label". $ObjectArray{'Name'} ."' onclick=\"hideshow('". $ObjectArray{'Name'} ."')\">&#151;</span> <a href='http://www.nees.lehigh.edu/".$source_location."/switchboard.php?UniqueName=TypicalExperimentalTask&View=1&ClassID=".$ObjectArray{'ID'}."'><b>". $ObjectArray{'Name'} . "</b></a><p />";
  		echo " <div style='display:none;margin-left:10px;' id='". $ObjectArray{'Name'} ."'></div></div></div>";
 	}
 	else if (in_array($Children,$UniqueName)) {		
		echo " <span style='cursor:default;' id='label". $ObjectArray{'Name'} ."' onclick=\"hideshow('". $ObjectArray{'Name'} ."')\">&#151;</span> <a href='http://www.nees.lehigh.edu/".$source_location."/switchboard.php?UniqueName=TypicalExperimentalTask&View=1&ClassID=".$ObjectArray{'ID'}."'>". $ObjectArray{'Name'} . "</a><p />";
  		echo " <div style='display:block;margin-left:10px;' id='". $ObjectArray{'Name'} ."'></div></div></div>";
 	}
 	else {		
		echo " <span style='cursor:default;' id='label". $ObjectArray{'Name'} ."' onclick=\"hideshow('". $ObjectArray{'Name'} ."')\">+</span> <a href='http://www.nees.lehigh.edu/".$source_location."/switchboard.php?UniqueName=TypicalExperimentalTask&View=1&ClassID=".$ObjectArray{'ID'}."'>". $ObjectArray{'Name'} . "</a><p />";
  		echo " <div style='display:none;margin-left:10px;' id='". $ObjectArray{'Name'} ."'></div></div></div>";
 	}
 	*/
	//}
 				
}
/*
// get all the major hierarchy level names and form into a list compatible with javascript function refreshExplorerPanel()
// Sections: Project=1, Project Task=2, Test Condition=3, Test=4, Data Set=5
function refreshExplorerPanel($ProjectID,$Section,$SectionName) { 
 // get Project Name
 $Project = mysql_query("select Name FROM Project WHERE ProjectID=". $ProjectID);
 $ObjectArray = mysql_fetch_array($Project,MYSQL_ASSOC);
 $ProjectName = $ObjectArray{'Name'};
 $SectionName = eregi_replace(" ","" ,$SectionName); 
 $ProjectNameCheck = eregi_replace(" ","" ,$ProjectName);  
 if ($SectionName == $ProjectNameCheck) { 
  echo "<span style='cursor:default;' id='label". $ProjectNameCheck . $ProjectID ."' onclick=\"hideshow('". $ProjectNameCheck . $ProjectID ."')\">&#151;</span> <a href='http://www.nees.lehigh.edu/".$source_location."/Project/viewProject.php?ProjectID=". $ProjectID . "'><b>". $ProjectName . "</b></a><p />";
  echo "<div style='display:block;margin-left:10px;' id='". $ProjectNameCheck . $ProjectID ."'>";
 }
 else {
  if ($Section > 1) {
   echo "<span style='cursor:default;' id='label". $ProjectNameCheck . $ProjectID ."' onclick=\"hideshow('". $ProjectNameCheck . $ProjectID ."')\">&#151;</span> <a href='http://www.nees.lehigh.edu/".$source_location."/Project/viewProject.php?ProjectID=". $ProjectID . "'>". $ProjectName . "</a><p />";
   echo "<div style='display:block;margin-left:10px;' id='". $ProjectNameCheck . $ProjectID ."'>";
  }
  else {
   echo "<span style='cursor:default;' id='label". $ProjectNameCheck . $ProjectID ."' onclick=\"hideshow('". $ProjectNameCheck . $ProjectID ."')\">+</span> <a href='http://www.nees.lehigh.edu/".$source_location."/Project/viewProject.php?ProjectID=". $ProjectID . "'>". $ProjectName . "</a><p />";
   echo "<div style='display:none;margin-left:10px;' id='". $ProjectNameCheck . $ProjectID ."'>";	
  }
 }
   	
 // get the Project Task Names
 $ProjectTask = mysql_query("select Name,ProjectTaskID FROM ProjectTask WHERE ProjectID=". $ProjectID);
 while ($ProjectTaskInfo = mysql_fetch_array($ProjectTask,MYSQL_ASSOC)) {
  $ProjectTaskName = $ProjectTaskInfo{'Name'};
  $ProjectTaskNameCheck = eregi_replace(" ","" ,$ProjectTaskName); 
  $ProjectTaskID = $ProjectTaskInfo{'ProjectTaskID'};
  if ($SectionName == $ProjectTaskNameCheck) {
   echo "<span style='cursor:default;' id='label". $ProjectTaskNameCheck . $ProjectTaskID ."' onclick=\"hideshow('". $ProjectTaskNameCheck . $ProjectTaskID ."')\">&#151;</span> <a href='http://www.nees.lehigh.edu/".$source_location."/ProjectTask/viewProjectTask.php?ProjectTaskID=". $ProjectTaskID . "'><b>". $ProjectTaskName . "</b></a><p />";
   echo "<div style='display:block;margin-left:10px;' id='". $ProjectTaskNameCheck . $ProjectTaskID ."'>";
  }
  else {      
   if ($Section > 2) {
   	echo "<span style='cursor:default;' id='label". $ProjectTaskNameCheck . $ProjectTaskID ."' onclick=\"hideshow('". $ProjectTaskNameCheck . $ProjectTaskID ."')\">&#151;</span> <a href='http://www.nees.lehigh.edu/".$source_location."/ProjectTask/viewProjectTask.php?ProjectTaskID=". $ProjectTaskID . "'>". $ProjectTaskName . "</a><p />";
    echo "<div style='display:block;margin-left:10px;' id='". $ProjectTaskNameCheck . $ProjectTaskID ."'>";
   }
   else {
   	echo "<span style='cursor:default;' id='label". $ProjectTaskNameCheck . $ProjectTaskID ."' onclick=\"hideshow('". $ProjectTaskNameCheck . $ProjectTaskID ."')\">+</span> <a href='http://www.nees.lehigh.edu/".$source_location."/ProjectTask/viewProjectTask.php?ProjectTaskID=". $ProjectTaskID . "'>". $ProjectTaskName . "</a><p />";       
    echo "<div style='display:none;margin-left:10px;' id='". $ProjectTaskNameCheck . $ProjectTaskID ."'>";
   }
  }
  
  // get the Test Condition Names
  $TestCondition = mysql_query("select Name,TestConditionID FROM TestCondition WHERE ProjectTaskID=". $ProjectTaskID);
  while ($TestConditionInfo = mysql_fetch_array($TestCondition,MYSQL_ASSOC)) {
   $TestConditionName = $TestConditionInfo{'Name'};
   $TestConditionNameCheck = eregi_replace(" ","" ,$TestConditionName); 
   $TestConditionID = $TestConditionInfo{'TestConditionID'};
   if ($SectionName == $TestConditionNameCheck) {
    echo "<span style='cursor:default;' id='label". $TestConditionNameCheck . $TestConditionID ."' onclick=\"hideshow('". $TestConditionNameCheck . $TestConditionID ."')\">&#151;</span> <a href='http://www.nees.lehigh.edu/".$source_location."/TestCondition/viewTestCondition.php?TestConditionID=". $TestConditionID . "'><b>". $TestConditionName . "</b></a><p />";
    echo "<div style='display:block;margin-left:10px;' id='". $TestConditionNameCheck . $TestConditionID ."'>";
   }
   else {  
    if ($Section > 3) {   
     echo "<span style='cursor:default;' id='label". $TestConditionNameCheck . $TestConditionID ."' onclick=\"hideshow('". $TestConditionNameCheck . $TestConditionID ."')\">&#151;</span> <a href='http://www.nees.lehigh.edu/".$source_location."/TestCondition/viewTestCondition.php?TestConditionID=". $TestConditionID . "'>". $TestConditionName . "</a><p />";	     	
     echo "<div style='display:block;margin-left:10px;' id='". $TestConditionNameCheck . $TestConditionID ."'>";
    }   
    else { 
     echo "<span style='cursor:default;' id='label". $TestConditionNameCheck . $TestConditionID ."' onclick=\"hideshow('". $TestConditionNameCheck . $TestConditionID ."')\">+</span> <a href='http://www.nees.lehigh.edu/".$source_location."/TestCondition/viewTestCondition.php?TestConditionID=". $TestConditionID . "'>". $TestConditionName . "</a><p />";
     echo "<div style='display:none;margin-left:10px;' id='". $TestConditionNameCheck . $TestConditionID ."'>";
    }
   }
   
   // get the Test Names
   $Test = mysql_query("select Name,TestID FROM Test WHERE TestConditionID=". $TestConditionID);
   while ($TestInfo = mysql_fetch_array($Test,MYSQL_ASSOC)) {
    $TestName = $TestInfo{'Name'};
    $TestNameCheck = eregi_replace(" ","" ,$TestName); 
    $TestID = $TestInfo{'TestID'};
    if ($SectionName == $TestNameCheck) {
     echo "<span style='cursor:default;' id='label". $TestNameCheck . $TestID ."' onclick=\"hideshow('". $TestNameCheck . $TestID ."')\">&#151;</span> <a href='http://www.nees.lehigh.edu/".$source_location."/Test/viewTest.php?TestID=". $TestID . "'><b>". $TestName . "</b></a><p />";
     echo "<div style='display:block;margin-left:18px;' id='". $TestNameCheck . $TestID ."'>";
    }
    else {
     if ($Section > 4) {
      echo "<span style='cursor:default;' id='label". $TestNameCheck . $TestID ."' onclick=\"hideshow('". $TestNameCheck . $TestID ."')\">&#151;</span> <a href='http://www.nees.lehigh.edu/".$source_location."/Test/viewTest.php?TestID=". $TestID . "'>". $TestName . "</a><p />";
      echo "<div style='display:block;margin-left:18px;' id='". $TestNameCheck . $TestID ."'>";
     }    
	 else {	 
	  echo "<span style='cursor:default;' id='label". $TestNameCheck . $TestID ."' onclick=\"hideshow('". $TestNameCheck . $TestID ."')\">+</span> <a href='http://www.nees.lehigh.edu/".$source_location."/Test/viewTest.php?TestID=". $TestID . "'>". $TestName . "</a><p />";
      echo "<div style='display:none;margin-left:18px;' id='". $TestNameCheck . $TestID ."'>";
	 }
    }	
	
    // get the DataSet information
    $DataSet = mysql_query("select Name,DataSetID FROM DataSet WHERE TestID=". $TestID);
    while ($DataSetInfo = mysql_fetch_array($DataSet,MYSQL_ASSOC)) {
     $DataSetName = $DataSetInfo{'Name'}; 
     $DataSetNameCheck = eregi_replace(" ","" ,$DataSetName);    
     $DataSetID = $DataSetInfo{'DataSetID'};
     if ($SectionName == $DataSetNameCheck) {
      echo "<a href='http://www.nees.lehigh.edu/".$source_location."/DataSet/viewDataSet.php?DataSetID=". $DataSetID . "'><b>". $DataSetName . "</b></a><br /><br />";
     }
     else {
      echo "<a href='http://www.nees.lehigh.edu/".$source_location."/DataSet/viewDataSet.php?DataSetID=". $DataSetID . "'>". $DataSetName . "</a><br /><br />";
     }     
    }
    echo "</div>"; 
   }
   echo "</div>";    
  }   
  echo "</div>"; 
 }
 echo "</div>";
}
*/ 


  
?>

