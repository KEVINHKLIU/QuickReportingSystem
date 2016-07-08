<?php 
	//接收來自distance_hospital.php的ajax
	if(isset($_GET['dist']))
	{
		include("MysqlSet.inc.php");
		$dist = $_GET['dist'];
		$addr = $_GET['address'];
		$id = $_GET['id'];
		//暫存distance_police.php的計算結果於資料庫
    	$upd = "UPDATE assigntable_hospital set dist = '$dist' where address = '$addr' && reportid = '$id'";
    	$sql = mysql_query($upd) or die('error');

    	mysql_close($con);
	}
?>
