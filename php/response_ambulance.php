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

		$sqlm = "SELECT reportid, status FROM ambulance where fireSTID = '11111'";
		$resultm = mysql_query($sqlm) or die ("search major report error");
		while($rowm = mysql_fetch_array($resultm))
		{
			if(preg_match($pattern, $rowm[1]))				//找到所屬單位被分配的任務，且該任務為未處理
			{
				$sql = "SELECT * FROM recordt where id = $rowm[0]";
				$result = mysql_query($sql) or die ("select major record error");
				$rowmaj = mysql_fetch_array($result);
				$FindCaseflag = true;
				$tag_maj = "1";
				$ID = $rowmaj[0];
				$time = $rowmaj[2];
				$num = $rowmaj[3];
				$case = $rowmaj[5];
				$case_sub = $rowmaj[6];
				$addr = $rowmaj[8];
	        	break;
			}	   
		}

		$sql = "SELECT id FROM firestationid where firestationid = '$username'";
		$result = mysql_query($sql) or die ("search fireSTID error");
		$row = mysql_fetch_array($result);
		$fireSTID = $row[0];

		$sql = "SELECT reportid FROM ambulance where fireSTID = '$fireSTID' && ambulanceID = '$password' && `P/C` = 'C' && status = '處理中'";
		$result = mysql_query($sql) or die ("search report error");
		$row = mysql_fetch_array($result);
		$reportid = $row[0];

		$sql = "SELECT * FROM recordt where id = '$reportid'";
		$result = mysql_query($sql) or die ("select recordt error");
		while($row = mysql_fetch_array($result))
		{
			$FindCaseflag = true;						//找到分配的任務，改變flag為true
			$tag_com = "1";
			$IDc = $row[0];
			$timec = $row[2];
			$numc = $row[3];
			$casec = $row[5];
			$case_subc = $row[6];
			$addrc = $row[8];
        	break;
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
		$result = mysql_query($sql) or die ("search fireid error");
		$row = mysql_fetch_array($result);
		$fireSTID = $row[0];

		$sql = "SELECT pStation FROM firestationinfo where id = $fireSTID";
		$result = mysql_query($sql) or die ("search add error");
		$row = mysql_fetch_array($result);
		$unit = $row[0];

		if($major == "1")									//接收到重大案件
		{
			$sql = "SELECT 時間 FROM recordt where id = '$id'";
			$result = mysql_query($sql) or die ("search record error");
			$row = mysql_fetch_array($result);
			$report_time = $row[0];

			$sql = "INSERT INTO report_trace(reportid, 報案時間, 出勤時間, 處理單位, ambulanceID) VALUES ('$id', '$report_time', '$time', '$unit', '$password')"; //新增報案處理進度的時間(報案時間)
			$conn = mysql_query($sql) or die('build trace error');
			
			$sql = "SELECT LAST_INSERT_ID()";
			$ins = mysql_query($sql) or die('select last id error');
			$row = mysql_fetch_array($ins);
			$trace = $row[0];	
			mysql_close($con);			
		}
		else
		{
			$sql ="UPDATE report_trace SET 出勤時間 = '$time', 處理單位 = '$unit' where reportid = '$id' && 處理單位 = '救護車V' && ambulanceID = '$password'";  
			$conn = mysql_query($sql) or die('trace report error');

			$update = "UPDATE ambulance SET status = '出勤中' WHERE reportid = '$id' && ambulanceID = '$password'";   
		    $modsta = mysql_query($update) or die('update status error');

		    $sql = "SELECT id FROM report_trace WHERE reportid = '$id' && 處理單位 = '$unit' && ambulanceID = '$password'";
			$ins = mysql_query($sql) or die('select last id error');
			$row = mysql_fetch_array($ins);
			$trace = $row[0];	
			mysql_close($con);
		}

		echo $trace;

		include("MysqlSet.inc.php");
	    $sql = "SELECT num FROM ambulanceid where acount = '$username'";
		$result = mysql_query($sql) or die ("search num error");
		$row = mysql_fetch_array($result);
		$num = $row[0];

	    $sql = "SELECT * FROM firestationid where firestationid = '$username'";
		$result = mysql_query($sql) or die ("search id error");
		while ($row = mysql_fetch_array($result))
		{
			for($i = 2; $i < ($num + 2); $i++)
			{
				if($password == $row[$i])
				{
					$acount = $i - 1;
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
		$password = $_POST["password"];
		$id = $_POST["reportID"];
		$major = $_POST["major"];
		$trace = $_POST["trace"];
		$id = str_replace("reportID:  ", "",$id);
		
		if($major == '1')
		{
			//變更案件的處理進度(抵達時間)
			$sql ="UPDATE report_trace SET 抵達時間 = '$time' where id = $trace";		
			$conn = mysql_query($sql) or die('trace report error');
		}
		else
		{
			//變更案件的處理進度(抵達時間)
			$sql ="UPDATE report_trace SET 抵達時間 = '$time' where id = $trace";		
			$conn = mysql_query($sql) or die('trace report error');
			//變更案件狀態為抵達現場
		    $update = "UPDATE ambulance SET status = '抵達' WHERE reportid = '$id' && ambulanceID = '$password'";			
	    	$modsta = mysql_query($update) or die('update status error');
		}	
	    mysql_close($con);
	}

	if(isset($_POST["condition"]))
	{
		include("MysqlSet.inc.php");
		$password = $_POST["password"];
		$id = $_POST["reportID"];
		$id = str_replace("reportID:  ", "",$id);
		$term_child = $_POST["term_child"];
		$term_none = $_POST["term_none"];
		$category = $_POST["category"];
	 	$term_one = $_POST["term_one"];						//限制條件一, 嚴重燒燙or精神異常
		$term_two = $_POST["term_two"];						//限制條件二, 燒傷40%or多重藥物或中毒
		$term_three = $_POST["term_three"];					//限制條件三, 內外骨外

		$pattern1 = "/\生產/i";
		$pattern2 = "/\燒傷/i";
		$pattern3 = "/\其他/i";
		
		if(preg_match($pattern2, $category))
		{
			$cate = 0;
			$type = 0;								//燒傷病床
		}
		else if(preg_match($pattern3, $category))
			$cate = 1;	
		else if(preg_match($pattern1, $category))		
		{	
			$cate = 2;	
			$type = 1;								//嬰兒床
		}
		if($cate == 1)
		{
			if($term_one == 1 && $term_two == 0 && $term_three == 0)
				$type = 2;							//精神病床
			else if($term_two == 1 || $term_three == 1)
				$type = 3;							//一般床
		}
		$sql = "SELECT * FROM hospitalinfo";
		$result = mysql_query($sql) or die('select hospitalinfo error');
		
		$hospitalID = 1;
		$listcount = 0;
			
		if($term_none*1 == 0)
		{
			while ($row = mysql_fetch_array($result))
			{
				if($cate == 0)
				{
					if($term_one*1 == 1 && $row[10] == 0)
					{
						if($term_two*1 == 1 && $row[11] == 0)
						{
							$hospitallist[$listcount] = $hospitalID;
							$listcount++;
						}
						else if($term_two*1 == 0)
						{
							$hospitallist[$listcount] = $hospitalID;
							$listcount++;
						}
					}
					else if($term_one*1 == 0)
					{
						if($term_two*1 == 1 && $row[11] == 0)
						{
							$hospitallist[$listcount] = $hospitalID;
							$listcount++;
						}
						else if($term_two*1 == 0)
						{
							$hospitallist[$listcount] = $hospitalID;
							$listcount++;
						}
					}
				}
				else if ($cate == 1)
				{
					if($term_two*1 == 1 && $row[12] == 0)
					{
						if($term_one*1 == 1 && $row[13] == 0)
						{
							if($term_three*1 == 1 && $row[14])
							{
								$hospitallist[$listcount] = $hospitalID;
								$listcount++;
							}
							else if($term_three*1 == 0)
							{
								$hospitallist[$listcount] = $hospitalID;
								$listcount++;	
							}
						}
						else if($term_one*1 == 0)
						{
							if($term_three*1 == 1 && $row[14])
							{
								$hospitallist[$listcount] = $hospitalID;
								$listcount++;
							}
							else if($term_three*1 == 0)
							{
								$hospitallist[$listcount] = $hospitalID;
								$listcount++;	
							}
						}
					}
					else if($term_two*1 == 0)
					{
						if($term_one*1 == 1 && $row[13] == 0)
						{
							if($term_three*1 == 1 && $row[14])
							{
								$hospitallist[$listcount] = $hospitalID;
								$listcount++;
							}
							else if($term_three*1 == 0)
							{
								$hospitallist[$listcount] = $hospitalID;
								$listcount++;	
							}
						}
						else if($term_one*1 == 0)
						{
							if($term_three*1 == 1 && $row[14])
							{
								$hospitallist[$listcount] = $hospitalID;
								$listcount++;
							}
							else if($term_three*1 == 0)
							{
								$hospitallist[$listcount] = $hospitalID;
								$listcount++;
							}
						}
					}
				}
				else if ($cate == 2)
				{													
					if($row[15] == 0)
					{
						$hospitallist[$listcount] = $hospitalID;
						$listcount++;
					}
				}
				$hospitalID++;
			}
		}
		if($term_none*1 == 1)
		{
			$type = 3;
			while ($row = mysql_fetch_array($result))
			{
				$hospitallist[$listcount] = $hospitalID;
				$listcount++;
				$hospitalID++;
			}
		}
		
		for($i = 0; $i < ($hospitalID*1 - 1); $i++)
		{
			$sql = "SELECT hAddress from hospitalinfo where id = $hospitallist[$i]";
			$result = mysql_query($sql) or die('select hospital address error');
			$row = mysql_fetch_array($result);

			if($cate == 0)
			{
				$ins = "INSERT INTO assigntable_hospital (address, reportid, ambulanceid, child, type, burn, burn_over_40, `V/I`) values ('$row[0]', $id, '$password', $term_child, $type, $term_one, $term_two, 'V')";
				$add = mysql_query($ins) or die('insert hospital address error');
			}
			else if($cate == 1)
			{
				$ins = "INSERT INTO assigntable_hospital (address, reportid, ambulanceid, child, type, psycho, poison, IM, `V/I`) values ('$row[0]', $id, '$password', $term_child, $type, $term_one, $term_two, $term_three, 'V')";
				$add = mysql_query($ins) or die('insert hospital address error');
			}
			else if($cate == 2)
			{
				$ins = "INSERT INTO assigntable_hospital (address, reportid, ambulanceid, child, type, OBS, `V/I`) values ('$row[0]', $id, '$password', $term_child, $type, 1, 'V')";
				$add = mysql_query($ins) or die('insert hospital address error');
			}
		}
		mysql_close($con);
	}
	if(isset($_POST["arriveHos"]))
	{
		include("MysqlSet.inc.php");
		$username = $_POST["username"];
		$password = $_POST["password"];
		$time = $_POST["time"];
		$id = $_POST["reportID"];
		$major = $_POST["major"];
		$trace = $_POST["trace"];
		$id = str_replace("reportID:  ", "",$id);

		if($major == '1')
		{
			//變更案件的處理進度(抵達時間)
			$sql ="UPDATE report_trace SET 抵達醫院時間 = '$time' where id = $trace";		
			$conn = mysql_query($sql) or die('trace report error');
		}
		else
		{
			//變更案件的處理進度(抵達時間)
			$sql ="UPDATE report_trace SET 抵達醫院時間 = '$time' where id = $trace";		
			$conn = mysql_query($sql) or die('trace report error');
			//變更案件狀態為抵達現場
		    $update = "UPDATE ambulance SET status = '抵達醫院' WHERE reportid = '$id' && ambulanceID = '$password'";			
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
		$trace = $_POST["trace"];
		$id = str_replace("reportID:  ", "",$id);
		$note = $_POST["note"];

		$sql = "SELECT id FROM firestationid where firestationid = '$username'";
		$result = mysql_query($sql) or die ("search fireST id error");
		$row = mysql_fetch_array($result);
		$fireSTID = $row[0];

		$sql = "SELECT pStation FROM firestationinfo where id = $fireSTID";
		$result = mysql_query($sql) or die ("search addr error");
		$row = mysql_fetch_array($result);
		$unit = $row[0];

		if($major == '1')
		{
			//建立任務備註
			$sql = "INSERT INTO report_note (reportid, auth, assigned, unit, note, major) VALUES ('$id', '救護', '$password', '$unit', '$note', 1)";
			$result = mysql_query($sql) or die('create report note error');	

			//變更案件的處理進度(完成任務)
			$sql ="UPDATE report_trace SET 任務完成時間 = '$time' where reportid = '$id' && id = $trace";
			$conn = mysql_query($sql) or die('trace report error');	
		}
		else if($major == '2')
		{
			//建立任務備註
			$sql = "INSERT INTO report_note (reportid, auth, assigned, unit, note, major) VALUES ('$id', '救護', '$password', '$unit', '$note', 1)";
			$result = mysql_query($sql) or die('create report note error');	

			//變更案件的處理進度(完成任務)
			$sql ="UPDATE report_trace SET 任務完成時間 = '$time' where reportid = '$id' && id = $trace";
			$conn = mysql_query($sql) or die('trace report error');	

			$update = "UPDATE ambulance SET status = '結案' WHERE reportid = '$id' && ambulanceID = '$password'";		
	    	$modsta = mysql_query($update) or die('update status error');
		}
		else
		{
			$sql = "INSERT INTO report_note (reportid, auth, assigned, unit, note) VALUES ('$id', '救護', '$password', '$unit', '$note')";
			$result = mysql_query($sql) or die('create report note error');

			//變更案件的處理進度(完成任務)
			$sql ="UPDATE report_trace SET 任務完成時間 = '$time' where reportid = '$id' && id = $trace";
			$conn = mysql_query($sql) or die('trace report error');	

			$update = "UPDATE ambulance SET status = '結案' WHERE reportid = '$id' && ambulanceID = '$password'";		
	    	$modsta = mysql_query($update) or die('update status error');
		}	

		$sql = "SELECT num FROM ambulanceid where acount = '$username'";
		$result = mysql_query($sql) or die ("search number of ambulance error");
		$row = mysql_fetch_array($result);
		$amb_num = $row[0];

		$sql = "SELECT * FROM firestationid where firestationid = '$username'";
		$result = mysql_query($sql) or die ("search error");
		while ($row = mysql_fetch_array($result))
		{
			for($i = 2; $i < (2 + $amb_num); $i++)
			{
				if($password == $row[$i])
				{
					$acount = $i - 1;
					break;
				}
			}
		}

		$acount = "fireSTacount".$acount;	

		$sql = "UPDATE firestationonduty SET $acount = '0' where firestationid = '$username'";
		$conn = mysql_query($sql) or die('update onduty error');

		
	    mysql_close($con);
	}
?>