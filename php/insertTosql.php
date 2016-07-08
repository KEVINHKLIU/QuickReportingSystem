<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>insert sql</title>
</head>
<body>
<form name="form" method="get" action="insertTosql.php">
請輸入時間:
<input type = "text" name = "time" maxlength="10" size="10"><br>
<input type = "text" name = "type" maxlength="10" size="10"><br>

<input type="submit" value="完成">
<input type="reset"></form>
</body>
</html>


<?php 
header('Content-Type: text/html; charset=utf-8');
$con = mysql_connect('localhost', 'lab0123_GT', '01230123', 'lab0123_emerge');

mysql_query("SET NAMES 'utf8'");
if (!$con)
{
	 die('Could not connect:'.mysql_error());
}

if ($_GET['time'])
{
	$gettime=$_GET['time'];
	$gettype=$_GET['type'];
	$sql="insert record(時間,報案類別) value($gettime,'$gettype')";
	echo $sql;
	$result = mysql_query($sql);
	if(mysql_affected_rows() >= 1)
		echo"insert successfully";
}


mysql_select_db("lab0123_emerge", $con);

mysql_close($con);
?>