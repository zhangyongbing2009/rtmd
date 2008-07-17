Metadata Web
Lehigh Data Model Implementation
Author: Thomas Marullo
Copyright: Lehigh University RTMD
 
 
** DESCRIPTION **
This web application provides a flexible framework for organizing and archiving data and metadata
for structural experiments.  Users can enter data for particular sections of the data model hierarchy and
upload files and organize them.  

This application provides the implementation of the Lehigh Data Model through the use of 
PHP and MySQL.  All classes in the Model are detailed here.  Users can modify or create their own
models using the framework described below

The main file is 'switchboard.php' where all the class information is kept.  There are two main sections 
to 'switchboard.php' where events occur.  Section 1 retrieves the requested class and Section 2 provides 
the details of the class and what data to retrieve from the MySQL database.  
See ** ADDING TO MODEL ** for further details.


** REQUIREMENTS **
PHP, MySQL, Perl, Apache


** INSTALLATION **
1- Extract files to your webserver
2- Set information in the config/config.php
3- Edit the mdw-header.cgi file in the cgi-bin directory and change $tmp_dir to the
   webserver's location of the applications files directory (ie: "/var/www/html/metadataweb/files/")
4- Move the files in the application's cgi-bin directory to webserver's /cgi-bin
5- chmod u+x on the previous files.  You may have to add g+x depending on your webserver.
6- Delete the applications cgi-bin directory
7- chmod u+w, and g+w if necessary, to files directory for write access.  Also make sure the to chown to apache:apache
   or whatever user/group runs the webserver daemon
8- Set the first three variables in config/createmysql.php
9- Use a browser to go to the config/createmysql.php to create the databases and then delete the config/createmysql.php file
10- After going to the application, log in as admin and you should change the admin password
11- Users can also change the header image by replacing the header.jpg image in the images folder with their own


** UPGRADE **
1- Back up current 'files' directory in your application path
2- Back up your config/config.php file and remove the config/createmysql.php file since its not necessary
3- Overwrite the existing installation
4- Restore your original config/config.php file and remove the config/createmysql.php file
5- Restore original 'files' directory, chown to apache:apache or whatever user/group runs the webserver
   and chmod u+w to files directory, and g+w if necessary.


** USAGE **
A user must log into the application in order to view, edit or create a project.  Through the use of
simple web forms, the data model can be populated. 

The admin user is the only one who can assign members to projects and create project admins.  From the main page, the admin
will 


