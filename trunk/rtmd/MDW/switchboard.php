<?php
// Required externals
require ("config/config.php");
require ("mysql/connect.php");
require ("php/checklogin.php");
require ("php/datetime.php");
require ("php/functions.php");
/*
 * Lehigh Data Model Switchboard  
 * See README.txt for instructions on using and editting this file
 */
 
/***** SECTION 1: Class retrieval *****/
// Retrieve the called Class 
$UniqueName = $_GET['UniqueName'];

/***** SECTION 2: Class details *****/
// Table of each Object and its properties
// Section 2A should always be in any model created
// Section 2B,C can be modified or recreated

/***** SECTION 2A: Project details *****/
/***** The $Items second may change*****/
/***** but the first 5 lines can't *****/
if ($UniqueName == "Project") {
	$DisplayName = "Project";
	$TableName = "BasicClass";
	$SharedName = "Projects";	
	$Items = array (	"text:Name:Name:s",
						"textarea:Description:Text1:s",
						"object:Descriptive Files:Files:ProjectDescriptiveFile:m",
						"object:Publications:Files:ProjectPublication:m",
						"object:Presentations:Files:ProjectPresentation:m",
						"break:here",													
						"object:Typical Experimental Tasks:BasicClass:TypicalExperimentalTask:m",
						"object:Hybrid Experimental Tasks:BasicClass:HybridExperimentalTask:m",
						"object:Analysis Tasks:BasicClass:AnalysisTask:m"); 	
 	$AllowSharing = "No";		
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}		

