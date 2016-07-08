<?php
header('refresh: 5; url = "sortdist.php"');
include("MysqlSet.inc.php");

$sql = "SELECT * FROM assigntable";
$result = mysql_query($sql) or die('select error');
 
$num = 0;
$auth[0]="";
$dist[0]="";

$row = mysql_fetch_array($result);
$reportid = $row[3];
echo $reportid;
while($row = mysql_fetch_array($result))
{
	if($row[3] == $reportid)
	{
		$auth[$num] = $row[1];
		$dist[$num] = $row[2];
		$num++;
	}
}

for($i = 0; $i < $num; $i++)
{
    for($j = 1; $j < ($num - $i); $j++)
    {
        if($dist[$j]*1 < $dist[$j-1]*1)
        {
            $templen = $dist[$j];
            $tempaddr = $auth[$j];
            $dist[$j] = $dist[$j-1];
            $auth[$j] = $auth[$j-1];  
            $dist[$j-1] =  $templen;
            $auth[$j-1] =  $tempaddr;
        }
    }
}

echo $auth[0];
//echo $dist[0];
$sql = "SELECT * FROM policeinfo where pAddress = '$auth[0]'";
$result = mysql_query($sql) or die('error3');
while($row = mysql_fetch_array($result))
{
	$pID = $row[0];
}
$update = "UPDATE police SET  policestationID = '$pID' WHERE reportid = '$reportid'";
$modlat = mysql_query($update) or die('error5');

//echo $row[0];
//$update = "UPDATE police SET  policestationID = '$row[0]' WHERE reportid = $reportid";
//$modlat = mysql_query($update) or die('error5');

$del = "DELETE FROM assigntable where reportid = $reportid";
$sql = mysql_query($del) or die('delelte error');

?>