** ADDING TO MODEL **
Adding to or changing the Lehigh Data Model should not be difficult so that is why there are two basic templates 
to utilize.  Note, there should always be a Project root section.
	sharedobject.php:	
	                Template for a basic class that contains the methods to add, view and edit a class.  					
					All non-file classes utilizes the 'BasicClass' table in the database.  This table provides 
					a simple framework for a class with a name, up to 9 text fields and 2 datetime properties 
					for start date and end date.  
					To create a reference to another class (a child class), a new object item must be added to the 
					$Items array.  
					A framework for a class is defined and has required properties for defining new objects or shared
					objects.  An object has only one parent whereas a shared object can have multiple parents and be
					referenced in multiple places.  				
					
					Framework:  Below is the necessary code for adding an object to Section 2B of 'switchboard.php'.
											
							    // A new object called MyClass
								else if ($UniqueName == "MyClass") {						
									$DisplayName = "My Class";
									$TableName = "BasicClass";
									$ParentUniqueName = "ParentClass";
									$ParentDisplayName = "Parent Class";
									$Items = array (		"text:Name:Name:s",
															"textarea:display_text:name_in_table:s",
															"radio:display_text:name_in_table:s",
															"datetime:display_text:name_in_table:s",
															"object:display_text:table_name:unique_name:s",															
															"sharedobject:display_text:table_name:unique_name:s",
															"break:here",
															"object:display_text:table_name:unique_name:m",															
															"sharedobject:display_text:table_name:unique_name:m");
									require "sharedobject.php";
								}
								
								To make an object shared, the following variables must be added to the framework:
								$SharedName = "Plural Version of the Display Name";
								$AllowSharing = "No";	// No = Only allowed in the opened project,
														// Yes = Can be used in other projects
					
								$UniqueName must ALWAYS be unique for that class.  It is how the program
								differentiates between other classes.  $DisplayName is what is show to the
								user.  It can be duplicated.  $TableName is the reference to a table in the 
								database.  It must be 'BasicClass' or one of its derivates (if made).  The two 
								$Parent variables must reference the class' parent in the same manner.  
																
								$Items is an array that lists all the attributes and objects that a class can have.   
								The format is type:display name:database reference:single or multiple.
								The use of ":s" at the end of the item means it can only have 1 instance.
								The use of ":m" at the end of the item means it can multiple instances.
																
								The first property in $Items must ALWAYS be "text:Name:Name:s",.  Text, textarea,
								radio and datetime are all attributes that appears in order as text items.
								Object, objectlink and sharedobject are references to other objects and appear as blocks 
								in the order they are listed, 2 on each row.  Each array can have as many objects as 
								necessary.
								The use of "break:here", allows for subsections in the object list.  A horizontal
								line will appear below the previous object and the next object will appear below.
								
								The types of attributes allowed are:
									s	text:			String
									s	textarea:		Paragraph
									s	radio:			Multiple option buttons where one can be selected
									s	datetime:		Date and Time
									
								The types of objects allowed are:
									sm	object:			A reference to an object class which is a child
									sm	objectlink: 	A link to an object class which is a child of another class
									sm	sharedobject:	A reference to a shared class
									
									(s = Single instance, sm = Single or Multiple instance)
	
	file.php:
					Provides the framework for any type of object that is meant to store a file.
					It utilizes a similar framework as a sharedobject except it requires a different template:

					require "templates/file.php";	
					
					Files can not be shared objects.
					
					Framework:  Below is the necessary code for adding a file object to Section 2B of 'switchboard.php'.
						
								// A new file object called MyFileClass
								else if ($UniqueName == "MyFileClass") {
									$DisplayName = "My File";
									$ParentUniqueName = "ParentClass";	
									$ParentDisplayName = "Parent Class";
									$Items = array (		"text:Name:Name:s",
															"textarea:display_text:name_in_table:s",
															"radio:display_text:name_in_table:s",
															"datetime:display_text:name_in_table:s");
									require "templates/file.php";	
								}
								
								Note that $TableName is not used because these objects are statically referenced to the 
								table "Files".
								
								The first property in $Items must ALWAYS be "text:Name:Name:s",.  These objects appear 
								in the order they are listed, 2 on each row.  Each array can have as many objects as 
								necessary.
								
								The types of attributes allowed are:
									s	text:			String
									s	textarea:		Paragraph
									s	radio:			Multiple option buttons where one can be selected
									s	datetime:		Date and Time									
									
									(s = Single instance)
	

** EXAMPLE MODEL TO START WITH **
The following shows a simple model to start with where the hierarchy is:
Project
->ProjectTask
--*Descriptive File
--*System

/***** SECTION 2B: Standard object details *****/
else if ($UniqueName == "ProjectTask") {    	
	$DisplayName = "Project Task";
	$TableName = "BasicClass";
	$ParentUniqueName = "Project";
	$ParentDisplayName = "Project";	
	$Items = array (	"text:Name:Name:s",
						"textarea:Description:Text1:s",
						"object:Descriptive Files:Files:ProjectTaskDescriptiveFile:m",
						"break:here",
						"sharedobject:System:BasicClass:System:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}		
else if ($UniqueName == "ProjectTaskDescriptiveFile") {
	$DisplayName = "Descriptive File";
	$ParentUniqueName = "ProjectTask";
	$ParentDisplayName = "Project Task";	
	$Items = array (	"textarea:Description:Text1:s");
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/file.php");	
}

/***** SECTION 2C: Shared objects *****/
else if ($UniqueName == "System") {
	$DisplayName = "System";
	$SharedName = "Systems";
	$TableName = "BasicClass";		
	$Items = array (	"text:Name:Name:s",
						"textarea:Description:Text1:s",
	$AllowSharing = "Yes";
	require ($_SERVER["DOCUMENT_ROOT"]."/".$source_location."/templates/sharedobject.php");
}
	