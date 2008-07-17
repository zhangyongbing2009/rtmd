// AJAX browser check
function createXMLHttpRequest() {
 var ua;
 if(window.XMLHttpRequest) {
    try {
      ua = new XMLHttpRequest();
    } catch(e) {
      ua = false;
    }
  } else if(window.ActiveXObject) {
    try {
      ua = new ActiveXObject("Microsoft.XMLHTTP");
    } catch(e) {
      ua = false;
    }
  }
  return ua;
}

// Create the request handler
var req = createXMLHttpRequest();

// Button action opens PHP file
function sendRequest(name,admin) {
  var projects =  document.getElementById(name).value;
  var projectsadmin =  document.getElementById(admin).value;
  
  // Open the PHP file and run the mysql code
  req.open('get', 'Users/userchange.php?Username=' + name + '&Projects=' + projects + '&ProjectsAdmin=' + projectsadmin);  

  // Get the echo response from the PHP file
  req.onreadystatechange = handleResponse;

  // Finish the transaction
  req.send(null);
}


// Get the response from the button action
//  AJAX readyState Status Codes:

//  0 - uninitialized
//  1 - loading
//  2 - loaded
//  3 - interactive
//  4 - complete
function handleResponse() {
 
  // Completed transaction
  if(req.readyState == 4){
    // Get the echo response
    var response = req.responseText;
  
    var update = new Array();
    
    alert("Updated");
            
  }
  else
  ; 
}