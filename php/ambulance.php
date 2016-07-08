<?php
include("MysqlSet.inc.php");

header('Content-type : text/html; charset = utf-8');

$pattern = "/\已轉送至處理單位/i";
if(isset($_POST["username"]))
{
	$username = $_POST["username"];
	$password = $_POST["password"];
	
	$firestationID = substr($username, 10);
	$sql = "SELECT * FROM firestation where firestationID = '$firecestationID'";
	$result = mysql_query($sql) or die ("search error");
	while($row = mysql_fetch_array($result))
	{
		if(preg_match($pattern, $row[11]))
		{
			echo $row[1];
			echo "</br>";
			echo $row[2];
			echo "</br>";
			echo $row[3];
			echo "</br>";
			echo $row[4];
			echo "</br>";
			echo $row[5];
			echo "</br>";
			echo $row[6];
			echo "</br>";
			echo $row[7];
			echo "</br>";
			echo $row[8];
			echo "</br>";
			echo $row[9];
			echo "</br>";
			
			$update = "UPDATE firestation SET  status = '出勤中' WHERE reportid = $row[1]";
        	$modsta = mysql_query($update) or die('update status error');
        	break;
		}
	}
	echo "ss";
	echo "</br>";
	echo "ee";
}

$pattern1 = "/\生產/i";
$pattern2 = "/\燒燙傷/i";
$pattern3 = "/\其他/i";
if(isset($_POST["id"]))
{
	$id = $_POST["id"];
	$category = $_POST["category"];

	if(preg_match($pattern2, $category))
	{
		$term_one = $_POST["term_one"];				//嚴重燒燙
		$term_two = $_POST["term_two"];				//燒傷%數
		$term_three = $_POST["term_three"];			//灼傷
	}
	else if(preg_match($pattern3, $category))
	{
		$term_one = $_POST["term_one"];				//精神異常
		$term_two = $_POST["term_two"];				//多重藥物或中毒
		$term_three = $_POST["term_three"];			//內外骨外
	}
}


?>
