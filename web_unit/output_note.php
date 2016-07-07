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

		// $passed = $_COOKIE["passed"];
	 	// $id =  $_COOKIE["id"];

	  	$cate = $_GET["cate"];
	  	// setcookie("id", $id);
	  	// setcookie("passed", "TRUE");
	  	//if($cate == 1)
	  	 //	header('refresh: 3; url = "output_note.php?cate=1"');
	  	//else
	  	 //	header('refresh: 3; url = "output_note.php?cate=2"');
	  	 
	  	
		echo "<table width = '80%' align = 'center' cellpadding = '0' cellsapcing = '0'>";
		echo "<tr bgcolor = #EDEDED>
		      <td style = 'width:90px'>&nbsp;&nbsp;類別</td>
	          <td style = 'width:90px'>受理單位</td>
	          <td style = 'width:200px'>紀錄</td>
	          </tr>";

		$sql = "SELECT * FROM report_note where major = 1";
		$result = mysql_query($sql) or die('select report note error');
		while($row = mysql_fetch_array($result))
		{
			if((strcasecmp($row[4],"現場情形") != 0))
			{
				echo "<tr bgcolor = white>
		              <td>&nbsp;&nbsp;$row[2]</td>
		              <td>$row[4]</td>
		              <td style='height:150px'>$row[5]</td>
		              </tr>";
			}	
		}
		$complete = '1';
		echo "<p align='center'> <font face='微軟正黑體''><font size='5'><font color='#FF0000' > 重大事件已完全排除</a></font></font></p>";
		if($cate == '1')
			echo "<p align='center'> <font face='微軟正黑體''><font size='5'><a href='http://140.113.72.125/lab01230322/web_unit/policeMain.php?complete=$complete'><font color='#FF0000' > 點擊結束</a></font></font></p>";
		else
			echo "<p align='center'> <font face='微軟正黑體''><font size='5'><a href='http://140.113.72.125/lab01230322/web_unit/fireStationMain.php?complete=$complete'><font color='#FF0000' > 點擊結束</a></font></font></p>";

		mysql_close($con); 
	?>
		</div>
</body>
</html>