<?php
/* Search feature that I don't really like
if (isset($UniqueName) && isset($_GET['View'])) {
	if (!isset($_GET["List"])) {
		$classIDname = ($UniqueName . "ID"); 
		echo "<div id=\"footer_search\">\n". 	 				 
			 " <form style=\"padding: 1px;\" name=\"search\" action=\"search.php\" method=\"get\">\n".
		  	 "  <input type=\"text\" name=\"term\" /> \n".
		  	 "  <button type=\"submit\" class=\"search\"><img src=\"images/magnifier.png\" alt=\"\"/>Search (beta)</button>\n".
		  	 "  <input type=\"hidden\" name=\"UniqueName\" value=\"$UniqueName\" />\n".
		  	 "  <input type=\"hidden\" name=\"ClassID\" value=\"".$_SESSION[$classIDname]."\" />\n".
		  	 "  <input type=\"hidden\" name=\"Search\" value=\"Search\" />\n".   	  	
			 "  </form>\n".
			 "</div>\n";
	}
}
*/
echo '<div align="right" id="footer"><h1>Metadata Web v1.4</h1>';
?>