/***** SECTION 2B: Standard object details *****/
else if ($UniqueName == "ProjectDescriptiveFile") {
	$DisplayName = "Descriptive File";
	$ParentUniqueName = "Project";	
	$ParentDisplayName = "Project";
	$Items = array ("	textarea:Description:Text1:s");	
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "ProjectPublication") {
	$DisplayName = "Publication";
	$ParentUniqueName = "Project";	
	$ParentDisplayName = "Project";
	$Items = array ("	textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "ProjectPresentation") {
	$DisplayName = "Presentation";
	$ParentUniqueName = "Project";	
	$ParentDisplayName = "Project";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "AnalysisTask") {    	
	$DisplayName = "Analysis Task";
	$TableName = "BasicClass";
	$ParentUniqueName = "Project";
	$ParentDisplayName = "Project";	
	$Items = array (	"text:Name:Name:s",
						"textarea:Description:Text1:s",
						"object:Descriptive Files:Files:AnalysisTaskDescriptiveFile:m",
						"object:Publications:Files:AnalysisTaskPublication:m",
						"object:Presentations:Files:AnalysisTaskPresentation:m",
						"break:here",
						"object:Analysis Conditions:BasicClass:AnalysisCondition:m");	
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}		
else if ($UniqueName == "AnalysisTaskDescriptiveFile") {
	$DisplayName = "Descriptive File";
	$ParentUniqueName = "AnalysisTask";
	$ParentDisplayName = "Analysis Task";	
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "AnalysisTaskPublication") {
	$DisplayName = "Publication";
	$ParentUniqueName = "AnalysisTask";
	$ParentDisplayName = "Analysis Task";	
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "AnalysisTaskPresentation") {
	$DisplayName = "Presentation";
	$ParentUniqueName = "AnalysisTask";
	$ParentDisplayName = "Analysis Task";	
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "TypicalExperimentalTask") {    	
	$DisplayName = "Typical Experimental Task";
	$TableName = "BasicClass";
	$ParentUniqueName = "Project";
	$ParentDisplayName = "Project";
	$Items = array (	"text:Name:Name:s",
						"textarea:Description:Text1:s",
						"object:Descriptive Files:Files:TypicalExperimentalTaskDescriptiveFile:m",
						"object:Publications:Files:TypicalExperimentalTaskPublication:m",
						"object:Presentations:Files:TypicalExperimentalTaskPresentation:m",
						"break:here",													
						"object:Test Condition:BasicClass:TestCondition:m",
						"object:Analyses:BasicClass:TypicalExperimentalTaskAnalysis:m");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}	
else if ($UniqueName == "TypicalExperimentalTaskDescriptiveFile") {
	$DisplayName = "Descriptive File";
	$ParentUniqueName = "TypicalExperimentalTask";
	$ParentDisplayName = "Typical Experimental Task";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "TypicalExperimentalTaskPublication") {
	$DisplayName = "Publication";
	$ParentUniqueName = "TypicalExperimentalTask";
	$ParentDisplayName = "Typical Experimental Task";	
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "TypicalExperimentalTaskPresentation") {
	$DisplayName = "Presentation";
	$ParentUniqueName = "TypicalExperimentalTask";
	$ParentDisplayName = "Typical Experimental Task";
	$Items = array (	"textarea:Description:Text1:s");	
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "HybridExperimentalTask") {    	
	$DisplayName = "Hybrid Experimental Task";
	$TableName = "BasicClass";
	$ParentUniqueName = "Project";
	$ParentDisplayName = "Project";
	$Items = array (	"text:Name:Name:s",
						"textarea:Description:Text1:s",						
						"object:Descriptive Files:Files:HybridExperimentalTaskDescriptiveFile:m",
						"object:Publications:Files:HybridExperimentalTaskPublication:m",
						"object:Presentations:Files:HybridExperimentalTaskPresentation:m",
						"break:here",
						"object:Simulation Coordinator:BasicClass:SimulationCoordinator:s",						
						"object:Analytical Substructures:BasicClass:AnalyticalSubstructure:m",
						"object:Physical Substructures:BasicClass:PhysicalSubstructure:m",
						"object:Analyses:BasicClass:HybridExperimentalTaskAnalysis:m");	
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "HybridExperimentalTaskDescriptiveFile") {
	$DisplayName = "Descriptive File";
	$ParentUniqueName = "HybridExperimentalTask";
	$ParentDisplayName = "Hybrid Experimental Task";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "HybridExperimentalTaskPublication") {
	$DisplayName = "Publication";
	$ParentUniqueName = "HybridExperimentalTask";
	$ParentDisplayName = "Hybrid Experimental Task";
	$Items = array (	"textarea:Description:Text1:s");	
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "HybridExperimentalTaskPresentation") {
	$DisplayName = "Presentation";
	$ParentUniqueName = "HybridExperimentalTask";
	$ParentDisplayName = "Hybrid Experimental Task";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
/// here
else if ($UniqueName == "SimulationCoordinator") {    	
	$DisplayName = "Simulation Coordinator";
	$TableName = "BasicClass";
	$ParentUniqueName = "HybridExperimentalTask";
	$ParentDisplayName = "Hybrid Experimental Task";
	$Items = array (	"text:Name:Name:s",
						"textarea:Description:Text1:s",
						"text:Software:Text2:s",
						"object:Descriptive Files:Files:SimulationCoordinatorDescriptiveFile:m",								
						"break:here",
						"object:Model:BasicClass:SimulationCoordinatorModel:s",
						"sharedobject:Persons:BasicClass:Person:m",
						"sharedobject:Facility:BasicClass:Facility:s",						
						"break:here",									
						"object:Simulations:BasicClass:SimulationCoordinatorSimulation:m");									
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "SimulationCoordinatorDescriptiveFile") {
	$DisplayName = "Descriptive File";
	$ParentUniqueName = "SimulationCoordinator";
	$ParentDisplayName = "Simulation Coordinator";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "SimulationCoordinatorModel") {   		
	$DisplayName = "Model";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "SimulationCoordinator";
	$ParentDisplayName = "Simulation Coordinator";
	$Items = array (	"text:Name:Name:s",
						"textarea:Description:Text1:s",
						"object:Model Files:Files:SimulationCoordinatorModelModelFile:m",								
						"object:Visual Files:Files:SimulationCoordinatorModelVisualFile:m");	
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "SimulationCoordinatorModelModelFile") {
	$DisplayName = "Model File";
	$ParentUniqueName = "SimulationCoordinatorModel";
	$ParentDisplayName = "Simulation Coordinator Model";	
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "SimulationCoordinatorModelVisualFile") {
	$DisplayName = "Visual File";
	$ParentUniqueName = "SimulationCoordinatorModel";
	$ParentDisplayName = "Simulation Coordinator Model";
	$Items = array (	"textarea:Description:Text1:s");	
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "SimulationCoordinatorSimulation") {
	$DisplayName = "Simulation";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "SimulationCoordinator";
	$ParentDisplayName = "Simulation Coordinator";
	$Items = array (	"text:Name:Name:s",
						"textarea:Description:Text1:s",
						"datetime:Start Date and Time:DateTime1:s",
						"datetime:End Date and Time:DateTime2:s",						
						"object:Descriptive Files:BasicClass:SimulationCoordinatorSimulationDescriptiveFile:m",
						"break:here",												
						"sharedobject:Persons:BasicClass:Person:m",
						"break:here",
						"object:Interfaces to Physical Substructure:BasicClass:PhysicalSubstructureInterface:m",
						"object:Interfaces to Analytical Substructure:BasicClass:AnalyticalSubstructureInterface:m",						
						"sharedobject:Simulation Protocol:BasicClass:Protocol:s",
						"object:Data Set:BasicClass:SimulationCoordinatorSimulationDataSet:s");						
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "SimulationCoordinatorSimulationDescriptiveFile") {
	$DisplayName = "Descriptive File";
	$ParentUniqueName = "SimulationCoordinatorSimulation";
	$ParentDisplayName = "Simulation";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "SimulationCoordinatorSimulationDataSet") {   		
	$DisplayName = "Data Set";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "SimulationCoordinatorSimulation";
	$ParentDisplayName = "Simulation";
	$Items = array (	"text:Name:Name:s",
						"textarea:Description:Text1:s",
						"object:Descriptive Files:Files:SimulationCoordinatorSimulationSimulationDescriptiveFile:m",
						"object:Output Data:Files:SimulationCoordinatorSimulationOutputData:m",
						"object:Processed Data:Files:SimulationCoordinatorSimulationProcessedData:m",
						"object:Images:Files:SimulationCoordinatorSimulationImage:m",																											
						"object:Videos:Files:SimulationCoordinatorSimulationVideo:m",
						"object:Other Files:Files:SimulationCoordinatorSimulationOtherFile:m");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "SimulationCoordinatorSimulationSimulationDescriptiveFile") {
	$DisplayName = "Descriptive File";
	$ParentUniqueName = "SimulationCoordinatorSimulationDataSet";
	$ParentDisplayName = "Data Set";	
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "SimulationCoordinatorSimulationOutputData") {
	$DisplayName = "Output Data";
	$ParentUniqueName = "SimulationCoordinatorSimulationDataSet";
	$ParentDisplayName = "Data Set";	
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "SimulationCoordinatorSimulationProcessedData") {
	$DisplayName = "Processed Data";
	$ParentUniqueName = "SimulationCoordinatorSimulationDataSet";
	$ParentDisplayName = "Data Set";
	$Items = array (	"textarea:Description:Text1:s");	
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "SimulationCoordinatorSimulationImage") {
	$DisplayName = "Image";
	$ParentUniqueName = "SimulationCoordinatorSimulationDataSet";
	$ParentDisplayName = "Data Set";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "SimulationCoordinatorSimulationVideo") {
	$DisplayName = "Video";
	$ParentUniqueName = "SimulationCoordinatorSimulationDataSet";
	$ParentDisplayName = "Data Set";	
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "SimulationCoordinatorSimulationOtherFile") {
	$DisplayName = "Other File";
	$ParentUniqueName = "SimulationCoordinatorSimulationDataSet";
	$ParentDisplayName = "Data Set";	
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "PhysicalSubstructureInterface") {
	$DisplayName = "Physical Substructure Interface";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "SimulationCoordinatorSimulation";
	$ParentDisplayName = "Simulation";
	$Items = array (	"text:Name:Name:s",
						"object:Commands:Files:PhysicalSubstructureInterfaceCommands:s",
						"object:Feedbacks:Files:PhysicalSubstructureInterfaceFeedbacks:s",							
						"sharedobject:Physical Substructure Test:BasicClass:PhysicalSubstructureTest:s");	
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "PhysicalSubstructureInterfaceCommands") {
	$DisplayName = "Commands";
	$ParentUniqueName = "PhysicalSubstructureInterface";
	$ParentDisplayName = "Physical Substructure Interface";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "PhysicalSubstructureInterfaceFeedbacks") {
	$DisplayName = "Feedbacks";
	$ParentUniqueName = "PhysicalSubstructureInterface";
	$ParentDisplayName = "Physical Substructure Interface";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "AnalyticalSubstructureInterface") {
	$DisplayName = "Analytical Substructure Interface";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "SimulationCoordinatorSimulation";
	$ParentDisplayName = "Simulation";
	$Items = array (	"text:Name:Name:s",
						"object:Commands:Files:AnalyticalSubstructureInterfaceCommands:s",
						"object:Feedbacks:Files:AnalyticalSubstructureInterfaceFeedbacks:s",							
						"sharedobject:Computation:BasicClass:AnalyticalSubstructureComputation:s");	
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "AnalyticalSubstructureInterfaceCommands") {
	$DisplayName = "Commands";
	$ParentUniqueName = "AnalyticalSubstructureInterface";
	$ParentDisplayName = "Analytical Substructure Interface";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "AnalyticalSubstructureInterfaceFeedbacks") {
	$DisplayName = "Feedbacks";
	$ParentUniqueName = "AnalyticalSubstructureInterface";
	$ParentDisplayName = "Analytical Substructure Interface";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "AnalysisCondition") {   		
	$DisplayName = "Analysis Condition";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "AnalysisTask";
	$ParentDisplayName = "Analysis Task";	
	$Items = array (	"text:Name:Name:s",
						"textarea:Description:Text1:s",
						"object:Descriptive Files:Files:AnalysisConditionDescriptiveFile:m",						
						"break:here",
						"object:Model:BasicClass:AnalysisConditionModel:s",						
						"sharedobject:Persons:BasicClass:Person:m",						
						"object:Software:BasicClass:AnalysisConditionSoftware:m",
						"object:Hardware:BasicClass:AnalysisConditionHardware:m",
						"break:here",
						"object:Analysis Cases:BasicClass:AnalysisConditionAnalysisCase:m");									
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "AnalysisConditionDescriptiveFile") {
	$DisplayName = "Descriptive File";
	$ParentUniqueName = "AnalysisCondition";
	$ParentDisplayName = "Analysis Condition";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "AnalysisConditionModel") {   		
	$DisplayName = "Model";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "AnalysisCondition";
	$ParentDisplayName = "Analysis Condition";
	$Items = array (	"text:Name:Name:s",
						"textarea:Description:Text1:s",
						"object:Model Files:Files:AnalysisConditionModelModelFile:m",								
						"object:Visual Files:Files:AnalysisConditionModelVisualFile:m");	
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "AnalysisConditionModelModelFile") {
	$DisplayName = "Model File";
	$ParentUniqueName = "AnalysisConditionModel";
	$ParentDisplayName = "Analysis Condition Model";	
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "AnalysisConditionModelVisualFile") {
	$DisplayName = "Visual File";
	$ParentUniqueName = "AnalysisConditionModel";
	$ParentDisplayName = "Analysis Condition Model";
	$Items = array (	"textarea:Description:Text1:s");	
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}	
else if ($UniqueName == "AnalysisConditionHardware") {   		
	$DisplayName = "Hardware";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "AnalysisCondition";
	$ParentDisplayName = "Analysis Condition";
	$Items = array (	"text:Name:Name:s",
						"textarea:Description:Text1:s",
						"sharedobject:Site:BasicClass:Site:s",								
						"object:Specification:Files:AnalysisConditionHardwareSpecification:s");	
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "AnalysisConditionHardwareSpecification") {
	$DisplayName = "Specification";
	$ParentUniqueName = "AnalysisConditionHardware";
	$ParentDisplayName = "Hardware";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "AnalysisConditionSoftware") {   		
	$DisplayName = "Software";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "AnalysisCondition";
	$ParentDisplayName = "Analysis Condition";
	$Items = array (	"text:Name:Name:s",
						"textarea:Description:Text1:s",
						"text:Version:Text2:s",															
						"object:Referred Document:Files:AnalysisConditionSoftwareReferredDocument:s");	
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "AnalysisConditionSoftwareReferredDocument") {
	$DisplayName = "Referred Document";
	$ParentUniqueName = "AnalysisConditionSoftware";
	$ParentDisplayName = "Software";	
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "AnalysisConditionAnalysisCase") {   		
	$DisplayName = "Analysis Case";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "AnalysisCondition";
	$ParentDisplayName = "Analysis Condition";
	$Items = array (	"text:Name:Name:s",
						"textarea:Description:Text1:s",
						"datetime:Start Date and Time:DateTime1:s",
						"datetime:End Date and Time:DateTime2:s",
						"object:Descriptive Files:Files:AnalysisConditionAnalysisCaseDescriptiveFile:m",											
						"break:here",
						"sharedobject:Case Protocol:BasicClass:Protocol:s",
						"break:here",
						"object:Data Set:BasicClass:AnalysisConditionAnalysisCaseDataSet:s");						
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "AnalysisConditionAnalysisCaseDescriptiveFile") {
	$DisplayName = "Descriptive File";
	$ParentUniqueName = "AnalysisConditionAnalysisCase";
	$ParentDisplayName = "Analysis Case";	
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "AnalysisConditionAnalysisCaseDataSet") {   		
	$DisplayName = "Data Set";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "AnalysisConditionAnalysisCase";
	$ParentDisplayName = "Analysis Case";
	$Items = array (	"text:Name:Name:s",
						"textarea:Description:Text1:s",
						"object:Descriptive Files:Files:AnalysisConditionAnalysisCaseDescriptiveFile:m",
						"object:Output Data:Files:AnalysisConditionAnalysisCaseOutputData:m",
						"object:Processed Data:Files:AnalysisConditionAnalysisCaseProcessedData:m",
						"object:Images:Files:AnalysisConditionAnalysisCaseImage:m",															
						"object:Videos:Files:AnalysisConditionAnalysisCaseVideo:m",							
						"object:Other Files:Files:AnalysisConditionAnalysisCaseOtherFile:m");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "AnalysisConditionAnalysisCaseDescriptiveFile") {
	$DisplayName = "Descriptive File";
	$ParentUniqueName = "AnalysisConditionAnalysisCaseDataSet";
	$ParentDisplayName = "Data Set";	
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "AnalysisConditionAnalysisCaseOutputData") {
	$DisplayName = "Output Data";
	$ParentUniqueName = "AnalysisConditionAnalysisCaseDataSet";
	$ParentDisplayName = "Data Set";	
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "AnalysisConditionAnalysisCaseProcessedData") {
	$DisplayName = "Processed Data";
	$ParentUniqueName = "AnalysisConditionAnalysisCaseDataSet";
	$ParentDisplayName = "Data Set";	
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "AnalysisConditionAnalysisCaseImage") {
	$DisplayName = "Image";
	$ParentUniqueName = "AnalysisConditionAnalysisCaseDataSet";
	$ParentDisplayName = "Data Set";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "AnalysisConditionAnalysisCaseVideo") {
	$DisplayName = "Video";
	$ParentUniqueName = "AnalysisConditionAnalysisCaseDataSet";
	$ParentDisplayName = "Data Set";	
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "AnalysisConditionAnalysisCaseOtherFile") {
	$DisplayName = "Other File";
	$ParentUniqueName = "AnalysisConditionAnalysisCaseDataSet";
	$ParentDisplayName = "Data Set";	
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "TypicalExperimentalTaskAnalysis") {   		
	$DisplayName = "Analysis";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "TypicalExperimentalTask";
	$ParentDisplayName = "Typical Experimental Task";
	$Items = array (	"text:Name:Name:s",
						"textarea:Description:Text1:s",
						"object:Descriptive Files:BasicClass:TypicalExperimentalTaskAnalysisDescriptiveFile:m",						
						"break:here",						
						"object:Model:BasicClass:TypicalExperimentalTaskAnalysisModel:s",						
						"sharedobject:Persons:BasicClass:Person:m",						
						"object:Software:BasicClass:TypicalExperimentalTaskAnalysisSoftware:m",
						"object:Hardware:BasicClass:TypicalExperimentalTaskAnalysisHardware:m",
						"break:here",
						"object:Analysis Cases:BasicClass:TypicalExperimentalTaskAnalysisAnalysisCase:m");									
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "TypicalExperimentalTaskAnalysisDescriptiveFile") {
	$DisplayName = "Descriptive File";
	$ParentUniqueName = "TypicalExperimentalTaskAnalysis";
	$ParentDisplayName = "Analysis";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "TypicalExperimentalTaskAnalysisModel") {   		
	$DisplayName = "Model";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "TypicalExperimentalTaskAnalysis";
	$ParentDisplayName = "Analysis";
	$Items = array (	"text:Name:Name:s",
						"textarea:Description:Text1:s",
						"object:Model Files:Files:TypicalExperimentalTaskAnalysisModelModelFile:m",								
						"object:Model Images:Files:TypicalExperimentalTaskAnalysisModelModelImage:m");	
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "TypicalExperimentalTaskAnalysisModelModelFile") {
	$DisplayName = "Model File";
	$ParentUniqueName = "TypicalExperimentalTaskAnalysisModel";
	$ParentDisplayName = "Analysis Model";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "TypicalExperimentalTaskAnalysisModelModelImage") {
	$DisplayName = "Model Image";
	$ParentUniqueName = "TypicalExperimentalTaskAnalysisModel";
	$ParentDisplayName = "Analysis Model";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "TypicalExperimentalTaskAnalysisHardware") {   		
	$DisplayName = "Hardware";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "TypicalExperimentalTaskAnalysis";
	$ParentDisplayName = "Analysis";
	$Items = array (	"text:Name:Name:s",
						"textarea:Description:Text1:s",
						"sharedobject:Site:BasicClass:Site:s",								
						"object:Specification:Files:TypicalExperimentalTaskAnalysisHardwareSpecification:s");	
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "TypicalExperimentalTaskAnalysisHardwareSpecification") {
	$DisplayName = "Specification";
	$ParentUniqueName = "TypicalExperimentalTaskAnalysisHardware";
	$ParentDisplayName = "Hardware";	
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "TypicalExperimentalTaskAnalysisSoftware") {   		
	$DisplayName = "Software";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "TypicalExperimentalTaskAnalysis";
	$ParentDisplayName = "Analysis";
	$Items = array (	"text:Name:Name:s",
						"textarea:Description:Text1:s",
						"text:Version:Text2:s",															
						"object:Referred Document:Files:TypicalExperimentalTaskAnalysisSoftwareReferredDocument:s");	
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "TypicalExperimentalTaskAnalysisSoftwareReferredDocument") {
	$DisplayName = "Referred Document";
	$ParentUniqueName = "TypicalExperimentalTaskAnalysisSoftware";
	$ParentDisplayName = "Software";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "TypicalExperimentalTaskAnalysisAnalysisCase") {   		
	$DisplayName = "Analysis Case";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "TypicalExperimentalTaskAnalysis";
	$ParentDisplayName = "Analysis";
	$Items = array (	"text:Name:Name:s",
						"textarea:Description:Text1:s",
						"datetime:Start Date and Time:DateTime1:s",
						"datetime:End Date and Time:DateTime2:s",
						"object:Descriptive Files:Files:TypicalExperimentalTaskAnalysisAnalysisCaseDescriptiveFile:m",						
						"break:here",
						"sharedobject:Case Protocol:BasicClass:Protocol:s",
						"break:here",
						"object:Data Set:BasicClass:TypicalExperimentalTaskAnalysisAnalysisCaseDataSet:s");	
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "TypicalExperimentalTaskAnalysisAnalysisCaseDescriptiveFile") {
	$DisplayName = "Descriptive File";
	$ParentUniqueName = "TypicalExperimentalTaskAnalysisAnalysisCase";
	$ParentDisplayName = "Analysis Case";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "TypicalExperimentalTaskAnalysisAnalysisCaseDataSet") {   		
	$DisplayName = "Data Set";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "TypicalExperimentalTaskAnalysisAnalysisCase";
	$ParentDisplayName = "Analysis Case";
	$Items = array (	"text:Name:Name:s",
						"textarea:Description:Text1:s",
						"object:Descriptive Files:Files:TypicalExperimentalTaskAnalysisAnalysisCaseDescriptiveFile:m",
						"object:Output Data:Files:TypicalExperimentalTaskAnalysisAnalysisCaseOutputData:m",
						"object:Processed Data:Files:TypicalExperimentalTaskAnalysisAnalysisCaseProcessedData:m",
						"object:Images:Files:TypicalExperimentalTaskAnalysisAnalysisCaseImage:m",															
						"object:Videos:Files:TypicalExperimentalTaskAnalysisAnalysisCaseVideo:m",							
						"object:Other Files:Files:TypicalExperimentalTaskAnalysisAnalysisCaseOtherFile:m");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "TypicalExperimentalTaskAnalysisAnalysisCaseDescriptiveFile") {
	$DisplayName = "Descriptive File";
	$ParentUniqueName = "TypicalExperimentalTaskAnalysisAnalysisCaseDataSet";
	$ParentDisplayName = "Data Set";	
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "TypicalExperimentalTaskAnalysisAnalysisCaseOutputData") {
	$DisplayName = "Output Data";
	$ParentUniqueName = "TypicalExperimentalTaskAnalysisAnalysisCaseDataSet";
	$ParentDisplayName = "Data Set";	
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "TypicalExperimentalTaskAnalysisAnalysisCaseProcessedData") {
	$DisplayName = "Processed Data";
	$ParentUniqueName = "TypicalExperimentalTaskAnalysisAnalysisCaseDataSet";
	$ParentDisplayName = "Data Set";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "TypicalExperimentalTaskAnalysisAnalysisCaseImage") {
	$DisplayName = "Image";
	$ParentUniqueName = "TypicalExperimentalTaskAnalysisAnalysisCaseDataSet";
	$ParentDisplayName = "Data Set";	
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "TypicalExperimentalTaskAnalysisAnalysisCaseVideo") {
	$DisplayName = "Video";
	$ParentUniqueName = "TypicalExperimentalTaskAnalysisAnalysisCaseDataSet";
	$ParentDisplayName = "Data Set";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "TypicalExperimentalTaskAnalysisAnalysisCaseOtherFile") {
	$DisplayName = "Other File";
	$ParentUniqueName = "TypicalExperimentalTaskAnalysisAnalysisCaseDataSet";
	$ParentDisplayName = "Data Set";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "HybridExperimentalTaskAnalysis") {   		
	$DisplayName = "Analysis";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "HybridExperimentalTask";
	$ParentDisplayName = "Hybrid Experimental Task";	
	$Items = array (	"text:Name:Name:s",
						"textarea:Description:Text1:s",
						"object:Descriptive Files:Files:HybridExperimentalTaskAnalysisDescriptiveFile:m",
						"break:here",
						"object:Model:BasicClass:HybridExperimentalTaskAnalysisModel:s",
						"sharedobject:Persons:BasicClass:Person:m",						
						"object:Software:BasicClass:HybridExperimentalTaskAnalysisSoftware:m",
						"object:Hardware:BasicClass:HybridExperimentalTaskAnalysisHardware:m",
						"break:here",
						"object:Analysis Cases:BasicClass:HybridExperimentalTaskAnalysisAnalysisCase:m");									
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "HybridExperimentalTaskAnalysisDescriptiveFile") {
	$DisplayName = "Descriptive File";
	$ParentUniqueName = "HybridExperimentalTaskAnalysis";
	$ParentDisplayName = "Analysis";
	$Items = array (	"textarea:Description:Text1:s");	
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "HybridExperimentalTaskAnalysisModel") {   		
	$DisplayName = "Model";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "HybridExperimentalTaskAnalysis";
	$ParentDisplayName = "Analysis";	
	$Items = array (	"text:Name:Name:s",
						"textarea:Description:Text1:s",
						"object:Model Files:Files:HybridExperimentalTaskAnalysisModelModelFile:m",								
						"object:Model Images:Files:HybridExperimentalTaskAnalysisModelModelImage:m");	
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "HybridExperimentalTaskAnalysisModelModelFile") {
	$DisplayName = "Model File";
	$ParentUniqueName = "HybridExperimentalTaskAnalysisModel";
	$ParentDisplayName = "Analysis Model";	
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "HybridExperimentalTaskAnalysisModelModelImage") {
	$DisplayName = "Model Image";
	$ParentUniqueName = "HybridExperimentalTaskAnalysisModel";
	$ParentDisplayName = "Analysis Model";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}		
else if ($UniqueName == "HybridExperimentalTaskAnalysisHardware") {   		
	$DisplayName = "Hardware";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "HybridExperimentalTaskAnalysis";
	$ParentDisplayName = "Analysis";
	$Items = array (	"text:Name:Name:s",
						"textarea:Description:Text1:s",
						"sharedobject:Site:BasicClass:Site:s",								
						"object:Specification:Files:HybridExperimentalTaskAnalysisHardwareSpecification:s");	
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "HybridExperimentalTaskAnalysisHardwareSpecification") {
	$DisplayName = "Specification";
	$ParentUniqueName = "HybridExperimentalTaskAnalysisHardware";
	$ParentDisplayName = "Hardware";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "HybridExperimentalTaskAnalysisSoftware") {   		
	$DisplayName = "Software";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "HybridExperimentalTaskAnalysis";
	$ParentDisplayName = "Analysis";
	$Items = array (	"text:Name:Name:s",
						"textarea:Description:Text1:s",
						"text:Version:Text2:s",															
						"object:Referred Document:Files:HybridExperimentalTaskAnalysisSoftwareReferredDocument:s");	
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "HybridExperimentalTaskAnalysisSoftwareReferredDocument") {
	$DisplayName = "Referred Document";
	$ParentUniqueName = "HybridExperimentalTaskAnalysisSoftware";
	$ParentDisplayName = "Software";	
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "HybridExperimentalTaskAnalysisAnalysisCase") {   		
	$DisplayName = "Analysis Case";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "HybridExperimentalTaskAnalysis";
	$ParentDisplayName = "Analysis";
	$Items = array (	"text:Name:Name:s",
						"textarea:Description:Text1:s",
						"datetime:Start Date and Time:DateTime1:s",
						"datetime:End Date and Time:DateTime2:s",
						"object:Descriptive Files:Files:HybridExperimentalTaskAnalysisAnalysisCaseDescriptiveFile:m",
						"sharedobject:Related Test:BasicClass:PhysicalSubstructureTest:s",
						"break:here",
						"sharedobject:Case Protocol:BasicClass:Protocol:s",
						"break:here",
						"object:Data Set:BasicClass:HybridExperimentalTaskAnalysisAnalysisCaseDataSet:s");							
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "HybridExperimentalTaskAnalysisAnalysisCaseDescriptiveFile") {
	$DisplayName = "Descriptive File";
	$ParentUniqueName = "HybridExperimentalTaskAnalysisAnalysisCase";
	$ParentDisplayName = "Analysis Case";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "HybridExperimentalTaskAnalysisAnalysisCaseDataSet") {   		
	$DisplayName = "Data Set";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "HybridExperimentalTaskAnalysisAnalysisCase";
	$ParentDisplayName = "Analysis Case";
	$Items = array (	"text:Name:Name:s",
						"textarea:Description:Text1:s",
						"object:Descriptive Files:Files:HybridExperimentalTaskAnalysisAnalysisCaseDescriptiveFile:m",
						"object:Output Data:Files:HybridExperimentalTaskAnalysisAnalysisCaseOutputData:m",
						"object:Processed Data:Files:HybridExperimentalTaskAnalysisAnalysisCaseProcessedData:m",
						"object:Images:Files:HybridExperimentalTaskAnalysisAnalysisCaseImage:m",															
						"object:Videos:Files:HybridExperimentalTaskAnalysisAnalysisCaseVideo:m",							
						"object:Other Files:Files:HybridExperimentalTaskAnalysisAnalysisCaseOtherFile:m");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "HybridExperimentalTaskAnalysisAnalysisCaseDescriptiveFile") {
	$DisplayName = "Descriptive File";
	$ParentUniqueName = "HybridExperimentalTaskAnalysisAnalysisCaseDataSet";
	$ParentDisplayName = "Data Set";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "HybridExperimentalTaskAnalysisAnalysisCaseOutputData") {
	$DisplayName = "Output Data";
	$ParentUniqueName = "HybridExperimentalTaskAnalysisAnalysisCaseDataSet";
	$ParentDisplayName = "Data Set";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "HybridExperimentalTaskAnalysisAnalysisCaseProcessedData") {
	$DisplayName = "Processed Data";
	$ParentUniqueName = "HybridExperimentalTaskAnalysisAnalysisCaseDataSet";
	$ParentDisplayName = "Data Set";
	$Items = array (	"textarea:Description:Text1:s");	
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");
}
else if ($UniqueName == "HybridExperimentalTaskAnalysisAnalysisCaseImage") {
	$DisplayName = "Image";
	$ParentUniqueName = "HybridExperimentalTaskAnalysisAnalysisCaseDataSet";
	$ParentDisplayName = "Data Set";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "HybridExperimentalTaskAnalysisAnalysisCaseVideo") {
	$DisplayName = "Video";
	$ParentUniqueName = "HybridExperimentalTaskAnalysisAnalysisCaseDataSet";
	$ParentDisplayName = "Data Set";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "HybridExperimentalTaskAnalysisAnalysisCaseOtherFile") {
	$DisplayName = "Other File";
	$ParentUniqueName = "HybridExperimentalTaskAnalysisAnalysisCaseDataSet";
	$ParentDisplayName = "Data Set";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "TestCondition") {   		
	$DisplayName = "Test Condition";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "TypicalExperimentalTask";
	$ParentDisplayName = "Typical Experimental Task";
	$Items = array (	"text:Name:Name:s",
						"textarea:Description:Text1:s",
						"object:Descriptive Files:Files:TestConditionDescriptiveFile:m",												
						"object:Setup Drawings and Photos:Files:TestConditionSetupDrawings:m",
						"break:here",
						"sharedobject:Persons:BasicClass:Person:m",		
						"sharedobject:Specimen:BasicClass:Specimen:s",						
						"sharedobject:Facility:BasicClass:Facility:s",
						"sharedobject:Actuators:BasicClass:Actuator:m",
						"sharedobject:Other Loading Fixtures:BasicClass:OtherLoadingFixture:m",
						"sharedobject:Bracing and Reaction Fixtures:BasicClass:BracingAndReactionFixture:m",
						"sharedobject:Sensors:BasicClass:Sensor:m",
						"sharedobject:Cables:BasicClass:Cable:m",
						"break:here",
						"object:Tests:BasicClass:TestConditionTest:m");								
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "TestConditionDescriptiveFile") {
	$DisplayName = "Descriptive File";
	$ParentUniqueName = "TestCondition";
	$ParentDisplayName = "Test Condition";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "TestConditionSetupDrawings") {
	$DisplayName = "Setup Drawing and/or Photo";
	$ParentUniqueName = "TestCondition";
	$ParentDisplayName = "Test Condition";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "TestConditionTest") {
	$DisplayName = "Test";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "TestCondition";
	$ParentDisplayName = "Test Condition";
	$Items = array (	"text:Name:Name:s",
						"textarea:Description:Text1:s",								
						"datetime:Start Date and Time:DateTime1:s",
						"datetime:End Date and Time:DateTime2:s",
						"textarea:Uncontrolled Test Condition:Text2:s",
						"object:Descriptive Files:Files:TestConditionTestDescriptiveFile:m",						
						"break:here",
						"sharedobject:Test Protocol:BasicClass:Protocol:s",
						"sharedobject:Persons:BasicClass:Person:m",
						"break:here",						
						"object:Data Set:BasicClass:TestConditionTestDataSet:s");				
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "TestConditionTestDescriptiveFile") {
	$DisplayName = "Descriptive File";
	$ParentUniqueName = "TestConditionTest";
	$ParentDisplayName = "Test";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "TestConditionTestDataSet") {   		
	$DisplayName = "Data Set";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "TestConditionTest";
	$ParentDisplayName = "Test";
	$Items = array (	"text:Name:Name:s",
						"textarea:Description:Text1:s",
						"object:Descriptive Files:Files:TestConditionTestDataSetDescriptiveFile:m",
						"object:Raw Binary Data:Files:TestConditionTestRawBinaryData:m",								
						"object:Raw Engineering Unit Data:Files:TestConditionTestRawEngineeringUnitData:m",
						"object:Corrected Data:Files:TestConditionTestCorrectedData:m",
						"object:Processed Data:Files:TestConditionTestProcessedData:m",
						"object:Images:Files:TestConditionTestImage:m",															
						"object:Videos:Files:TestConditionTestVideo:m",
						"object:Webcam Images:Files:TestConditionTestWebcamImage:m",
						"object:Timelapsed Video:Files:TestConditionTestTimelapsedVideo:m",
						"object:Composite Video:Files:TestConditionTestCompositeVideo:m",
						"object:Other Files:Files:TestConditionTestOtherFile:m");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "TestConditionTestDataSetDescriptiveFile") {
	$DisplayName = "Descriptive File";
	$ParentUniqueName = "TestConditionTestDataSet";
	$ParentDisplayName = "Data Set";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "TestConditionTestRawBinaryData") {
	$DisplayName = "Raw Binary Data";
	$ParentUniqueName = "TestConditionTestDataSet";
	$ParentDisplayName = "Data Set";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "TestConditionTestRawEngineeringUnitData") {
	$DisplayName = "Raw Engineering Unit Data";
	$ParentUniqueName = "TestConditionTestDataSet";
	$ParentDisplayName = "Data Set";
	$Items = array (	"textarea:Description:Text1:s");	
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "TestConditionTestCorrectedData") {
	$DisplayName = "Corrected Data";
	$ParentUniqueName = "TestConditionTestDataSet";
	$ParentDisplayName = "Data Set";	
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "TestConditionTestProcessedData") {
	$DisplayName = "Processed Data";
	$ParentUniqueName = "TestConditionTestDataSet";
	$ParentDisplayName = "Data Set";
	$Items = array (	"textarea:Description:Text1:s");	
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "TestConditionTestImage") {
	$DisplayName = "Image";
	$ParentUniqueName = "TestConditionTestDataSet";
	$ParentDisplayName = "Data Set";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "TestConditionTestWebcamImage") {
	$DisplayName = "Webcam Image";
	$ParentUniqueName = "TestConditionTestDataSet";
	$ParentDisplayName = "Data Set";
	$Items = array (	"textarea:Description:Text1:s");	
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "TestConditionTestVideo") {
	$DisplayName = "Video";
	$ParentUniqueName = "TestConditionTestDataSet";
	$ParentDisplayName = "Data Set";
	$Items = array (	"textarea:Description:Text1:s");	
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "TestConditionTestTimelapsedVideo") {
	$DisplayName = "Timelapsed Video";
	$ParentUniqueName = "TestConditionTestDataSet";
	$ParentDisplayName = "Data Set";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "TestConditionTestCompositeVideo") {
	$DisplayName = "Composite Video";
	$ParentUniqueName = "TestConditionTestDataSet";
	$ParentDisplayName = "Data Set";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "TestConditionTestOtherFile") {
	$DisplayName = "Other File";
	$ParentUniqueName = "TestConditionTestDataSet";
	$ParentDisplayName = "Data Set";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "PhysicalSubstructure") {   		
	$DisplayName = "Physical Substructure";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "HybridExperimentalTask";
	$ParentDisplayName = "Hybrid Experimental Task";
	$Items = array (	"text:Name:Name:s",
						"textarea:Description:Text1:s",
						"object:Descriptive Files:Files:PhysicalSubstructureDescriptiveFile:m",																					
						"object:Setup Drawings and Photos:Files:PhysicalSubstructureSetupDrawings:m",
						"break:here",
						"sharedobject:Persons:BasicClass:Person:m",
						"sharedobject:Specimen:BasicClass:Specimen:s",
						"sharedobject:Facility:BasicClass:Facility:s",											
						"sharedobject:Actuators:BasicClass:Actuator:m",
						"sharedobject:Other Loading Fixtures:BasicClass:OtherLoadingFixture:m",
						"sharedobject:Bracing and Reaction Fixtures:BasicClass:BracingAndReactionFixture:m",
						"sharedobject:Sensors:BasicClass:Sensor:m",
						"sharedobject:Cables:BasicClass:Cable:m",						
						"break:here",
						"sharedobject:Physical Substructure Tests:BasicClass:PhysicalSubstructureTest:m");								
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "PhysicalSubstructureDescriptiveFile") {
	$DisplayName = "Descriptive File";
	$ParentUniqueName = "PhysicalSubstructure";
	$ParentDisplayName = "Physical Substructure";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "PhysicalSubstructureSetupDrawings") {
	$DisplayName = "Setup Drawing and/or Photo";
	$ParentUniqueName = "PhysicalSubstructure";
	$ParentDisplayName = "Physical Substructure";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "AnalyticalSubstructure") {   		
	$DisplayName = "Analytical Substructure";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "HybridExperimentalTask";
	$ParentDisplayName = "Hybrid Experimental Task";
	$Items = array (	"text:Name:Name:s",
						"textarea:Description:Text1:s",
						"text:Software:Text2:s",
						"object:Descriptive Files:Files:AnalyticalSubstructureDescriptiveFile:m",																						
						"break:here",
						"object:Model:BasicClass:AnalyticalSubstructureModel:s",								
						"sharedobject:Persons:BasicClass:Person:m",
						"sharedobject:Facility:BasicClass:Facility:s",
						"break:here",														
						"sharedobject:Computations:BasicClass:AnalyticalSubstructureComputation:m");								
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "AnalyticalSubstructureDescriptiveFile") {
	$DisplayName = "Descriptive File";
	$ParentUniqueName = "AnalyticalSubstructure";
	$ParentDisplayName = "Analytical Substructure";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "AnalyticalSubstructureModel") {   		
	$DisplayName = "Model";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "AnalyticalSubstructure";
	$ParentDisplayName = "Analytical Substructure";
	$Items = array (	"text:Name:Name:s",
						"textarea:Description:Text1:s",
						"object:Model Files:Files:AnalyticalSubstructureModelModelFile:m",								
						"object:Visual Files:Files:AnalyticalSubstructureModelVisualFile:m");	
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "AnalyticalSubstructureModelModelFile") {
	$DisplayName = "Model File";
	$ParentUniqueName = "AnalyticalSubstructureModel";
	$ParentDisplayName = "Analytical Substructure Model";	
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "AnalyticalSubstructureModelVisualFile") {
	$DisplayName = "Visual File";
	$ParentUniqueName = "AnalyticalSubstructureModel";
	$ParentDisplayName = "Analytical Substructure Model";
	$Items = array (	"textarea:Description:Text1:s");	
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}

/***** SECTION 2C: Shared object details *****/
else if ($UniqueName == "Facility") {
	$DisplayName = "Facility";
	$SharedName = "Facilities";
	$TableName = "BasicClass";	
	$Items = array (	"text:Name:Name:s",
						"textarea:Description:Text1:s",
						"text:Origin of Coordinate System:Text2:s",
						"object:Specifications:Files:FacilitySpecifications:m",
						"sharedobject:Site:BasicClass:Site:s",						
						"object:Facility Drawings and Photos:Files:FacilityDrawing:m",
						"break:here",
						"object:Reference Plane for Strong Floor:BasicClass:FacilityReferencePlaneStrongFloor:s",											
						"object:Reference Planes:BasicClass:FacilityReferencePlane:m",
						"object:Reference Planes for Reaction Wall:BasicClass:FacilityReferencePlaneReactionWall:m");								
	$AllowSharing = "Yes";
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "FacilityDrawing") {
	$DisplayName = "Facility Drawing";
	$ParentUniqueName = "Facility";
	$ParentDisplayName = "Facility";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "FacilitySpecifications") {
	$DisplayName = "Specifications";
	$ParentUniqueName = "Facility";
	$ParentDisplayName = "Facility";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "FacilityReferencePlane") {
	$DisplayName = "Reference Plane";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "Facility";
	$ParentDisplayName = "Facility";
	$Items = array (	"text:Name:Name:s",								
						"text:Orientation:Text1:s",
						"text:Distance:Text2:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "FacilityReferencePlaneStrongFloor") {
	$DisplayName = "Reference Plane for Strong Floor";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "Facility";
	$ParentDisplayName = "Facility";
	$Items = array (	"text:Name:Name:s",								
						"text:Orientation:Text1:s",
						"text:Distance:Text2:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "FacilityReferencePlaneReactionWall") {
	$DisplayName = "Reference Plane for Reaction Wall";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "Facility";
	$ParentDisplayName = "Facility";
	$Items = array (	"text:Name:Name:s",								
						"text:Orientation:Text1:s",
						"text:Distance:Text2:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "Site") {
	$DisplayName = "Site";
	$SharedName = "Sites";
	$TableName = "BasicClass";	
	$Items = array (	"text:Name:Name:s",
						"textarea:Description:Text1:s");
	$AllowSharing = "Yes";
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "Person") {
	$DisplayName = "Person";
	$SharedName = "Persons";
	$TableName = "BasicClass";	
	$Items = array (	"text:Name:Name:s",
						"textarea:Description:Text1:s");
	$AllowSharing = "Yes";
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "Protocol") {
	$DisplayName = "Protocol";
	$SharedName = "Protocols";	
	$TableName = "BasicClass";	
	$Items = array (	"text:Name:Name:s",								
						"textarea:Description:Text1:s",
						"object:Descriptive Files:Files:ProtocolDescriptiveFile:m",
						"break:here",
						"object:History File:Files:ProtocolHistoryFile:s",
						"object:Simulation Method Files:Files:ProtocolSimulationMethodFile:m",
						"object:DAQ Config Files:Files:ProtocolDAQConfigFile:m",
						"object:Controller Config Files:Files:ProtocolControllerConfigFile:m",
						"object:Other Config Files:Files:ProtocolOtherConfigFile:m");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "ProtocolDescriptiveFile") {
	$DisplayName = "Descriptive File";
	$ParentUniqueName = "Protocol";
	$ParentDisplayName = "Protocol";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "ProtocolHistoryFile") {
	$DisplayName = "History File";
	$ParentUniqueName = "Protocol";
	$ParentDisplayName = "Protocol";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "ProtocolSimulationMethodFile") {
	$DisplayName = "Simulation Method File";
	$ParentUniqueName = "Protocol";
	$ParentDisplayName = "Protocol";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "ProtocolDAQConfigFile") {
	$DisplayName = "DAQ Config File";
	$ParentUniqueName = "Protocol";
	$ParentDisplayName = "Protocol";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "ProtocolControllerConfigFile") {
	$DisplayName = "Controller Config File";
	$ParentUniqueName = "Protocol";
	$ParentDisplayName = "Protocol";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "ProtocolOtherConfigFile") {
	$DisplayName = "Other Config File";
	$ParentUniqueName = "Protocol";
	$ParentDisplayName = "Protocol";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "Specimen") {
	$DisplayName = "Specimen";
	$SharedName = "Specimens";
	$TableName = "BasicClass";		
	$Items = array (	"text:Name:Name:s",
						"textarea:Description:Text1:s",
						"object:Specimen Drawings and Photos:Files:SpecimenDrawings:m",
						"break:here",
						"object:Specimen Components:BasicClass:SpecimenComponent:m");
	$AllowSharing = "Yes";
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "SpecimenDrawings") {
	$DisplayName = "Drawing and/or Photo";
	$ParentUniqueName = "Specimen";
	$ParentDisplayName = "Specimen";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "SpecimenComponent") {
	$DisplayName = "Specimen Component";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "Specimen";
	$ParentDisplayName = "Specimen";
	$Items = array (	"text:Name:Name:s",
						"textarea:Description:Text1:s",
						"object:Geometry:BasicClass:SpecimenComponentGeometry:s",
						"object:Location Using Drawings and Photos:BasicClass:SpecimenComponentLocationDrawings:s",
						"object:Location Using Grid:BasicClass:SpecimenComponentLocationGrid:s",
						"break:here",
						"sharedobject:Steel Materials:BasicClass:Steel:m",
						"sharedobject:Concrete Materials:BasicClass:Concrete:m",
						"sharedobject:Rebar Materials:BasicClass:Rebar:m",
						"sharedobject:Other Materials:BasicClass:OtherMaterial:m");						
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "SpecimenComponentLocationDrawings") {
	$DisplayName = "Location Using Drawings and Photos";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "SpecimenComponent";
	$ParentDisplayName = "Specimen Component";
	$Items = array (	"text:Name:Name:s",
						"textarea:Description:Text1:s",														
						"object:Drawings and Photos:Files:SpecimenComponentLocationDrawingsDrawings:m");								
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "SpecimenComponentLocationDrawingsDrawings") {
	$DisplayName = "Drawing and/or Photo";
	$ParentUniqueName = "SpecimenComponentLocationDrawings";
	$ParentDisplayName = "Location Using Drawings and Photos";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "SpecimenComponentLocationGrid") {
	$DisplayName = "Location Using Location Points and Lines";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "SpecimenComponent";
	$ParentDisplayName = "Specimen Component";
	$Items = array (	"text:Name:Name:s",																				
						"object:Location Points:BasicClass:SpecimenComponentLocationGridLocationPoint:m",
						"object:Location Lines:BasicClass:SpecimenComponentLocationGridLocationLine:m");								
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "SpecimenComponentLocationGridLocationPoint") {
	$DisplayName = "Location Point and its Location";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "SpecimenComponentLocationGrid";
	$ParentDisplayName = "Location Using Location Points and Lines";
	$Items = array (	"text:Name:Name:s",								
						"textarea:Location Point Description:Text1:s",
						"text:Location of Location Point:Text2:s",
						"text:Reference Point Reference Plane:Text3:s",
						"text:Reference Point Horizontal Distance:Text4:s",
						"text:Reference Point Vertical Distance:Text5:s",
						"text:Reference Point 3D X Coordinate:Text6:s",
						"text:Reference Point 3D Y Coordinate:Text7:s",
						"text:Reference Point 3D Z Coordinate:Text8:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "SpecimenComponentLocationGridLocationLine") {
	$DisplayName = "Location Line and its Location";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "SpecimenComponentLocationGrid";
	$ParentDisplayName = "Location Using Location Lines and Lines";
	$Items = array (	"text:Name:Name:s",								
						"textarea:Location Line Description:Text1:s",
						"text:Location of Location Line:Text2:s",
						"text:Reference Line Reference Plane:Text3:s",
						"text:Reference Line Orientation:Text4:s",
						"text:Reference Line Distance:Text5:s",
						"text:Reference Line 3D Orientation:Text6:s",
						"text:Reference Line 3D X Coordinate:Text7:s",
						"text:Reference Line 3D Y Coordinate:Text8:s",
						"text:Reference Line 3D Z Coordinate:Text9:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "SpecimenComponentGeometry") {
	$DisplayName = "Geometry";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "SpecimenComponent";
	$ParentDisplayName = "Specimen Component";
	$Items = array (	"text:Name:Name:s",
						"textarea:Description:Text1:s",
						"object:Drawings and Photos:Files:SpecimenComponentGeometryDrawing:m");								
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "SpecimenComponentGeometryDrawing") {
	$DisplayName = "Drawing";
	$ParentUniqueName = "SpecimenComponentGeometry";
	$ParentDisplayName = "Geometry";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "Steel") {
	$DisplayName = "Steel";
	$SharedName = "Steel";
	$TableName = "BasicClass";			
	$Items = array (	"text:Name:Name:s",
						"textarea:Description:Text1:s",
						"text:Type of Steel:Text2:s",
						"object:Modulus of Elasticity:BasicClass:SteelModulusOfElasticity:s",
						"object:Yield Stress:BasicClass:SteelYieldStress:s",
						"object:Ultimate Stress:BasicClass:SteelUltimateStress:s",								
						"object:Material Property Files:Files:SteelMaterialPropertyFile:m");								
	$AllowSharing = "Yes";
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "SteelMaterialPropertyFile") {
	$DisplayName = "Material Property File";
	$ParentUniqueName = "Steel";
	$ParentDisplayName = "Steel";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "SteelModulusOfElasticity") {
	$DisplayName = "Modulus of Elasticity";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "Steel";
	$ParentDisplayName = "Steel";
	$Items = array (	"text:Name:Name:s",
						"text:Nominal Value:Text1:s",
						"text:Average Value:Text2:s",
						"object:Test Values:BasicClass:SteelModulusOfElasticityTestValue:m");								
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "SteelModulusOfElasticityTestValue") {
	$DisplayName = "Test Value";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "SteelModulusOfElasticity";
	$ParentDisplayName = "Material Property Value";
	$Items = array (	"text:Name:Name:s",
						"text:Value:Text1:s");							
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "SteelYieldStress") {
	$DisplayName = "Yield Stress";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "Steel";
	$ParentDisplayName = "Steel";
	$Items = array (	"text:Name:Name:s",
						"text:Nominal Value:Text1:s",
						"text:Average Value:Text2:s",
						"object:Test Values:BasicClass:SteelYieldStressTestValue:m");								
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "SteelYieldStressTestValue") {
	$DisplayName = "Test Value";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "SteelYieldStress";
	$ParentDisplayName = "Yield Stress";
	$Items = array (	"text:Name:Name:s",
						"text:Value:Text1:s");									
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "SteelUltimateStress") {
	$DisplayName = "Ultimate Stress";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "Steel";
	$ParentDisplayName = "Steel";
	$Items = array (	"text:Name:Name:s",
						"text:Nominal Value:Text1:s",
						"text:Average Value:Text2:s",
						"object:Test Values:BasicClass:SteelUltimateStressTestValue:m");								
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "SteelUltimateStressTestValue") {
	$DisplayName = "Test Value";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "SteelUltimateStress";
	$ParentDisplayName = "Ultimate Stress";
	$Items = array (	"text:Name:Name:s",
						"text:Value:Text1:s");									
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "Concrete") {
	$DisplayName = "Concrete";
	$SharedName = "Concrete";
	$TableName = "BasicClass";	
	$Items = array (	"text:Name:Name:s",
						"textarea:Description:Text1:s",																
						"object:Compression Strength:BasicClass:ConcreteCompressionStrength:s",
						"object:Ultimate Compression Strain:BasicClass:ConcreteUltimateCompressionStrain:s",
						"object:Tensile Strength:BasicClass:ConcreteTensileStrength:s",
						"object:Modulus of Elasticity:BasicClass:ConcreteModulusOfElasticity:s",
						"object:Modulus of Rupture:BasicClass:ConcreteModulusOfRupture:s",
						"object:Material Property Files:Files:ConcreteMaterialPropertyFile:m");								
	$AllowSharing = "Yes";
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "ConcreteMaterialPropertyFile") {
	$DisplayName = "Material Property File";
	$ParentUniqueName = "Concrete";
	$ParentDisplayName = "Concrete";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "ConcreteModulusOfElasticity") {
	$DisplayName = "Modulus of Elasticity";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "Concrete";
	$ParentDisplayName = "Concrete";
	$Items = array (	"text:Name:Name:s",
						"text:Nominal Value:Text1:s",
						"text:Average Value:Text2:s",
						"object:Test Values:BasicClass:ConcreteModulusOfElasticityTestValue:m");								
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "ConcreteModulusOfElasticityTestValue") {
	$DisplayName = "Test Value";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "ConcreteModulusOfElasticity";
	$ParentDisplayName = "Material Property Value";
	$Items = array (	"text:Name:Name:s",
						"text:Value:Text1:s");							
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "ConcreteCompressionStrength") {
	$DisplayName = "Compression Strength";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "Concrete";
	$ParentDisplayName = "Concrete";
	$Items = array (	"text:Name:Name:s",
						"text:Nominal Value:Text1:s",
						"text:Average Value:Text2:s",
						"object:Test Values:BasicClass:ConcreteCompressionStrengthTestValue:m");								
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "ConcreteCompressionStrengthTestValue") {
	$DisplayName = "Test Value";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "ConcreteCompressionStrength";
	$ParentDisplayName = "Material Property Value";
	$Items = array (	"text:Name:Name:s",
						"text:Value:Text1:s");								
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "ConcreteUltimateCompressionStrain") {
	$DisplayName = "Ultimate Compression Strain";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "Concrete";
	$ParentDisplayName = "Concrete";
	$Items = array (	"text:Name:Name:s",
						"text:Nominal Value:Text1:s",
						"text:Average Value:Text2:s",
						"object:Test Values:BasicClass:ConcreteUltimateCompressionStrainTestValue:m");								
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "ConcreteUltimateCompressionStrainTestValue") {
	$DisplayName = "Test Value";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "ConcreteUltimateCompressionStrain";
	$ParentDisplayName = "Material Property Value";
	$Items = array (	"text:Name:Name:s",
						"text:Value:Text1:s");								
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "ConcreteTensileStrength") {
	$DisplayName = "Tensile Strength";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "Concrete";
	$ParentDisplayName = "Concrete";
	$Items = array (	"text:Name:Name:s",
						"text:Nominal Value:Text1:s",
						"text:Average Value:Text2:s",
						"object:Test Values:BasicClass:ConcreteTensileStrengthTestValue:m");								
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "ConcreteTensileStrengthTestValue") {
	$DisplayName = "Test Value";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "ConcreteTensileStrength";
	$ParentDisplayName = "Material Property Value";
	$Items = array (	"text:Name:Name:s",
						"text:Value:Text1:s");								
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "ConcreteModulusOfElasticity") {
	$DisplayName = "Modulus of Elasticity";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "Concrete";
	$ParentDisplayName = "Concrete";
	$Items = array (	"text:Name:Name:s",
						"text:Nominal Value:Text1:s",
						"text:Average Value:Text2:s",
						"object:Test Values:BasicClass:ConcreteModulusOfElasticityTestValue:m");								
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "ConcreteModulusOfElasticityTestValue") {
	$DisplayName = "Test Value";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "ConcreteModulusOfElasticity";
	$ParentDisplayName = "Material Property Value";
	$Items = array (	"text:Name:Name:s",
						"text:Value:Text1:s");								
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "ConcreteModulusOfRupture") {
	$DisplayName = "Modulus of Rupture";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "Concrete";
	$ParentDisplayName = "Concrete";
	$Items = array (	"text:Name:Name:s",
						"text:Nominal Value:Text1:s",
						"text:Average Value:Text2:s",
						"object:Test Values:BasicClass:ConcreteModulusOfRuptureTestValue:m");								
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "ConcreteModulusOfRuptureTestValue") {
	$DisplayName = "Test Value";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "ConcreteModulusOfRupture";
	$ParentDisplayName = "Material Property Value";
	$Items = array (	"text:Name:Name:s",
						"text:Value:Text1:s");							
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "Rebar") {
	$DisplayName = "Rebar";
	$SharedName = "Rebar";
	$TableName = "BasicClass";	
	$Items = array (	"text:Name:Name:s",
							"textarea:Description:Text1:s",
							"text:Type of Rebar:Text2:s",
							"object:Bar Diameter:BasicClass:RebarBarDiameter:s",
							"object:Bar Area:BasicClass:RebarBarArea:s",
							"object:Modulus of Elasticity:BasicClass:RebarModulusOfElasticity:s",
							"object:Yield Stress:BasicClass:RebarYieldStress:s",
							"object:Ultimate Stress:BasicClass:RebarUltimateStress:s",								
							"object:Material Property Files:Files:RebarMaterialPropertyFile:m");								
	$AllowSharing = "Yes";
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "RebarMaterialPropertyFile") {
	$DisplayName = "Material Property File";
	$ParentUniqueName = "Rebar";
	$ParentDisplayName = "Rebar";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "RebarBarDiameter") {
	$DisplayName = "Material Property Value";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "Rebar";
	$ParentDisplayName = "Rebar";
	$Items = array (	"text:Name:Name:s",
						"text:Nominal Value:Text1:s",
						"text:Average Value:Text2:s",
						"object:Test Values:BasicClass:RebarBarDiameterTestValue:m");								
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "RebarBarDiameterTestValue") {
	$DisplayName = "Test Value";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "RebarBarDiameter";
	$ParentDisplayName = "Material Property Value";
	$Items = array (	"text:Name:Name:s",
						"text:Value:Text1:s");								
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "RebarBarArea") {
	$DisplayName = "Material Property Value";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "Rebar";
	$ParentDisplayName = "Rebar";
	$Items = array (	"text:Name:Name:s",
						"text:Nominal Value:Text1:s",
						"text:Average Value:Text2:s",
						"object:Test Values:BasicClass:RebarBarAreaTestValue:m");								
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "RebarBarAreaTestValue") {
	$DisplayName = "Test Value";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "RebarBarArea";
	$ParentDisplayName = "Material Property Value";
	$Items = array (	"text:Name:Name:s",
						"text:Value:Text1:s");									
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "RebarModulusOfElasticity") {
	$DisplayName = "Modulus of Elasticity";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "Rebar";
	$ParentDisplayName = "Rebar";
	$Items = array (	"text:Name:Name:s",
						"text:Nominal Value:Text1:s",
						"text:Average Value:Text2:s",
						"object:Test Values:BasicClass:RebarModulusOfElasticityTestValue:m");								
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "RebarModulusOfElasticityTestValue") {
	$DisplayName = "Test Value";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "RebarModulusOfElasticity";
	$ParentDisplayName = "Material Property Value";
	$Items = array (	"text:Name:Name:s",
						"text:Value:Text1:s");								
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "RebarYieldStress") {
	$DisplayName = "Yield Stress";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "Rebar";
	$ParentDisplayName = "Rebar";
	$Items = array (	"text:Name:Name:s",
						"text:Nominal Value:Text1:s",
						"text:Average Value:Text2:s",
						"object:Test Values:BasicClass:RebarYieldStressTestValue:m");								
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "RebarYieldStressTestValue") {
	$DisplayName = "Test Value";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "RebarYieldStress";
	$ParentDisplayName = "Yield Stress";
	$Items = array (	"text:Name:Name:s",
						"text:Value:Text1:s");								
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "RebarUltimateStress") {
	$DisplayName = "Ultimate Stress";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "Rebar";
	$ParentDisplayName = "Rebar";
	$Items = array (	"text:Name:Name:s",
						"text:Nominal Value:Text1:s",
						"text:Average Value:Text2:s",
						"object:Test Values:BasicClass:RebarUltimateStressTestValue:m");								
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "RebarUltimateStressTestValue") {
	$DisplayName = "Test Value";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "RebarUltimateStress";
	$ParentDisplayName = "Ultimate Stress";
	$Items = array (	"text:Name:Name:s",
						"text:Value:Text1:s");									
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "OtherMaterial") {
	$DisplayName = "Other Material";
	$SharedName = "Other Materials";
	$TableName = "BasicClass";	
	$Items = array (	"text:Name:Name:s",
						"textarea:Description:Text1:s",														
						"object:Material Property Files:Files:OtherMaterialMaterialPropertyFile:m");								
	$AllowSharing = "Yes";
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "OtherMaterialMaterialPropertyFile") {
	$DisplayName = "Material Property File";
	$ParentUniqueName = "OtherMaterial";
	$ParentDisplayName = "OtherMaterial";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "Actuator") {
	$DisplayName = "Actuator";
	$SharedName = "Actuators";
	$TableName = "BasicClass";	
	$Items = array (	"text:Name:Name:s",
						"textarea:Description:Text1:s",
						"object:Descriptive Files:Files:ActuatorDescriptiveFiles:m",
						"object:Specifications:Files:ActuatorSpecifications:m",
						"object:Drawings and Photos:Files:ActuatorDrawings:m",
						"break:here",
						"object:Fixed Node (AFN) Location Using Drawings and Photos:BasicClass:ActuatorFixedNodeLocationDrawings:s",
						"object:Fixed Node (AFN) Location Using Grid:BasicClass:ActuatorFixedNodeLocationGrid:s",
						"object:Structure Node (ASN) Location Using Drawings and Photos:BasicClass:ActuatorStructureNodeLocationDrawings:s",
						"object:Structure Node (ASN) Location Using Grid:BasicClass:ActuatorStructureNodeLocationGrid:s");								
	$AllowSharing = "Yes";
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "ActuatorDescriptiveFiles") {
	$DisplayName = "Descriptive Files";
	$ParentUniqueName = "Actuator";
	$ParentDisplayName = "Actuator";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "ActuatorSpecifications") {
	$DisplayName = "Specifications";
	$ParentUniqueName = "Actuator";
	$ParentDisplayName = "Actuator";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "ActuatorDrawings") {
	$DisplayName = "Actuator Drawing and/or Photo";
	$ParentUniqueName = "Actuator";
	$ParentDisplayName = "Actuator";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "ActuatorFixedNodeLocationDrawings") {
	$DisplayName = "Location Using Drawings and Photos";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "Actuator";
	$ParentDisplayName = "Actuator";
	$Items = array (	"text:Name:Name:s",
						"textarea:Description:Text1:s",														
						"object:Drawings and Photos:Files:ActuatorFixedNodeLocationDrawingsDrawings:m");								
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "ActuatorFixedNodeLocationDrawingsDrawings") {
	$DisplayName = "Drawing and/or Photo";
	$ParentUniqueName = "ActuatorFixedNodeLocationDrawings";
	$ParentDisplayName = "Location Using Drawings and Photos";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "ActuatorFixedNodeLocationGrid") {
	$DisplayName = "Location Using Location Points and Lines";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "Actuator";
	$ParentDisplayName = "Actuator";
	$Items = array (	"text:Name:Name:s",																				
						"object:Location Points:BasicClass:ActuatorFixedNodeLocationGridLocationPoint:m",
						"object:Location Lines:BasicClass:ActuatorFixedNodeLocationGridLocationLine:m");								
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "ActuatorFixedNodeLocationGridLocationPoint") {
	$DisplayName = "Location Point and its Location";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "ActuatorFixedNodeLocationGrid";
	$ParentDisplayName = "Location Using Location Points and Lines";
	$Items = array (	"text:Name:Name:s",								
						"textarea:Location Point Description:Text1:s",
						"text:Location of Location Point:Text2:s",
						"text:Reference Point Reference Plane:Text3:s",
						"text:Reference Point Horizontal Distance:Text4:s",
						"text:Reference Point Vertical Distance:Text5:s",
						"text:Reference Point 3D X Coordinate:Text6:s",
						"text:Reference Point 3D Y Coordinate:Text7:s",
						"text:Reference Point 3D Z Coordinate:Text8:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "ActuatorFixedNodeLocationGridLocationLine") {
	$DisplayName = "Location Line and its Location";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "ActuatorFixedNodeLocationGrid";
	$ParentDisplayName = "Location Using Location Lines and Lines";
	$Items = array (	"text:Name:Name:s",								
						"textarea:Location Line Description:Text1:s",
						"text:Location of Location Line:Text2:s",
						"text:Reference Line Reference Plane:Text3:s",
						"text:Reference Line Orientation:Text4:s",
						"text:Reference Line Distance:Text5:s",
						"text:Reference Line 3D Orientation:Text6:s",
						"text:Reference Line 3D X Coordinate:Text7:s",
						"text:Reference Line 3D Y Coordinate:Text8:s",
						"text:Reference Line 3D Z Coordinate:Text9:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "ActuatorStructureNodeLocationDrawings and Photos") {
	$DisplayName = "Location Using Drawings and Photos";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "Actuator";
	$ParentDisplayName = "Actuator";
	$Items = array (	"text:Name:Name:s",
						"textarea:Description:Text1:s",														
						"object:Drawings and Photos:Files:ActuatorStructureNodeLocationDrawingsDrawings:m");								
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "ActuatorStructureNodeLocationDrawingsDrawings") {
	$DisplayName = "Drawing and/or Photo";
	$ParentUniqueName = "ActuatorStructureNodeLocationDrawings";
	$ParentDisplayName = "Location Using Drawings";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "ActuatorStructureNodeLocationGrid") {
	$DisplayName = "Location Using Location Points and Lines";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "Actuator";
	$ParentDisplayName = "Actuator";
	$Items = array (	"text:Name:Name:s",																				
						"object:Location Points:BasicClass:ActuatorStructureNodeLocationGridLocationPoint:m",
						"object:Location Lines:BasicClass:ActuatorStructureNodeLocationGridLocationLine:m");								
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "ActuatorStructureNodeLocationGridLocationPoint") {
	$DisplayName = "Location Point and its Location";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "ActuatorStructureNodeLocationGrid";
	$ParentDisplayName = "Location Using Location Points and Lines";
	$Items = array (	"text:Name:Name:s",								
						"textarea:Location Point Description:Text1:s",
						"text:Location of Location Point:Text2:s",
						"text:Reference Point Reference Plane:Text3:s",
						"text:Reference Point Horizontal Distance:Text4:s",
						"text:Reference Point Vertical Distance:Text5:s",
						"text:Reference Point 3D X Coordinate:Text6:s",
						"text:Reference Point 3D Y Coordinate:Text7:s",
						"text:Reference Point 3D Z Coordinate:Text8:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "ActuatorStructureNodeLocationGridLocationLine") {
	$DisplayName = "Location Line and its Location";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "ActuatorStructureNodeLocationGrid";
	$ParentDisplayName = "Location Using Location Lines and Lines";
	$Items = array (	"text:Name:Name:s",								
						"textarea:Location Line Description:Text1:s",
						"text:Location of Location Line:Text2:s",
						"text:Reference Line Reference Plane:Text3:s",
						"text:Reference Line Orientation:Text4:s",
						"text:Reference Line Distance:Text5:s",
						"text:Reference Line 3D Orientation:Text6:s",
						"text:Reference Line 3D X Coordinate:Text7:s",
						"text:Reference Line 3D Y Coordinate:Text8:s",
						"text:Reference Line 3D Z Coordinate:Text9:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "OtherLoadingFixture") {
	$DisplayName = "Other Loading Fixture";
	$SharedName = "Other Loading Fixtures";
	$TableName = "BasicClass";	
	$Items = array (	"text:Name:Name:s",
						"textarea:Description:Text1:s",
						"object:Geometry:BasicClass:OtherLoadingFixtureGeometry:s",
						"object:Location Using Drawings and Photos:BasicClass:OtherLoadingFixtureLocationDrawings:s",
						"object:Location Using Grid:BasicClass:OtherLoadingFixtureLocationGrid:s",
						"break:here",
						"sharedobject:Steel Materials:BasicClass:Steel:m",
						"sharedobject:Concrete Materials:BasicClass:Concrete:m",
						"sharedobject:Rebar Materials:BasicClass:Rebar:m",
						"sharedobject:Other Materials:BasicClass:OtherMaterial:m");
	$AllowSharing = "Yes";
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "OtherLoadingFixtureGeometry") {
	$DisplayName = "Geometry";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "OtherLoadingFixture";
	$ParentDisplayName = "Other Loading Fixture";
	$Items = array (	"text:Name:Name:s",
						"textarea:Description:Text1:s",
						"object:Drawings and Photos:Files:OtherLoadingFixtureGeometryDrawing:m");								
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "OtherLoadingFixtureGeometryDrawing") {
	$DisplayName = "Drawing";
	$ParentUniqueName = "OtherLoadingFixtureGeometry";
	$ParentDisplayName = "Geometry";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "OtherLoadingFixtureLocationGrid") {
	$DisplayName = "Location Using Location Points and Lines";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "OtherLoadingFixture";
	$ParentDisplayName = "Other Loading Fixture";
	$Items = array (	"text:Name:Name:s",																				
						"object:Location Points:BasicClass:OtherLoadingFixtureLocationGridLocationPoint:m",
						"object:Location Lines:BasicClass:OtherLoadingFixtureLocationGridLocationLine:m");								
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "OtherLoadingFixtureLocationGridLocationPoint") {
	$DisplayName = "Location Point and its Location";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "OtherLoadingFixtureLocationGrid";
	$ParentDisplayName = "Location Using Location Points and Lines";
	$Items = array (	"text:Name:Name:s",								
						"textarea:Location Point Description:Text1:s",
						"text:Location of Location Point:Text2:s",
						"text:Reference Point Reference Plane:Text3:s",
						"text:Reference Point Horizontal Distance:Text4:s",
						"text:Reference Point Vertical Distance:Text5:s",
						"text:Reference Point 3D X Coordinate:Text6:s",
						"text:Reference Point 3D Y Coordinate:Text7:s",
						"text:Reference Point 3D Z Coordinate:Text8:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "OtherLoadingFixtureLocationGridLocationLine") {
	$DisplayName = "Location Line and its Location";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "OtherLoadingFixtureLocationGrid";
	$ParentDisplayName = "Location Using Location Lines and Lines";
	$Items = array (	"text:Name:Name:s",								
						"textarea:Location Line Description:Text1:s",
						"text:Location of Location Line:Text2:s",
						"text:Reference Line Reference Plane:Text3:s",
						"text:Reference Line Orientation:Text4:s",
						"text:Reference Line Distance:Text5:s",
						"text:Reference Line 3D Orientation:Text6:s",
						"text:Reference Line 3D X Coordinate:Text7:s",
						"text:Reference Line 3D Y Coordinate:Text8:s",
						"text:Reference Line 3D Z Coordinate:Text9:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}

else if ($UniqueName == "BracingAndReactionFixture") {
	$DisplayName = "Bracing and Reaction Fixture";
	$SharedName = "Bracing and Reaction Fixtures";
	$TableName = "BasicClass";	
	$Items = array (	"text:Name:Name:s",
						"textarea:Description:Text1:s",
						"object:Fixture Drawings and Photos:Files:BracingAndReactionFixtureDrawing:m");
	$AllowSharing = "Yes";
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "BracingAndReactionFixtureDrawing") {
	$DisplayName = "Fixture Drawing";
	$ParentUniqueName = "BracingAndReactionFixture";
	$ParentDisplayName = "Bracing and Reaction Fixture";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "Sensor") {
	$DisplayName = "Sensor";
	$SharedName = "Sensors";
	$TableName = "BasicClass";	
	$Items = array (	"text:Name:Name:s",
						"textarea:Description:Text1:s",
						"text:Calibration Constant:Text2:s",
						"datetime:Calibration Date:DateTime1:s",
						"object:Calibration:Files:SensorCalibration:s",
						"object:Specifications:Files:SensorSpecifications:m",						
						"object:Location Using Drawings and Photos:BasicClass:SensorLocationDrawings:s",
						"object:Location Using Grid:BasicClass:SensorLocationGrid:s");
	$AllowSharing = "Yes";
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "SensorSpecifications") {
	$DisplayName = "Specifications";
	$ParentUniqueName = "Sensor";
	$ParentDisplayName = "Sensor";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "SensorCalibration") {
	$DisplayName = "Calibration";
	$ParentUniqueName = "Sensor";
	$ParentDisplayName = "Sensor";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "SensorLocationDrawings") {
	$DisplayName = "Location Using Drawings and Photos";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "Sensor";
	$ParentDisplayName = "Sensor";
	$Items = array (	"text:Name:Name:s",
						"textarea:Description:Text1:s",														
						"object:Drawings and Photos:Files:SensorLocationDrawingsDrawings:m");								
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "SensorLocationDrawingsDrawings") {
	$DisplayName = "Drawing and/or Photo";
	$ParentUniqueName = "SensorLocationDrawings";
	$ParentDisplayName = "Location Using Drawings and Photos";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "SensorLocationGrid") {
	$DisplayName = "Location Using Location Points and Lines";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "Sensor";
	$ParentDisplayName = "Sensor";
	$Items = array (	"text:Name:Name:s",																				
						"object:Location Points:BasicClass:SensorLocationGridLocationPoint:m",
						"object:Location Lines:BasicClass:SensorLocationGridLocationLine:m");								
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "SensorLocationGridLocationPoint") {
	$DisplayName = "Location Point and its Location";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "SensorLocationGrid";
	$ParentDisplayName = "Location Using Location Points and Lines";
	$Items = array (	"text:Name:Name:s",								
						"textarea:Location Point Description:Text1:s",
						"text:Location of Location Point:Text2:s",
						"text:Reference Point Reference Plane:Text3:s",
						"text:Reference Point Horizontal Distance:Text4:s",
						"text:Reference Point Vertical Distance:Text5:s",
						"text:Reference Point 3D X Coordinate:Text6:s",
						"text:Reference Point 3D Y Coordinate:Text7:s",
						"text:Reference Point 3D Z Coordinate:Text8:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "SensorLocationGridLocationLine") {
	$DisplayName = "Location Line and its Location";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "SensorLocationGrid";
	$ParentDisplayName = "Location Using Location Lines and Lines";
	$Items = array (	"text:Name:Name:s",								
						"textarea:Location Line Description:Text1:s",
						"text:Location of Location Line:Text2:s",
						"text:Reference Line Reference Plane:Text3:s",
						"text:Reference Line Orientation:Text4:s",
						"text:Reference Line Distance:Text5:s",
						"text:Reference Line 3D Orientation:Text6:s",
						"text:Reference Line 3D X Coordinate:Text7:s",
						"text:Reference Line 3D Y Coordinate:Text8:s",
						"text:Reference Line 3D Z Coordinate:Text9:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "Cable") {
	$DisplayName = "Cable";
	$SharedName = "Cables";
	$TableName = "BasicClass";	
	$Items = array (	"text:Name:Name:s",
						"textarea:Description:Text1:s",
						"text:Cable ID:Text2:s");
	$AllowSharing = "Yes";
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "PhysicalSubstructureTest") {
	$DisplayName = "Physical Substructure Test";	
	$SharedName = "Physical Substructure Tests";
	$TableName = "BasicClass";		
	$Items = array (	"text:Name:Name:s",
						"textarea:Description:Text1:s",								
						"datetime:Start Date and Time:DateTime1:s",
						"datetime:End Date and Time:DateTime2:s",
						"textarea:Uncontrolled Test Condition:Text2:s",
						"object:Descriptive Files:Files:PhysicalSubstructureTestDescriptiveFile:m",												
						"break:here",						
						"sharedobject:Persons:BasicClass:Person:m",
						"break:here",						
						"sharedobject:Test Protocol:BasicClass:Protocol:s",
						"object:Data Set:BasicClass:PhysicalSubstructureTestDataSet:s");
	$AllowSharing = "No";								
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "PhysicalSubstructureTestDescriptiveFile") {
	$DisplayName = "Descriptive File";
	$ParentUniqueName = "PhysicalSubstructureTest";
	$ParentDisplayName = "Physical Substructure Test";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "PhysicalSubstructureTestDataSet") {   		
	$DisplayName = "Data Set";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "PhysicalSubstructureTest";
	$ParentDisplayName = "Test";
	$Items = array (	"text:Name:Name:s",
						"textarea:Description:Text1:s",
						"object:Descriptive Files:Files:PhysicalSubstructureTestDataSetDescriptiveFile:m",
						"object:Raw Binary Data:Files:PhysicalSubstructureTestRawBinaryData:m",								
						"object:Raw Engineering Unit Data:Files:PhysicalSubstructureTestRawEngineeringUnitData:m",
						"object:Corrected Data:Files:PhysicalSubstructureTestCorrectedData:m",
						"object:Processed Data:Files:PhysicalSubstructureTestProcessedData:m",
						"object:Images:Files:PhysicalSubstructureTestImage:m",															
						"object:Videos:Files:PhysicalSubstructureTestVideo:m",
						"object:Webcam Images:Files:PhysicalSubstructureTestWebcamImage:m",
						"object:Timelapsed Video:Files:PhysicalSubstructureTestTimelapsedVideo:m",
						"object:Composite Video:Files:PhysicalSubstructureTestCompositeVideo:m",
						"object:Other Files:Files:PhysicalSubstructureTestOtherFile:m");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "PhysicalSubstructureTestDataSetDescriptiveFile") {
	$DisplayName = "Descriptive File";
	$ParentUniqueName = "PhysicalSubstructureTestDataSet";
	$ParentDisplayName = "Data Set";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "PhysicalSubstructureTestRawBinaryData") {
	$DisplayName = "Raw Binary Data";
	$ParentUniqueName = "PhysicalSubstructureTestDataSet";
	$ParentDisplayName = "Data Set";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "PhysicalSubstructureTestRawEngineeringUnitData") {
	$DisplayName = "Raw Engineering Unit Data";
	$ParentUniqueName = "PhysicalSubstructureTestDataSet";
	$ParentDisplayName = "Data Set";
	$Items = array (	"textarea:Description:Text1:s");	
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "PhysicalSubstructureTestCorrectedData") {
	$DisplayName = "Corrected Data";
	$ParentUniqueName = "PhysicalSubstructureTestDataSet";
	$ParentDisplayName = "Data Set";
	$Items = array (	"textarea:Description:Text1:s");	
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "PhysicalSubstructureTestProcessedData") {
	$DisplayName = "Processed Data";
	$ParentUniqueName = "PhysicalSubstructureTestDataSet";
	$ParentDisplayName = "Data Set";	
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "PhysicalSubstructureTestImage") {
	$DisplayName = "Image";
	$ParentUniqueName = "PhysicalSubstructureTestDataSet";
	$ParentDisplayName = "Data Set";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "PhysicalSubstructureTestWebcamImage") {
	$DisplayName = "Webcam Image";
	$ParentUniqueName = "PhysicalSubstructureTestDataSet";
	$ParentDisplayName = "Data Set";
	$Items = array (	"textarea:Description:Text1:s");	
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "PhysicalSubstructureTestVideo") {
	$DisplayName = "Video";
	$ParentUniqueName = "PhysicalSubstructureTestDataSet";
	$ParentDisplayName = "Data Set";
	$Items = array (	"textarea:Description:Text1:s");	
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "PhysicalSubstructureTestTimelapsedVideo") {
	$DisplayName = "Timelapsed Video";
	$ParentUniqueName = "PhysicalSubstructureTestDataSet";
	$ParentDisplayName = "Data Set";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "PhysicalSubstructureTestCompositeVideo") {
	$DisplayName = "Composite Video";
	$ParentUniqueName = "PhysicalSubstructureTestDataSet";
	$ParentDisplayName = "Data Set";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "PhysicalSubstructureTestOtherFile") {
	$DisplayName = "Other File";
	$ParentUniqueName = "PhysicalSubstructureTestDataSet";
	$ParentDisplayName = "Data Set";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "AnalyticalSubstructureComputation") {
	$DisplayName = "Computation";	
	$SharedName = "Analytical Substructure Computations";
	$TableName = "BasicClass";		
	$Items = array (	"text:Name:Name:s",
						"textarea:Description:Text1:s",								
						"datetime:Start Date and Time:DateTime1:s",
						"datetime:End Date and Time:DateTime2:s",
						"object:Descriptive Files:Files:AnalyticalSubstructureComputationDescriptiveFile:m",																												
						"break:here",													
						"sharedobject:Persons:BasicClass:Person:m",
						"break:here",						
						"sharedobject:Computation Protocol:BasicClass:Protocol:s",
						"object:Data Set:BasicClass:AnalyticalSubstructureComputationDataSet:s");
	$AllowSharing = "No";																		
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "AnalyticalSubstructureComputationDescriptiveFile") {
	$DisplayName = "Descriptive File";
	$ParentUniqueName = "AnalyticalSubstructureComputation";
	$ParentDisplayName = "Computation";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "AnalyticalSubstructureComputationDataSet") {   		
	$DisplayName = "Data Set";	
	$TableName = "BasicClass";	
	$ParentUniqueName = "AnalyticalSubstructureComputation";
	$ParentDisplayName = "Computation";
	$Items = array (	"text:Name:Name:s",
						"textarea:Description:Text1:s",
						"object:Descriptive Files:Files:AnalyticalSubstructureComputationDataSetDescriptiveFile:m",
						"object:Output Data:Files:AnalyticalSubstructureComputationOutputData:m",
						"object:Processed Data:Files:AnalyticalSubstructureComputationProcessedData:m",
						"object:Images:Files:AnalyticalSubstructureComputationImage:m",															
						"object:Videos:Files:AnalyticalSubstructureComputationVideo:m",							
						"object:Other Files:Files:AnalyticalSubstructureComputationOtherFile:m");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
else if ($UniqueName == "AnalyticalSubstructureComputationDataSetDescriptiveFile") {
	$DisplayName = "Descriptive File";
	$ParentUniqueName = "AnalyticalSubstructureComputationDataSet";
	$ParentDisplayName = "Data Set";
	$Items = array (	"textarea:Description:Text1:s");	
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "AnalyticalSubstructureComputationOutputData") {
	$DisplayName = "Output Data";
	$ParentUniqueName = "AnalyticalSubstructureComputationDataSet";
	$ParentDisplayName = "Data Set";
	$Items = array (	"textarea:Description:Text1:s");	
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "AnalyticalSubstructureComputationProcessedData") {
	$DisplayName = "Processed Data";
	$ParentUniqueName = "AnalyticalSubstructureComputationDataSet";
	$ParentDisplayName = "Data Set";
	$Items = array (	"textarea:Description:Text1:s");	
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "AnalyticalSubstructureComputationImage") {
	$DisplayName = "Image";
	$ParentUniqueName = "AnalyticalSubstructureComputationDataSet";
	$ParentDisplayName = "Data Set";
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "AnalyticalSubstructureComputationVideo") {
	$DisplayName = "Video";
	$ParentUniqueName = "AnalyticalSubstructureComputationDataSet";
	$ParentDisplayName = "Data Set";	
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}
else if ($UniqueName == "AnalyticalSubstructureComputationOtherFile") {
	$DisplayName = "Other File";
	$ParentUniqueName = "AnalyticalSubstructureComputationDataSet";
	$ParentDisplayName = "Data Set";	
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}

else
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/index.php");				
 
?>
