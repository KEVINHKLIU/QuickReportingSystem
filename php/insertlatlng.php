<?php
include("MysqlSet.inc.php");

$latitude = $_GET["lng"];
//$longitude = $_GET["lng"];
//$count = $_GET["count"];
//echo $latitude[3];
//echo $longitude
//echo $count;
$count = 0;

for($i = 0; $i < 10;$i++)
{
	$out[$i]="";
}
$i=0;
while($latitude[$i] != "")
{
	if($latitude[$i] != ',')
	{
		$out[$count] = $out[$count].$latitude[$i];
		$i++;
	}
	else
	{
		$i++;
		$count++;
	}
}
//echo $out[0];
$m=41;
for($i = 0; $i < 10; $i++)
{
	$updatelat = "UPDATE firestationinfo SET longitude = $out[$i] WHERE id = $m";
	$modlat = mysql_query($updatelat) or die('error3');
	$m++;
}
//$updatelng = "UPDATE policeinfo SET longitude = $longitude WHERE id = $count";
//$modlng = mysql_query($updatelat) or die('error4');

?>