<?php
	header('Content-type : text/html; charset = utf-8');
	
	$pattern = "/\處理中/i";
	if(isset($_POST["getCase"]))
	{
		include("MysqlSet.inc.php");
		$username = $_POST["username"];
		$password = $_POST["password"];
		
		$FindCaseflag = false;								//判斷是否有分配任務
		$tag_maj = "0";
		$tag_com = "0";

		//搜尋是否有發出需求之帳號所屬單位的任務
		$sql = "SELECT id FROM firestationid where firestationid = '$username'";
		$result = mysql_query($sql) or die ("search fireSTID error");
		$row = mysql_fetch_array($result);
		$fireSTID = $row[0];

		$sqlm = "SELECT * FROM firestation where firestationID = '11111'";
		$resultm = mysql_query($sqlm) or die ("search major report error");
		while($rowm = mysql_fetch_array($resultm))
		{
			if(preg_match($pattern, $rowm[11]))				//找到所屬單位被分配的任務，且該任務為未處理
			{
				$FindCaseflag = true;
				$tag_maj = "1";
				$ID = $rowm[1];
				$time = $rowm[3];
				$num = $rowm[4];
				$case = $rowm[5];
				$case_sub = $rowm[6];
				$addr = $rowm[8];
	        	break;
			}	   
		}

		$sql = "SELECT * FROM firestation where firestationID = '$fireSTID'";
		$result = mysql_query($sql) or die ("search error");
		while($row = mysql_fetch_array($result))
		{
			if(preg_match($pattern, $row[11]))
			{
				$FindCaseflag = true;						//找到分配的任務，改變flag為true
				$tag_com = "1";
				$IDc = $row[1];
				$timec = $row[3];
				$numc = $row[4];
				$casec = $row[5];
				$case_subc = $row[6];
				$addrc = $row[8];
	        	break;
			}
		}
		if($FindCaseflag == true)
		{
			echo $tag_maj.$tag_com."#";
			if($tag_maj == '1')
			{
				echo "reportID:  ".$ID."\n";
				echo "報案時間:\n".$time."\n";
				echo "人數:  ".$num."\n";
				echo "案件類別:  ".$case."\n";
				echo "案件細則  ".$case_sub."\n";
				echo "地址:\n".$addr."#";
			}
			if($tag_com == '1')
			{
				echo "reportID:  ".$IDc."\n";
				echo "報案時間:\n".$timec."\n";
				echo "人數:  ".$numc."\n";
				echo "案件類別:  ".$casec."\n";
				echo "案件細則  ".$case_subc."\n";
				echo "地址:\n".$addrc."#";
			}
		}
		if($FindCaseflag == false)
			 echo "目前無任務";

		mysql_close($con);
	}

	if(isset($_POST["attendance"]))
	{
		include("MysqlSet.inc.php");
		$username = $_POST["username"];
		$password = $_POST["password"];
		$time = $_POST["time"];
		$id = $_POST["reportID"];
		$major = $_POST["major"];
		$id = str_replace("reportID:  ", "",$id);
		
		$sql = "SELECT id FROM firestationid where firestationid = '$username'";
		$result = mysql_query($sql) or die ("search error");
		$row = mysql_fetch_array($result);
		$firID = $row[0];

		$sql = "SELECT pStation FROM firestationinfo where id = $firID";
		$result = mysql_query($sql) or die ("search error");
		$row = mysql_fetch_array($result);
		$unit = $row[0];

		if($major == "1")									//接收到重大案件
		{
			$sql = "SELECT 時間 FROM firestation where reportid = '$id'";
			$result = mysql_query($sql) or die ("search error");
			$row = mysql_fetch_array($result);
			$report_time = $row[0];

			$sql = "INSERT INTO report_trace(reportid, 報案時間, 出勤時間, 處理單位) VALUES ('$id', '$report_time', '$time', '$unit')"; //新增報案處理進度的時間(報案時間)
			$conn = mysql_query($sql) or die('build trace error');
			
			$sql = "SELECT LAST_INSERT_ID()";
			$ins = mysql_query($sql) or die('select last id error');
			$row = mysql_fetch_array($ins);
			$trace = $row[0];	
			mysql_close($con);			
		}
		else
		{
			$sql ="UPDATE report_trace SET 出勤時間 = '$time', 處理單位 = '$unit' where reportid = '$id' && 處理單位 = '消防'";  
			$conn = mysql_query($sql) or die('trace report error');

			$update = "UPDATE firestation SET status = '出勤中' WHERE reportid = '$id'";   
		    $modsta = mysql_query($update) or die('update status error');

		    $sql = "SELECT id FROM report_trace WHERE reportid = '$id' && 處理單位 = '$unit' && ambulanceID = 'None'";
			$ins = mysql_query($sql) or die('select last id error');
			$row = mysql_fetch_array($ins);
			$trace = $row[0];	
			mysql_close($con);
		}
		
		echo $trace;
		include("MysqlSet.inc.php");

	    $sql = "SELECT num FROM ambulanceid where acount = '$username'";
	    $result = mysql_query($sql) or die('select number of ambulance error');
	    $row = mysql_fetch_array($result);
	    $head = $row[0];

	    $sql = "SELECT * FROM firestationid where firestationid = '$username'";
		$result = mysql_query($sql) or die ("search error");
		while ($row = mysql_fetch_array($result))
		{
			for($i = (2 + $head); $i < 12; $i++)
			{
				if($password == $row[$i])
				{
					$acount = $i;
					break;
				}
			}
		}

		$acount = "fireSTacount".$acount;			

		$sql = "UPDATE firestationonduty SET $acount = '1' where firestationid = '$username'";
		$conn = mysql_query($sql) or die('update onduty error');
		mysql_close($con);
	}

	if(isset($_POST["arrive"]))
	{
		include("MysqlSet.inc.php");
		$time = $_POST["time"];
		$id = $_POST["reportID"];
		$major = $_POST["major"];
		$trace = $_POST["trace"];
		$id = str_replace("reportID:  ", "",$id);

		if($major == '1')
		{
			//變更案件的處理進度(抵達時間)
			$sql ="UPDATE report_trace SET 抵達時間 = '$time' where reportid = '$id' && id = $trace";		
			$conn = mysql_query($sql) or die('trace report error');
		}
		else
		{
			$sql ="UPDATE report_trace SET 抵達時間 = '$time' where reportid = '$id' && id = $trace";		
			$conn = mysql_query($sql) or die('trace report error');

			$update = "UPDATE firestation SET status = '抵達' WHERE reportid = '$id'";			
		    $modsta = mysql_query($update) or die('update status error');
		}
		mysql_close($con);
	}

	if(isset($_POST["complete"]))
	{
		include("MysqlSet.inc.php");
		$username = $_POST["username"];
		$password = $_POST["password"];
		$time = $_POST["time"];
		$id = $_POST["reportID"];
		$major = $_POST["major"];
		$id = str_replace("reportID:  ", "",$id);
		$trace = $_POST["trace"];
		$note = $_POST["note"];

		$sql = "SELECT id FROM firestationid where firestationid = '$username'";
		$result = mysql_query($sql) or die ("search error");
		$row = mysql_fetch_array($result);
		$firID = $row[0];

		$sql = "SELECT pStation FROM firestationinfo where id = $firID";
		$result = mysql_query($sql) or die ("search error");
		$row = mysql_fetch_array($result);
		$unit = $row[0];

		

		if($major == '1')
		{
			$sql = "INSERT INTO report_note (reportid, auth, assigned, unit, note, major) VALUES ('$id', '消防', '$password', '$unit', '$note', 1)";
			$result = mysql_query($sql) or die('create report note error');

			//變更案件的處理進度(完成任務)
			$sql ="UPDATE report_trace SET 任務完成時間 = '$time' where reportid = '$id' && id = $trace";
			$conn = mysql_query($sql) or die('trace report error');	
		}
		else
		{
			$sql = "INSERT INTO report_note (reportid, auth, assigned, unit, note) VALUES ('$id', '消防', '$password', '$unit', '$note')";
			$result = mysql_query($sql) or die('create report note error');

			$sql ="UPDATE report_trace SET 任務完成時間 = '$time' where reportid = '$id' && id = $trace";
			$conn = mysql_query($sql) or die('trace report error');
		}
			

		$sql = "SELECT num FROM ambulanceid where acount = '$username'";
	    $result = mysql_query($sql) or die('select number of ambulance error');
	    $row = mysql_fetch_array($result);
	    $head = $row[0];

		$sql = "SELECT * FROM firestationid where firestationid = '$username'";
		$result = mysql_query($sql) or die ("search error");
		while ($row = mysql_fetch_array($result))
		{
			for($i = (2 + $head); $i < 12; $i++)
			{
				if($password == $row[$i])
				{
					$acount = $i;
					break;
				}
			}
		}

		$acount = "fireSTacount".$acount;	

		$sql = "UPDATE firestationonduty SET $acount = '0' where firestationid = '$username'";
		$conn = mysql_query($sql) or die('update onduty error');

		if($major == '0')
		{
			$update = "UPDATE firestation SET status = '結案' WHERE reportid = '$id'";		
	    	$modsta = mysql_query($update) or die('update status error');
		}
		mysql_close($con);
	}
?>
