<?php 

$dbServer = 'localhost';
$dbUser = 'Raymond';
$dbPass = '01230123';
$dbName = 'lab0123';

$con = mysql_connect($dbServer, $dbUser, $dbPass, $dbName);

if (!$con)
{
	 die('Could not connect DB');
}
mysql_query("SET NAMES 'utf8'");

if(! mysql_select_db($dbName))
	die("Counld not use DB");

?>