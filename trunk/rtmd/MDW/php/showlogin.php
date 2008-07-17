<?php
if (isset($_SESSION['logged_in']) && $_SESSION['logged_in'] == 1) {
 echo $_SESSION['username'].' <a class="login" href="/'.$source_location.'/Users/logout.php">[logout]</a>';
} else {
	echo '<a class="login" href="/'.$source_location.'/Users/login.php">[login]</a>';
}
?>