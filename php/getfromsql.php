<?php
include("MysqlSet.inc.php");

$sql = "select * from recordt";
$result = mysql_query($sql) or die('error');

echo "目前有".mysql_num_rows($result)."筆報案紀錄";

echo "<table width = 80% align = center cellpadding = 0 cellsapcing = 0>";
echo "<tr bgcolor = yellow>
	  <td>id</td>
	  <td>時間</td>
	  <td>傷亡人數</td>
	  <td>通報機關</td>
	  <td>報案類別1</td>
	  <td>報案類別2</td>
	  <td>照片數量</td> 
	  <td>地址</td>
	  <td>經度</td> 
	  <td>緯度</td>
	  </tr>";

while($row = mysql_fetch_array($result))
{
	echo "<tr bgcolor = white>
		  <td>$row[0]</td>
		  <td>$row[2]</td>
   		  <td>$row[3]</td>
          <td>$row[4]</td>
          <td>$row[5]</td>
          <td>$row[6]</td>
          <td>$row[7]</td>
          <td>$row[8]</td>
          <td>$row[9]</td>
          <td>$row[10]</td>
          </tr>";
}
?>