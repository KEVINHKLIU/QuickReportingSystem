<?php 
	//接收來自distance_police.php的ajax
	if(isset($_GET['dist']))
	{
		include("MysqlSet.inc.php");
		$dist = $_GET['dist'];
		$addr = $_GET['address'];
		$id = $_GET['id'];
		//暫存distance_police.php的計算結果於資料庫
    	$ins = "INSERT INTO assigntable_police (address, dist, reportid) VALUES('$addr', '$dist', '$id')";
    	$sql = mysql_query($ins) or die('insert into assigntable error');

    	mysql_close($con);
	}
?>
