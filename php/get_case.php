<?php
 	header('Content-type : text/html; charset = utf-8');

	//取得app的輸入資料
	$userid = $_POST["IMEI"];
	$time = $_POST["strTime"];
	$number = $_POST["injuries_number"];
	$unit = $_POST["inform_unit"];
	$case1 = $_POST["unit_case"];
	$case2 = $_POST["unit_case_case"];
	$photonumber = $_POST["countStr"];
	$address = $_POST["strAddress"];
	$longitude = $_POST["longitude"];
	$latitude = $_POST["latitude"];

	//接收照片
	$picture[0] = $_POST["image_name1"];
	$encoded_picture[0] = $_POST["encoded_picture1"];
	$picture[1] = $_POST["image_name2"];
	$encoded_picture[1] = $_POST["encoded_picture2"];
	$picture[2] = $_POST["image_name3"];
	$encoded_picture[2] = $_POST["encoded_picture3"];
	$picture[3] = $_POST["image_name4"];
	$encoded_picture[3] = $_POST["encoded_picture4"];
	$picture[4] = $_POST["image_name5"];
	$encoded_picture[4] = $_POST["encoded_picture5"];
	$picture[5] = $_POST["image_name6"];
	$encoded_picture[5] = $_POST["encoded_picture6"];
	$picture[6] = $_POST["image_name7"];
	$encoded_picture[6] = $_POST["encoded_picture7"];
	$picture[7] = $_POST["image_name8"];
	$encoded_picture[7] = $_POST["encoded_picture8"];
	$picture[8] = $_POST["image_name9"];
	$encoded_picture[8] = $_POST["encoded_picture9"];
	$picture[9] = $_POST["image_name10"];
	$encoded_picture[9] = $_POST["encoded_picture10"];

	//將照片進行編碼
	//存到images/裡
	for($i = 0; $i < $photonumber; $i++)
	{
		$decoded_picture[$i] = base64_decode($encoded_picture[$i]);
		$path[$i]= 'images/'.$picture[$i];
		$file[$i] = fopen($path[$i], 'wb');
		$is_written[$i] = fwrite($file[$i], $decoded_picture[$i]);
	}
	if($number*1 >= 15)
		$major = 1;
	else
		$major = 0;
	//將收取到的報案資料傳入資料庫，初始狀態為有效報案與已接收
	if(isset($_POST["injuries_number"]))
	{
		include("MysqlSet.inc.php");								//與DB建立連線
		$insrecordt = "INSERT INTO recordt
		(userid, 時間, 傷亡人數, 通報機關, 報案類別1, 報案類別2, 照片數量, 地址,
		經度, 緯度, VI, status, major)
		VALUES('$userid', '$time', '$number', '$unit', '$case1', '$case2', '$photonumber', '$address', '$longitude', '$latitude', 'V', '已接收', $major)";
		
		//執行sql
		$ins = mysql_query($insrecordt) or die('insert into recordt error');

		$sql = "SELECT LAST_INSERT_ID()";
		$ins = mysql_query($sql) or die('select last id error');
		$row = mysql_fetch_array($ins);
		$id = $row[0];	
		
		//判定是否insert成功
		if(mysql_affected_rows() >= 1)
			echo "insert successfully";
		mysql_close($con);
	}
	include("MysqlSet.inc.php");								//與DB建立連線
	$sql = "SELECT * FROM recordt where id= $id";
	$result = mysql_query($sql) or die('select recordt error');

	//存取reportid, VI, status
	$temp = mysql_fetch_array($result);
	$id = $temp[0];
	$address = $temp[8];
	$longitude = $temp[9];
	$latitude = $temp[10];
	$VI = $temp[11];
	$status = $temp[12];
		
	
	//存取照片
	for($i = 0; $i < $photonumber; $i++)
	{
		$inspic = "INSERT INTO photo (reportid, userid, photoname, photolocation) 
		VALUES('$id', '$userid', '$picture[$i]', '$encoded_picture[$i]')";
		$ins = mysql_query($inspic) or die('insert photo error');
	}
	
	//將時間(YYYY-MM-DD hh:mm:ss)轉換成秒，進行兩報案案件的時間間距比較
	$count = $id;
	$str_time = explode("-",$time);
	$year = $str_time[0] * 1;								//將時間以"-"做字串切割，將'年'與'月'取出								
	$month = $str_time[1] * 1;

	$str_day = explode(" ",$str_time[2]);					//將時間以" "做字串切割，再將第三部分以" "做切割，取出'日'使用
	$day = $str_day[0] * 1;

	$str_subday = explode(":",$str_day[1]);					//以":"做切割得到的第二部分，以":"做切割，最後的到'時'、'分'、'秒'
	$hour = $str_subday[0] * 1;
	$minute = $str_subday[1] * 1 *60;
	$second = $str_subday[2] * 1;

	//重複報案位置範圍
	//以當前報案位置將其從中心點向外取邊界值(約半徑500公尺)
	$latn = $latitude * 1 + 0.005;							
	$lats = $latitude * 1 - 0.005;
	$lnge = $longitude * 1 + 0.005;
	$lngw = $longitude * 1 - 0.005;

	//重複報案時間範圍
	//與之前已接收報案資料做比較，符合兩報案時間間隔小於800秒、已接收報案位置處於當前報案的重複範圍內、報案類型箱等
	//則將當前報案視為重複報案
	while(true)
	{
		$count --;
		//擷取已受理報案資訊											
		$sql = "SELECT 時間, 報案類別1, 報案類別2, 經度, 緯度, VI, status from recordt where id = $count";
		$result = mysql_query($sql) or die('select time error');
		$temp = mysql_fetch_array($result);
		$btime = $temp[0];
		$breporttype[0] = $temp[1];
		$breporttype[1] = $temp[2];
		$str_lon = $temp[3];
		$str_lat = $temp[4];
		$bVI = $temp[5];
		$bstatus = $temp[6];

		//切割時間轉換為秒
		$str_time = explode("-",$btime);
		$byear = $str_time[0] * 1;
		$bmonth = $str_time[1] * 1;

		$str_day = explode(" ",$str_time[2]);
		$bday = $str_day[0] * 1;

		$str_subday = explode(":",$str_day[1]);
		$bhour = $str_subday[0] * 1;
		$bminute = $str_subday[1] * 1 * 60;
		$bsecond = $str_subday[2] * 1;

		$blon = $str_lon * 1;
		$blat = $str_lat * 1;

		if($year == $byear)
		{
			if($month == $bmonth)
			{
				if($day == $bday)
				{
					if($hour == $bhour)
					{
						if(($minute + $second - $bminute - $bsecond) <= 800)
						{
							if((strcasecmp($bstatus, "已接收")) == 0)
							{
								if($blon > $lngw && $blon < $lnge && $blat > $lats && $blat < $latn)
								{
									if((strcasecmp($case1,$breporttype[0])) == 0)
									{
										if((strcasecmp($case2,$breporttype[1])) == 0)
										{
											if((strcasecmp($bVI,"V") == 0))
											{
												$update = "UPDATE recordt SET VI = 'I' WHERE id = $id";
												$upd = mysql_query($update) or die('change to Invalid error');
												$VI="I";
												break;
											}
										}
									}
								}
							}
							else
								break;
						}
						else
							break;
					}
					else
						break;
				}
				else
					break;
			}
			else
				break;
		}
		else
			break;
	}

	//判斷處理單位，以進行分配
	$pattern1 = "/\救護/i";									//處理單位為消防局之救護車					
	$pattern2 = "/\消防/i";									//處理單位為消防局之非救護車
	$pattern3 = "/\警察/i";									//處理單位為消防局之救護車
	if((strcasecmp($VI,"V") == 0))							//案件為有效報案
	{
		//將報案進行分類，insert into各單位之table
		if(preg_match($pattern1, $unit))
		{
			$count = $number;
			if($count >= 15)
			{
				$count = 15;
				$number = 15;
			}
			//有關救護之報案進度，分割成單一傷患為單一事件，故需要n筆追蹤紀錄
			while($count * 1 > 0)
			{	
				$insamb = "INSERT INTO ambulance
				(reportid, 傷亡人數, status, `P/C`) 
				VALUES('$id', '1', '已轉送至處理單位', 'C')";
				$ins = mysql_query($insamb) or die('transform to ambulance error');
				
				$sql = "INSERT INTO report_trace(reportid, 報案時間, 處理單位) VALUES ('$id', '$time', '救護車')"; //新增報案處理進度的時間(報案時間)
				$conn = mysql_query($sql) or die('build trace error');
				$count--;
			}

			//insert into ambulance, P代表此紀錄微系統接收收資料, C代表此資料為已分割為單一傷患單一事件
			// $insamb = "INSERT INTO ambulance
			// (reportid, 傷亡人數, status, `P/C`) 
			// VALUES('$id', '$number', '已轉送至處理單位', 'P')";
			// $ins = mysql_query($insamb) or die('transform to ambulance error');

			if($major == 1)
			{
				$insamb = "INSERT INTO ambulance
				(reportid, 傷亡人數, status, `P/C`, fireSTID) 
				VALUES('$id', '$number', '已轉送至處理單位', 'C', '11111')";
				$ins = mysql_query($insamb) or die('transform to ambulance error');

				$insfir = "INSERT INTO firestation
				(reportid, userid, 時間, 傷亡人數, 報案類別1, 報案類別2, 照片數量, 地址, 經度, 緯度, status, firestationID)   
				VALUES('$id', '$userid', '$time', '$number', '$case1', '$case2', '$photonumber', '$address', '$longitude', '$latitude', '已轉送至處理單位', '11111')";
				$ins = mysql_query($insfir) or die('transform to firestation error');

				$inspol = "INSERT INTO police
				(reportid, userid, 時間, 傷亡人數, 報案類別1, 報案類別2, 照片數量, 地址, 經度, 緯度, status, policestationID)
				VALUES('$id', '$userid', '$time', '$number', '$case1', '$case2', '$photonumber', '$address', '$longitude', '$latitude', '已轉送至處理單位', '11111')";
				$ins = mysql_query($inspol) or die('transform to police error');
			}
		}

		if(preg_match($pattern2, $unit))
		{
			if($major != 1)
			{	
				$sql = "INSERT INTO report_trace(reportid, 報案時間, 處理單位) VALUES ('$id', '$time', '消防')"; //新增報案處理進度的時間(報案時間)
				$conn = mysql_query($sql) or die('build trace error');
			
				//insert to firestation
				$insfir = "INSERT INTO firestation
				(reportid, userid, 時間, 傷亡人數, 報案類別1, 報案類別2, 照片數量, 地址, 經度, 緯度, status)   
				VALUES('$id', '$userid', '$time', '$number', '$case1', '$case2', '$photonumber', '$address', '$longitude', '$latitude', '已轉送至處理單位')";
				$ins = mysql_query($insfir) or die('transform to firestation error');
			}
		}

		if(preg_match($pattern3, $unit))
		{
			if($major != 1)
			{	
				$sql = "INSERT INTO report_trace(reportid, 報案時間, 處理單位) VALUES ('$id', '$time', '警察')"; //新增報案處理進度的時間(報案時間)
				$conn = mysql_query($sql) or die('build trace error');

				//insert to police
				$inspol = "INSERT INTO police
				(reportid, userid, 時間, 傷亡人數, 報案類別1, 報案類別2, 照片數量, 地址, 經度, 緯度, status)
				VALUES('$id', '$userid', '$time', '$number', '$case1', '$case2', '$photonumber', '$address', '$longitude', '$latitude', '已轉送至處理單位')";
				$ins = mysql_query($inspol) or die('transform to police error');
			}
		}
	}
	mysql_close($con);
?>

