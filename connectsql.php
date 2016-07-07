<?php 
include ('MysqlSet.inc.php');

$category=$_POST['category'];
$q=mysql_query("SELECT * from record where category='".$category."' limit 10");
while($e=mysql_fetch_assoc($q))
$output[]=$e;

mysql_query($sql,$con);
$sql = "select * from record where number = ".$number;
$result = mysql_query($sql) or die('MySQL query error');
  
//while($row = mysql_fetch_array($result))
//{
 //  echo $row['id']." ";
//}

mysql_close($con);
?>