<?php
//header('refresh: 5; url = "distance_sort_police.php"');
if(isset($_GET['request']))
{
	include("MysqlSet.inc.php");


	$reportid = $_GET['request'];

	$sql = "SELECT * FROM assigntable where reportid = '$reportid'";
	$result = mysql_query($sql) or die('select error');
	 
	$num = 0;

	while($row = mysql_fetch_array($result))
	{
			$auth[$num] = $row[1];
			$dist[$num] = $row[2];
			$num++;
	}
	if($num >= 1)
	{
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
	}

	$full = 0;
	for($i = 0; $i < $num; $i++)
	{
		$sql = "SELECT id FROM policeinfo where pAddress = '$auth[$i]'";
		$result = mysql_query($sql) or die('select policeid error');
		$row = mysql_fetch_array($result);
		$policeID = $row[0];
		$sel = "SELECT * FROM policeonduty where id = '$row[0]'";
		$conn = mysql_query($sel) or die('select available police errror');
		$temp = mysql_fetch_array($conn);
		for($j = 2; $j < 12; $j++)
		{
			if($temp[$j] == 1)
			{
				$full =1;
				break;
			}
		}
		if($full == 0)
			break;
	}

	$update = "UPDATE police SET  policestationID = '$policeID' WHERE reportid = '$reportid'";
	$modlat = mysql_query($update) or die('error5');

	$del = "DELETE FROM assigntable where reportid = '$reportid'";
	$sql = mysql_query($del) or die('delete error');
	

}


?>