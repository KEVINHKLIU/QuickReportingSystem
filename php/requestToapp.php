<?php
include("MysqlSet.inc.php");

header('Content-type : text/html; charset = utf-8');

$auth = $_POST["auth"];
$username = $_POST["username"];
$password = $_POST["password"];

if($auth*1 == 0)
{
	$sql = "SELECT * FROM policeid where policeofficeid = '$username'";
	$result = mysql_query($sql) or die ("connect error");
	
	$start =2;
	$end = 12;
}
else if($auth*1 == 1)
{
	$sql = "SELECT * FROM firestationid where firestationid = '$username'";
	$result = mysql_query($sql) or die ("connect error");	

	$start =2;
	$end = 12;
}

$flag=0;
$row = mysql_fetch_array($result);

for($i = $start; $i < $end; $i++)
{
	if($password == $row[$i])
	{
		echo "success";
		$flag=1;
		break;
	}
	$i++;
}
if($row[0] == "")
{
	$flag=1;
	echo "username error";
}

if($flag == 0)
	echo "password not found";

?>
