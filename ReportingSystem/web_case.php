<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>即時報案系統</title>
		<meta http-equiv="content-type" content="text/html; charset=utf-8" />
		<meta name="description" content="" />
		<meta name="keywords" content="" />
		<link href="http://fonts.googleapis.com/css?family=Open+Sans:400,300,600,700,800" rel="stylesheet" type="text/css" />
		<!--[if lte IE 8]><script src="js/html5shiv.js"></script><![endif]-->
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
		<script src="js/skel.min.js"></script>
		<script src="js/skel-panels.min.js"></script>
		<script src="js/init.js"></script>
		<noscript>
			<link rel="stylesheet" href="css/skel-noscript.css" />
			<link rel="stylesheet" href="css/style.css" />
			<link rel="stylesheet" href="css/style-desktop.css" />
		</noscript>
</head>
<body>
	<div class="container">
	<?php
	include("MysqlSet.inc.php");

	$sql = "SELECT * FROM recordt WHERE VI = 'V' order by id desc";
	$result = mysql_query($sql) or die('error');
	$row_num = mysql_num_rows($result);
	echo "<font face='微軟正黑體''><font size='5'><font color='#FAFAFA'>目前有 $row_num 筆報案紀錄</font></font>";
	echo "<br>";
	echo "<font face='微軟正黑體''><font size='4'><font color='#FAFAFA'>顯示最新50筆(點擊案件id即可追蹤案件目前處理狀態)</font></font>";
	echo "<table width = 110% align = 'left' cellpadding = '8' cellsapcing = '20'>
		  <tr bgcolor = #EDEDED>
		  <td>&nbsp;&nbsp;id&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
		  <td>&nbsp;&nbsp;時間</td>
		  <td>傷亡&nbsp;</td>
		  <td>&nbsp;通報機關</td>
		  <td>報案類別1</td>
		  <td>報案類別2</td>
		  <td>地址</td>
		  </tr>";
	$i=0;
	while($row = mysql_fetch_array($result))
	{
		if($i < 50)
		{
			$id = $row[0];
			echo "<tr bgcolor = white>
				  <td>&nbsp;&nbsp;<a href='http://140.113.72.125/lab01230322/ReportingSystem/output_trace.php?id=$id'>$row[0]</td>
				  <td>&nbsp;&nbsp;$row[2]&nbsp;&nbsp;&nbsp;</td>
		   		  <td>$row[3]&nbsp;&nbsp;</td>
		          <td>&nbsp;&nbsp;$row[4]&nbsp;</td>
		          <td>$row[5]&nbsp;&nbsp;&nbsp;</td>
		          <td>$row[6]&nbsp;&nbsp;</td>
		          <td>&nbsp;$row[8]&nbsp;&nbsp;</td>
		          </tr>";
	      }
	      $i++;
	}
	mysql_close($con); 
?>
	</div>
</body>
</html>
