<?php
	header('Content-type : text/html; charset = utf-8');
	
	//進行帳號登入認證
	if(isset($_POST["auth"]))
	{
		include("MysqlSet.inc.php");
		$auth = $_POST["auth"];
		$username = $_POST["username"];
		$password = $_POST["password"];

		if($auth * 1 == 0)														//auth = 0為警務用
		{
			$sql = "SELECT * FROM policeid where policeofficeid = '$username'";
			$result = mysql_query($sql) or die ("connect error");
			
			$start = 2;
			$end = 12;
		}
		else if($auth * 1 == 1)													//auth = 1為消防用
		{
			$sql = "SELECT * FROM firestationid where firestationid = '$username'";
			$result = mysql_query($sql) or die ("connect error");	

			$amb = "SELECT num FROM ambulanceid where acount = '$username'";
			$conn = mysql_query($amb) or die ("connect error");
			$temp = mysql_fetch_array($conn);

			$start = 2 + $temp[0];												//消防局前N個帳號保留給救護車使用,其中N為救護車數	
			$end = 12;
		}
		else if($auth * 1 == 2)													//auth = 2為救護用
		{
			$sql = "SELECT * FROM firestationid where firestationid = '$username'";
			$result = mysql_query($sql) or die ("connect error");	

			$amb = "SELECT num FROM ambulanceid where acount = '$username'";
			$conn = mysql_query($amb) or die ("connect error");
			$temp = mysql_fetch_array($conn);	

			$start = 2;
			$end = $temp[0] + 2;												//消防局前N個帳號保留給救護車使用,其中N為救護車數
		}

		$flag = 0;
		$row = mysql_fetch_array($result);

		for($i = $start; $i < $end; $i++)
		{
			if($password == $row[$i])
			{
				echo "success";
				$flag = 1;
				break;
			}
		}

		if($row[0] == "")
		{
			$flag = 1;
			echo "username error";
		}
		if($flag == 0)
			echo "password not found";

		mysql_close($con); 
	}
?>
