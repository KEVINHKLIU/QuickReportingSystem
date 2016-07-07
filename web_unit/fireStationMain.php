<?php
  //檢查 cookie 中的 passed 變數是否等於 TRUE
  $passed = $_COOKIE["passed"];
	$id =  $_COOKIE["id"];
  /*  如果 cookie 中的 passed 變數不等於 TRUE
      表示尚未登入網站，將使用者導向首頁 警察單位登入系統.html	*/
  if ($passed != "TRUE")
  {
    header("location:firestationLoginSystem.html");
    exit();
  }
  else
  {
    include("MysqlSet.inc.php");

    if(isset($_GET["complete"]))
    {
      $complete = $_GET["complete"];
      if($complete == '1')
      {
        $upd = "UPDATE recordt SET major = 0 where major = 1 && VI = 'V'";
        $mod = mysql_query($upd) or die('change major record error');

        $upd = "UPDATE police SET policestationID = 0 where policestationID = 11111";
        $mod = mysql_query($upd) or die('change police major record error');

        $upd = "UPDATE firestation SET firestationID = 0 where firestationID = 11111";
        $mod = mysql_query($upd) or die('change fireST major record error');

        $upd = "UPDATE  ambulance SET fireSTID = 0 where fireSTID = 11111";
        $mod = mysql_query($upd) or die('change fireST major record error');

        $upd = "UPDATE report_note SET major = 0 where major = 1";
        $mod = mysql_query($upd) or die('change report note major record error');
        header('refresh: 0; url = "firestationMain.php"');
      }
    }  

    $sql = "SELECT * from firestationinfo where id = '$id'";
    $result = mysql_query($sql) or die('error');
    $row = mysql_fetch_array($result);
    echo "<table width = '80%' align = 'center' cellpadding = '0' cellsapcing = '0'>";
    echo "<tr>
          <td>$row[1]</td>
          </tr>";

    echo "<tr bgcolor = 'yellow'>
          <td>類別</td>
      		<td>報案id</td>
      		<td>報案人id</td>
      		<td>時間</td>
      		<td>傷亡人數</td>
      		<td>報案類別</td>
      		<td>報案細則</td>
      		<td>照片數量</td> 
      		<td>地址</td>
      		<td>狀態</td> 
      		</tr>";

    $sql = "SELECT * from firestation where firestationID = '$id' && status != '結案' || firestationID = '11111' order by id desc";
    $result = mysql_query($sql) or die('error');
    while($row = mysql_fetch_array($result))
    {
      $reportid = $row[1];
      echo "<tr bgcolor = white>
            <td>消防</td>
            <td><a href='http://140.113.72.125/lab01230322/images/image_output.php?id=$reportid'>$row[1]</td>
            <td>$row[2]</td>
            <td>$row[3]</td>
            <td>$row[4]</td>
            <td>$row[5]</td>
            <td>$row[6]</td>
            <td>$row[7]</td>
            <td>$row[8]</td>
            <td>$row[11]</td>
            </tr>";
    }

    echo "<tr bgcolor = 'yellow'>
          <td>類別</td>
          <td>報案id</td>
          <td>報案人id</td>
          <td>時間</td>
          <td>傷亡人數</td>
          <td>報案類別</td>
          <td>報案細則</td>
          <td>照片數量</td> 
          <td>地址</td>
          <td>狀態</td> 
          </tr>";

    $i = 0;
    $sql = "SELECT reportid, status from ambulance where (fireSTID = '$id'  || fireSTID = '11111') && status != '結案'order by id desc";
    $result = mysql_query($sql) or die('error');
    while($row = mysql_fetch_array($result))
    {
      $report[$i] = $row[0];
      $status[$i] = $row[1];
      $i++;
    }
  
    for($n = 0; $n < $i; $n++)
    {
      $sel = "SELECT * from recordt where id = '$report[$n]'";
      $quer = mysql_query($sel) or die('error');
      $temp = mysql_fetch_array($quer) or die ('select recordt error');
      $reportid = $temp[0];
      echo "<tr bgcolor = white>
            <td>救護</td>
            <td><a href='http://140.113.72.125/lab01230322/images/image_output.php?id=$reportid'>$temp[0]</td>
            <td>$temp[1]</td>
            <td>$temp[2]</td>
            <td>$temp[3]</td>
            <td>$temp[5]</td>
            <td>$temp[6]</td>
            <td>$temp[7]</td>
            <td>$temp[8]</td>
            <td>$status[$n]</td>
            </tr>";
    }
    mysql_close($con); 
  }

?>
<!doctype html>
<html>
<head>
  <title>消防局受理案件一覽表</title>
  <meta http-equiv = Refresh content = 8; url = firestationMain.php>
  <meta charset="utf-8">
</head>
<body>
<p align = "center"><img src="fireStationLogIN.png"></p>
  <?php
    include("MysqlSet.inc.php");
    $sql = "SELECT major from recordt where major = '1' && VI = 'V'";
    $result = mysql_query($sql) or die('error');
    $row = mysql_fetch_array($result);
    if($row[0] == 1)
    {
      $cate = '0';
      echo "<p align='center'> <font face='微軟正黑體''><font size='5'><a href='http://140.113.72.125/lab01230322/web_unit/output_note.php?cate=$cate'><font color='#FF0000' > 各單位注意，目前發生重大事故</a></font></font></p>";
    }
    mysql_close($con); 
  ?>

<p align = "center">
<a href = "fireStationLogout.php">登出網站</a>
</p>
</body>
</html>