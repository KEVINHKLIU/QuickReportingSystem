<?php 
	//接收來自distance_ambulance.php的ajax
	if(isset($_GET['dist']))
	{
		include("MysqlSet.inc.php");
		$dist = $_GET['dist'];
		$addr = $_GET['address'];
		$id = $_GET['id'];
		$caseID = $_GET['caseID'];
		//暫存distance_police.php的計算結果於資料庫
    	$ins = "INSERT INTO assigntable_ambulance (address, dist, reportid, caseID) VALUES('$addr', '$dist', '$id', '$caseID')";
    	$sql = mysql_query($ins) or die('error');

    	mysql_close($con);
	}
?>
