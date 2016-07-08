<?php 
	header('Content-type : text/html; charset = utf-8');
	
	$pattern = "/\處理中/i";
	if(isset($_POST["getCase"]))							//警務APP發出接收案件的要求
	{
		include("MysqlSet.inc.php");
		$username = $_POST["username"];
		$password = $_POST["password"];
		
		$FindCaseflag = false;								//判斷是否有分配任務
		$tag_maj = "0";
		$tag_com = "0";

		//搜尋是否有發出需求之帳號所屬單位的任務
		$sql = "SELECT id FROM policeid where policeofficeid = '$username'";
		$result = mysql_query($sql) or die ("search police id error");
		$row = mysql_fetch_array($result);
		$policeSTID = $row[0];								//發出需求所屬單位的編號

		$sqlm = "SELECT * FROM police where policestationID = '11111'";
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

		$sql = "SELECT * FROM police where policestationID = '$policeSTID'";
		$result = mysql_query($sql) or die ("search report error");										
		while($row = mysql_fetch_array($result))
		{
			if(preg_match($pattern, $row[11]))				//找到所屬單位被分配的任務，且該任務為未處理
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
		if($FindCaseflag == false)							//未找到分配至該帳號的任務
			 echo "目前無任務";

		mysql_close($con);
	}

	if(isset($_POST["attendance"]))							//接收到警務APP發出出勤的訊息
	{
		include("MysqlSet.inc.php");
		$username = $_POST["username"];
		$password = $_POST["password"];
		$time = $_POST["time"];
		$id = $_POST["reportID"];
		$major = $_POST["major"];
		$id = str_replace("reportID:  ", "",$id);
		
		$sql = "SELECT id FROM policeid where policeofficeid = '$username'";
		$result = mysql_query($sql) or die ("search policeID error");
		$row = mysql_fetch_array($result);
		$polID = $row[0];

		$sql = "SELECT pStation FROM policeinfo where id = $polID";
		$result = mysql_query($sql) or die ("search adddress error");
		$row = mysql_fetch_array($result);
		$unit = $row[0];


		if($major == "1")									//接收到重大案件
		{
			$sql = "SELECT 時間 FROM police where reportid = '$id'";
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
			//變更案件的處理進度(出勤時間)
			$sql ="UPDATE report_trace SET 出勤時間 = '$time', 處理單位 = '$unit' where reportid = '$id' && 處理單位 = '警察'";  
			$conn = mysql_query($sql) or die('trace report error');
			//變更案件狀態為出勤中
			$update = "UPDATE police SET status = '出勤中' WHERE reportid = '$id'";   
		    $modsta = mysql_query($update) or die('update status error');

		    $sql = "SELECT id FROM report_trace WHERE reportid = '$id' && 處理單位 = '$unit'";
			$ins = mysql_query($sql) or die('select last id error');
			$row = mysql_fetch_array($ins);
			$trace = $row[0];	
			mysql_close($con);		
		}

		echo $trace;
		include("MysqlSet.inc.php");

		//變更出勤人員的工作狀態
	    $sql = "SELECT * FROM policeid where policeofficeid = '$username'";
		$result = mysql_query($sql) or die ("search error");
		$row = mysql_fetch_array($result);
		for($i = 2; $i < 12; $i++)
		{
			if($password == $row[$i])
			{
				$acount = $i;
				break;
			}
		}

		$acount = "policeacount".$acount;			

		$sql = "UPDATE policeonduty SET $acount = '1' where policeofficeid = '$username'";
		$conn = mysql_query($sql) or die('update onduty error');
		mysql_close($con);
	}

	if(isset($_POST["arrive"]))								//接收警務APP抵達現場之訊息
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
			//變更案件的處理進度(抵達時間)
			$sql ="UPDATE report_trace SET 抵達時間 = '$time' where reportid = '$id' && id = $trace";		
			$conn = mysql_query($sql) or die('trace report error');
			//變更案件狀態為抵達現場
			$update = "UPDATE police SET status = '抵達' WHERE reportid = '$id'";			
		    $modsta = mysql_query($update) or die('update status error');
		}	
		mysql_close($con);
	}

	if(isset($_POST["complete"]))							//接收警務APP完成任務之訊息
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
		
		$sql = "SELECT id FROM policeid where policeofficeid = '$username'";
		$result = mysql_query($sql) or die ("search error");
		$row = mysql_fetch_array($result);
		$polID = $row[0];

		$sql = "SELECT pStation FROM policeinfo where id = $polID";
		$result = mysql_query($sql) or die ("search error");
		$row = mysql_fetch_array($result);
		$unit = $row[0];
		
		if($major == '1')
		{
			//建立任務備註
			$sql = "INSERT INTO report_note (reportid, auth, assigned, unit, note, major) VALUES ('$id', '警務', '$password', '$unit', '$note', 1)";
			$result = mysql_query($sql) or die('create report note error');	

			//變更案件的處理進度(完成任務)
			$sql ="UPDATE report_trace SET 任務完成時間 = '$time' where reportid = '$id' && id = $trace";
			$conn = mysql_query($sql) or die('trace report error');	
		}
		else
		{
			$sql = "INSERT INTO report_note (reportid, auth, assigned, unit, note) VALUES ('$id', '警務', '$password', '$unit', '$note')";
			$result = mysql_query($sql) or die('create report note error');

			//變更案件的處理進度(完成任務)
			$sql ="UPDATE report_trace SET 任務完成時間 = '$time' where reportid = '$id' && id = $trace";
			$conn = mysql_query($sql) or die('trace report error');	
		}
		
		//變更出勤人員的工作狀態
		$sql = "SELECT * FROM policeid where policeofficeid = '$username'";
		$result = mysql_query($sql) or die ("search error");
		$row = mysql_fetch_array($result);
		for($i = 2; $i < 12; $i++)
		{
			if($password == $row[$i])
			{
				$acount = $i;
				break;
			}
		}

		$acount = "policeacount".$acount;	

		$sql = "UPDATE policeonduty SET $acount = '0' where policeofficeid = '$username'";
		$conn = mysql_query($sql) or die('update onduty error');

		if($major == '0')
		{
			//變更案件狀態為結案
			$update = "UPDATE police SET status = '結案' WHERE reportid = '$id'";		
		    $modsta = mysql_query($update) or die('update status error');
		}
		mysql_close($con);
	}
?>
