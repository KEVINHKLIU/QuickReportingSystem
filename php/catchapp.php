<?php 
include("MysqlSet.inc.php");

header('Content-type : text/html; charset = utf-8');
//if(isset($_POST["IMEI"]))
//{
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

	//將收取到的報案資料傳入資料庫，初始狀態為有效報案與已接收
	if($_POST["injuries_number"])
	{
		$insrecordt = "INSERT INTO recordt
		(userid, 時間, 傷亡人數, 通報機關, 報案類別1, 報案類別2, 照片數量, 地址,
		經度, 緯度, VI, status) 
		VALUES('$userid', '$time', '$number', '$unit', '$case1', '$case2', '$photonumber', '$address', '$longitude', '$latitude', 'V', '已接收')";
		
		//執行sql
		$ins = mysql_query($insrecordt) or die('insert into recordt error');

		//判定是否insert成功
		if(mysql_affected_rows() >= 1)
			echo "insert successfully";
	}

	$sql = "SELECT * FROM recordt";
	$result = mysql_query($sql) or die('select recordt error');
	$row = mysql_num_rows($result);

	$i = 1;
	//存取reportid, VI, status
	while($temp = mysql_fetch_array($result))
	{
		if($i == $row)
		{
			$id = $temp[0];
			//$time = $temp[2];
			//$unit = $temp[4];
			$address = $temp[8];
			$longitude = $temp[9];
			$latitude = $temp[10];
			$VI = $temp[11];
			$status = $temp[12];
		}
		$i++;
	}

	//存取照片
	for($i = 0; $i < $photonumber; $i++)
	{
		$inspic = "INSERT INTO photo (reportid, userid, photoname, photolocation) 
		VALUES('$id', '$userid', '$picture[$i]', '$encoded_picture[$i]')";
		$ins = mysql_query($inspic) or die('insert photo error');
	}
	
	$count = $id;
	$str_time = explode("-",$time);
	$year = $str_time[0] * 1;
	$month = $str_time[1] * 1;

	$str_day = explode(" ",$str_time[2]);
	$day = $str_day[0] * 1;

	$str_subday = explode(":",$str_day[1]);
	$hour = $str_subday[0] * 1;
	$minute = $str_subday[1] * 1 *60;
	$second = $str_subday[2] * 1;

	$latn = $latitude*1 + 0.005;
	$lats = $latitude*1 - 0.005;
	$lnge = $longitude*1 + 0.005;
	$lngw = $longitude*1 - 0.005;

	while(true)
	{
		$count --;

		$sql = "SELECT 時間 from recordt where id = $count";
		$result = mysql_query($sql) or die('select time error');
		$btime = mysql_fetch_array($result);

		$sql = "SELECT 報案類別1 from recordt where id = $count";
		$result = mysql_query($sql) or die('select reporttype1 error');
		$temp = mysql_fetch_array($result);
		$breporttype[0] = $temp[0];

		$sql = "SELECT 報案類別2 from recordt where id = $count";
		$result = mysql_query($sql) or die('select reporttype2 error');
		$temp = mysql_fetch_array($result);
		$breporttype[1] = $temp[0];

		$sql = "SELECT 經度 from recordt where id = $count";
		$result = mysql_query($sql) or die('select longitude error');
		$str_lon = mysql_fetch_array($result);

		$sql = "SELECT 緯度 from recordt where id = $count";
		$result = mysql_query($sql) or die('select latitude error');
		$str_lat = mysql_fetch_array($result);

		$sql = "SELECT VI from recordt where id = $count";
		$result = mysql_query($sql) or die('select VI error');
		$bVI = mysql_fetch_array($result);

		$sql = "SELECT status from recordt where id = $count";
		$result = mysql_query($sql) or die('select status error');
		$bstatus = mysql_fetch_array($result);

		$str_time = explode("-",$btime[0]);
		$byear = $str_time[0] * 1;
		$bmonth = $str_time[1] * 1;

		$str_day = explode(" ",$str_time[2]);
		$bday = $str_day[0] * 1;

		$str_subday = explode(":",$str_day[1]);
		$bhour = $str_subday[0] * 1;
		$bminute = $str_subday[1] * 1 * 60;
		$bsecond = $str_subday[2] * 1;

		$blon = $str_lon[0] * 1;
		$blat = $str_lat[0] * 1;

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
							if((strcasecmp($bstatus[0], "已接收")) == 0)
							{
								if($blon > $lngw && $blon < $lnge && $blat > $lats && $blat < $latn)
								{
									if((strcasecmp($case1,$breporttype[0])) == 0)
									{
										if((strcasecmp($case2,$breporttype[1])) == 0)
										{
											if((strcasecmp($bVI[0],"V") == 0))
											{
												$update = "UPDATE recordt SET VI = 'I' WHERE id = $id";
												$upd = mysql_query($update) or die('change to Invalid error');
												$VI="I";
												//$status="已接收";
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

	$pattern1 = "/\救護/i";
	$pattern2 = "/\消防/i";
	$pattern3 = "/\警察/i";
	if((strcasecmp($VI,"V") == 0))
	{
		if(preg_match($pattern1, $unit))
		{
			if(preg_match($pattern3, $unit))
			{
				$inspol = "INSERT INTO police
				(reportid, userid, 時間, 傷亡人數, 報案類別1, 報案類別2, 照片數量, 地址, 經度, 緯度, status) 
				VALUES('$id', '$userid', '$time', '$number', '$case1', '$case2', '$photonumber', '$address', '$longitude', '$latitude', '已轉送至處理單位')";
				$ins = mysql_query($inspol) or die('transform to police error');

				include("asignpolice.php");
			}
			//insert to firestation
			$insfir = "INSERT INTO firestation
			(reportid, userid, 時間, 傷亡人數, 報案類別1, 報案類別2, 照片數量, 地址, 經度, 緯度, status) 
			VALUES('$id', '$userid', '$time', '$number', '$case1', '$case2', '$photonumber', '$address', '$longitude', '$latitude', '已轉送至處理單位')";
			$ins = mysql_query($insfir) or die('transform to firestation error');

			include("assignfireST.php");
		}
		else if(preg_match($pattern2, $unit))
		{
			if(preg_match($pattern3, $unit))
			{
				//insert to police
				$inspol = "INSERT INTO police
				(reportid, userid, 時間, 傷亡人數, 報案類別1, 報案類別2, 照片數量, 地址, 經度, 緯度, status) 
				VALUES('$id', '$userid', '$time', '$number', '$case1', '$case2', '$photonumber', '$address', '$longitude', '$latitude', '已轉送至處理單位')";
				$ins = mysql_query($inspol) or die('transform to police error');

				include("asignpolice.php");
			}
			//insert to firestation
			$insfir = "INSERT INTO firestation
			(reportid, userid, 時間, 傷亡人數, 報案類別1, 報案類別2, 照片數量, 地址, 經度, 緯度, status)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        , userid, 時間, 傷亡人數, 報案類別1, 報案類別2, 照片數量, 地址, 經度, 緯度) 
			VALUES('$id', '$userid', '$time', '$number', '$case1', '$case2', '$photonumber', '$address', '$longitude', '$latitude', '已轉送至處理單位')";
			$ins = mysql_query($insfir) or die('transform to police error');

			include("assignfireST.php");
		}
		else if(preg_match($pattern3, $unit))
		{
			//insert to police
			$inspol = "INSERT INTO police
			(reportid, userid, 時間, 傷亡人數, 報案類別1, 報案類別2, 照片數量, 地址, 經度, 緯度, status)
			VALUES('$id', '$userid', '$time', '$number', '$case1', '$case2', '$photonumber', '$address', '$longitude', '$latitude', '已轉送至處理單位')";
			$ins = mysql_query($inspol) or die('transform to police error');
			mysql_close($con);
			include("asignpolice.php");
		}
	}
//}
/*
$policeloc = "select * from policeinfo";
$result = mysql_query($policeloc) or die('connect policeinfo error');

$latn = $latitude*1 + 0.05;
$lats = $latitude*1 - 0.05;
$lnge = $longitude*1 + 0.005;
$lngw = $longitude*1 - 0.005;

$k = 0;
$m = 0;

while($row = mysql_fetch_array($result))
{
	$addr[$m] = $row[2];
	$pollat[$m] = $row[3];
	$pollng[$m] = $row[4];
	$m++;
}

for($n = 0; $n < $m ; $n++)
{
	if($pollat[$n]<$latn && $pollat[$n]>$lats && $pollng[$n] <$lnge && $pollng[$n] > $lngw)
	{
		$circle[$k] = $addr[$n];
		$k ++;
	}
}
*/
?>
