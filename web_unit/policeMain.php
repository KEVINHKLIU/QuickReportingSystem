<?php
  //檢查 cookie 中的 passed 變數是否等於 TRUE
  $passed = $_COOKIE["passed"];
  $id =  $_COOKIE["id"];
  /*  如果 cookie 中的 passed 變數不等於 TRUE
      表示尚未登入網站，將使用者導向首頁 警察單位登入系統.html	*/
  if ($passed != "TRUE")
  {
    header("location:policeLoginSystem.html");
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
        header('refresh: 0; url = "policeMain.php"');
      }
    }  

    $sql = "SELECT * from policeinfo where id = '$id'";
    $result = mysql_query($sql) or die('error');
    $row = mysql_fetch_array($result);
    echo "<table width = '80%' align = 'center' cellpadding = '0' cellsapcing = '0'>";
    echo "<tr>
          <td>$row[1]</td>
          </tr>";

    echo "<tr bgcolor = 'yellow'>
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

    $sql = "SELECT * from police where policestationID = '$id' && status != '結案' || policestationID = '11111' order by id desc";
    $result = mysql_query($sql) or die('error');
    while($row = mysql_fetch_array($result))
    {
      $id = $row[1];
      echo "<tr bgcolor = white>
            <td><a href='http://140.113.72.125/lab01230322/images/image_output.php?id=$id'>$row[1]</td>
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
    mysql_close($con); 
  }
?>
<!doctype html>
<html>
<head>
  <title>警局受理案件一覽表</title>
  <meta http-equiv = Refresh content = 8; url = policeMain.php>
  <meta charset="utf-8">
</head>
<body>
  <p align="center"><img src="policeLogIN.png"></p>
  
  <?php
    include("MysqlSet.inc.php");

    $sql = "SELECT major from recordt where major = '1' && VI = 'V'";
    $result = mysql_query($sql) or die('error');
    $row = mysql_fetch_array($result);
    if($row[0] == 1)
    {
      $cate = '1';
      echo "<p align='center'> <font face='微軟正黑體''><font size='5'><a href='http://140.113.72.125/lab01230322/web_unit/output_note.php?cate=$cate'><font color='#FF0000' > 各單位注意，目前發生重大事故</a></font></font></p>";
    }
    mysql_close($con); 
  ?>

  <p align="center">
  <a href="policeLogout.php">登出網站</a>
  </p>
</body>
</html>