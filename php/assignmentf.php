<?php
include("MysqlSet.inc.php");

$auth = $_GET["auth"];
$count = 0;
$num = "";
$n = 0;

while($auth[$n] != ',')
{
	$num = $num.$auth[$n];
	$n++;
}
$i = $n + 1;

for($n = 0; $n < ($num * 1 - 1); $n++)
{
	$out[$i]="";
}

$countid = 0;

while($auth[$i] != "")
{
	if($auth[$i] != ',')
	{
		$out[$count] = $out[$count].$auth[$i];
		$i++;
	}
	else
	{
		if($countid == 0)
		{
			$reportid = $out[$count];
			$out[$count] = "";
			$countid++;
			$count--;
		}
		$i++;
		$count++;
	}
}

for($i = 0; $i < $num *	1 - 1; $i++)
{
	for($j = 0; $j < ($num * 1 - 1 - $i - 1); $j ++)
	{
		if($out[$j] > $out[$j+1])
		{
			$temp = $out[$j];
			$out[$j] = $out[$j+1]; 
			$out[$j+1] =  $temp;
		}
	}
}
$update = "UPDATE firestation SET  firestationID = (select id from firestationinfo where pAddress = $out[0]) WHERE userid = $reportid";
$modlat = mysql_query($update) or die('error3');

?>