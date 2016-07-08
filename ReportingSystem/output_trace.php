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
			$id = $_GET["id"];
			echo "<table width = '110%' align = 'center' cellpadding = '0' cellsapcing = '0'>";
			echo "<tr bgcolor = #EDEDED>
		          <td>&nbsp;&nbsp;報案id</td>
		          <td>報案時間時間</td>
		          <td>人員出勤時間</td>
		          <td>抵達現場時間</td>
		          <td>任務完成時間</td>
		          <td>抵達醫院</td> 
		          <td>受理單位</td>
		          <td>救護車</td> 
		          </tr>";
		    
		    $amb = "None";
			$sql = "SELECT * FROM report_trace where reportid = '$id'";
		  	$result = mysql_query($sql) or die('select report trace error');
		  	while($row = mysql_fetch_array($result))
		  	{
		  		$i = 3;
		  		if($row[8]!="None")
		  		{
		  			$amb = str_replace('firestation', "", $row[8]);
					$amb_info = explode("-",$amb);

					$sel = "SELECT * FROM ambulanceid where id = $amb_info[0]";
			  		$ans = mysql_query($sel) or die('select report trace error');
			  		$temp = mysql_fetch_array($ans);
			  		while(true)
			  		{
			  			if($amb_info[1] == $i-2)
			  			{
			  				$amb = $temp[$i];
			  				break;
			  			}
			  			$i++;
			  		}
			  	}
			  	else
			  		$amb = $row[8];
			  	
		  		echo "<tr bgcolor = white>
		              <td>&nbsp;&nbsp;$row[1]</td>
		              <td>$row[2]</td>
		              <td>$row[3]</td>
		              <td>$row[4]</td>
		              <td>$row[5]</td>
		              <td>$row[6]</td>
		              <td>$row[7]</td>
		              <td>$amb</td>
		              </tr>";
		  	}

		  	echo "<p align='center'> <font face='微軟正黑體''><font size='5'><a href='http://140.113.72.125/lab01230322/ReportingSystem/web_case.php'><font color='#A1FFFF' > 返回 </a></font></font></p>";
			mysql_close($con); 
		?>
		</div>
</body>
</html>