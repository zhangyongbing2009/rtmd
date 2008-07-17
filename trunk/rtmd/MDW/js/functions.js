/* 
 * PHP File Uploader with progress bar Version 1.20
 * Copyright (C) Raditha Dissanyake 2003
 * http://www.raditha.com

 * Licence:
 * The contents of this file are subject to the Mozilla Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/MPL/
 * 
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 * 
 * The Initial Developer of the Original Code is Raditha Dissanayake.
 * Portions created by Raditha are Copyright (C) 2003
 * Raditha Dissanayake. All Rights Reserved.
 * 
 */
var postLocation="pgbar.php";

function popUP(mypage, myname, w, h, scroll, titlebar)
{

	var winl = (screen.width - w) / 2;
	var wint = (screen.height - h) / 2;
	winprops = 'height='+h+',width='+w+',top='+wint+',left='+winl+',scrollbars='+scroll+',resizable'
	win = window.open(mypage, myname, winprops)
	if (parseInt(navigator.appVersion) >= 4) {
		win.window.focus();
	}
}

function postIt(form,url)
{	    
 // Check file name entered
  baseUrl = url + "/php/pgbar.php";
  sid = form.sessionid.value;
  iTotal = escape("-1");
  baseUrl += "?iTotal=" + iTotal;
  baseUrl += "&iRead=0";
  baseUrl += "&iStatus=1";
  baseUrl += "&sessionid=" + sid;
      
  popUP(baseUrl,"Uploader",460,262,false,false);
  form.submit();
}
/// End uploader functions

// Confirm a DELETE request
function confirmdelete(source){
 if (source == "object") {
	 msg = "This will delete the object\nClick OK to confirm DELETE";
	 return confirm(msg);
 }
 else if (source == "link") {
 	 msg = "This will delete the object link only\nClick OK to confirm DELETE";
	 return confirm(msg);
 }
}

// Confirm a DELETE USER request
function confirmdeleteuser(username){
 msg = "This will delete the user " + username + "\nClick OK to confirm DELETE";
 if (confirm(msg)) {
  window.location.href="Users/deleteuser.php?Username=" + username;
 }
}

// Request a project from the admin
function requestproject(project,username){
 msg = "Are you sure you want to request membership to project: " + project;
 if (confirm(msg)) {
  window.location.href="Users/projectrequest.php?Project=" + project + "&Username=" + username;
 }
}


// Toggle hide/show for explorer like menu
function hideshow(id){
 var label = 'label' + id;  
 if (document.getElementById(id).style.display == 'none') {
  document.getElementById(id).style.display = 'block';
  document.getElementById(label).innerHTML = "&#151;";
 }
 else {
  document.getElementById(id).style.display = 'none';
  document.getElementById(label).innerHTML = "+";
 }
}

// Check if a field is not empty
function isNotEmpty(aTextField) {
 // First check for zero-length or null values
 if ((aTextField.value.length==0) || (aTextField.value==null)) {
  alert('Please fill in required information');
  return false;
 }
   
 // Check for just white spaces
 var re = /\s/g; //Match any white space including space, tab, form-feed, etc.
 RegExp.multiline = true; // IE support
 var str = aTextField.value.replace(re, "");
 if (str.length == 0) {
  alert('Please fill in required information');
  return false;
 }
   
 // If all passes, its ok
 return true;
}

// Check if passwords match for changing password 
function checkpasswords() {
	if (isNotEmpty(document.getElementsByName("currentpwd")[0]) && isNotEmpty(document.getElementsByName("newpwd")[0]) && isNotEmpty(document.getElementsByName("retypenewpwd")[0])) {	
		if ( document.getElementsByName("newpwd")[0].value == document.getElementsByName("retypenewpwd")[0].value)	
			form.submit();
		else {
			alert("New password doesn't match");
			return false;
		}
	}	
	else 
		return false;	
}

// Tiny MCE
function convertWord(type, content) {
        return content;
}

// Toggling object block arrow
function toggleArrow(obj,base) {		
		var arrow = obj + "_arrow";
				
		// Toggle arrow        
        var arrow_el = document.getElementById(arrow);
        // Closed arrow
        if (arrow_el.innerHTML.indexOf('arrow_closed.gif') > -1) {
            // Toggle arrow image open
    	    arrow_el.innerHTML="<img src='/"+base+"/images/arrow_open.gif' onclick='toggleArrow(\"" + obj + "\",\"" + base + "\");Effect.toggle(\"" +obj + "_content\",\"blind\",{duration: 0.2});' />";
        }
        else {
        	// Toggle arrow image closed
    	    arrow_el.innerHTML="<img src='/"+base+"/images/arrow_closed.gif' onclick='toggleArrow(\"" + obj + "\",\"" + base + "\");Effect.toggle(\"" +obj + "_content\",\"blind\",{duration: 0.2});' />";
        }        
}

