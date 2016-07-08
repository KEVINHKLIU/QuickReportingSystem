<html>
<head>
    <meta charset="UTF-8">
    <link href="https://developers.google.com/maps/documentation/javascript/examples/default.css" rel="stylesheet" />
    <script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false&language=zh-TW"></script>
    <script type="text/javascript" src="http://code.jquery.com/jquery-1.9.1.js"></script>
</head>
<body> 
    <script src="http://code.jquery.com/jquery-latest.min.js" type="text/javascript"></script>
    <script type="text/javascript">
        //google api，計算兩點的導航路徑距離
        function Distance(fireST, reportLoc, id) 
        {
            var start = fireST;                   //導航的起點，fireST為消防局地址
            var end = reportLoc;                    //導航的終點，reportLoc為報案地點地址
            var request = {
                origin: start,
                destination: end,
                travelMode: google.maps.DirectionsTravelMode.DRIVING
            };
            //宣告
            var directionsService = new google.maps.DirectionsService();

            directionsService.route(request, function (response, status) 
            {
                var strTmp = "";

                if (status == google.maps.DirectionsStatus.OK) 
                {
                    var route = response.routes[0];
                 
                    for (var i = 0; i < route.legs.length; i++) 
                    {
                        var routeSegment = i + 1;
                        strTmp += route.legs[i].distance.text;
                    }
                    //取得距離(正整數，公尺)
                    var dist = parseInt(parseFloat(strTmp) * 1000).toString();

                    //傳到get_distance_police.php進行暫存
                    $.ajax({
                            async: false,
                            url: "get_distance_fireST.php",
                            dataType: 'json',
                            type: "GET", 
                            data: { dist: dist, address: fireST, id: id},
                            success: function(response) {
                              $('#div_name').html(response); 
                            }, 
                            error: function() {
                              console.log("ajax error!"); 
                            }
                            });
                }
            });
        }

    </script>
        <div id="div_name">

        <?php
            header('refresh: 3; url = "distance_fireST.php"');
            include("MysqlSet.inc.php");                        //建立與資料庫連線

            $exeflag = true;
            //status = '已轉送至處理單位'為未進行分配任務的報案資訊
            $sql = "SELECT * FROM firestation where status = '已轉送至處理單位'";
            $result = mysql_query($sql) or die('select report error');
            //取得未處理報案資訊，並改變status = '處理中'
            while($temp = mysql_fetch_array($result))
            {
                $exeflag = false;
                $reportid = $temp[1];
                $address = $temp[8];
                $longitude = $temp[9];
                $latitude = $temp[10];
                $update = "UPDATE firestation SET status = '處理中' WHERE reportid = '$reportid'";
                $modlat = mysql_query($update) or die('update status error');
                break;
            }
            if($temp[12] == '11111')
            {
                $exeflag = true;
                $update = "UPDATE firestation SET status = '處理中' WHERE reportid = '$reportid'";
                $modlat = mysql_query($update) or die('update status error');
            }

            if($exeflag)
            {
                mysql_close($con);
                die('no waiting mission');
            }
                
            //以報案地點為圓心，取半徑500公尺的範圍，搜尋消防局。
            //因google api最多只能計算十筆資料，所以範圍內消防局不得大於10間
            $lat_range = 0.005;
            $lng_range = 0.005; 
            while(true)
            { 
                $latn = $latitude * 1 + $lat_range;             //以報案地點的經緯度進行取範圍
                $lats = $latitude * 1 - $lat_range;
                $lnge = $longitude * 1 + $lng_range;
                $lngw = $longitude * 1 - $lng_range;

                //將所有消防局的地址與經緯度，依序從資料庫取出
                $fireSTloc = "SELECT * FROM firestationinfo";
                $result = mysql_query($fireSTloc) or die('connect firestationinfo error');

                $k = 0;
                $m = 0;
                while($row = mysql_fetch_array($result))
                {
                    $addr[$m] = $row[2];
                    $firelat[$m] = $row[3];
                    $firelng[$m] = $row[4];
                    $m++;
                }
                //將在範圍內的消防局第只存入circle中
                for($n = 0; $n < $m ; $n++)
                {
                    if($firelat[$n] * 1 < $latn * 1 && $firelat[$n] * 1 > $lats * 1 && $firelng[$n] * 1 < $lnge * 1 && $firelng[$n] * 1 > $lngw * 1)
                    {
                        $circle[$k] = $addr[$n];
                        $k ++;
                    }
                }
                if($k > 0 && $k < 10)               //範圍內消防局數量，至少一間且少於十間
                    break;                          //符合條件，離開篩選程序
                if($k == 0)                         //範圍內沒有消防局
                {                                   //加大範圍半徑500公尺，繼續進行篩選
                    $lat_range += 0.005;
                    $lng_range += 0.005;
                }
                if($k >= 10)                        //範圍內消防局數量大於10間
                {
                    $lat_range -= 0.01;             //縮小範圍半徑1000公尺，繼續進行篩選
                    $lng_range -= 0.01;
                }
            }
            
            //將消防局地址、報案地點地址、報案ID，傳入Distance()，進行距離運算
            for($i = 0; $i < $k; $i++)
            { 
                echo "<script>Distance('$circle[$i]', '$address', '$reportid'); </script>";
            }
            mysql_close($con);  
            
            include("MysqlSet.inc.php");
            //讀取暫存在資料庫各消防局到報案地點的距離，並做排序
            //利用while(true)作等待，等待Distanse()中的ajax執行完成
            while(true)
            {
                $sql = "SELECT * FROM assigntable_firestation where reportid = '$reportid'";
                $result = mysql_query($sql);
                $row = mysql_fetch_array($result);
                
                if($row[3] == $reportid)                //ajax至少已經完成一筆資料寫入
                {
                    sleep(1);                           //等待後續資料寫入
                    
                    $sql = "SELECT * FROM assigntable_firestation where reportid = '$reportid'";
                    $result = mysql_query($sql) or die('select assign report error');
                     
                    $num = 0;
                    //auth[]存取消防局名稱，dist[]存取距離
                    while($row = mysql_fetch_array($result))
                    {
                            $auth[$num] = $row[1];
                            $dist[$num] = $row[2];
                            $num++;
                    }
                    //進行排序，以取得最近的警察局
                    if($num > 1)
                    {
                        for($i = 0; $i < $num; $i++)
                        {
                            for($j = 1; $j < ($num - $i); $j++)
                            {
                                if($dist[$j]*1 < $dist[$j-1]*1)
                                {
                                    $templen = $dist[$j];
                                    $tempaddr = $auth[$j];
                                    $dist[$j] = $dist[$j-1];
                                    $auth[$j] = $auth[$j-1];  
                                    $dist[$j-1] =  $templen;
                                    $auth[$j-1] =  $tempaddr;
                                }
                            }
                        }
                    }
                    //進行消防局人員出勤狀況篩選
                    $full = 0;
                    for($i = 0; $i < $num; $i++)
                    {
                        $sql = "SELECT id FROM firestationinfo where pAddress = '$auth[$i]'";
                        $result = mysql_query($sql) or die('select firestationid error');
                        $row = mysql_fetch_array($result);
                        $fireSTID = $row[0];

                        $sql = "SELECT num FROM ambulanceid where id = $fireSTID";
                        $result = mysql_query($sql) or die('select number of ambulance error');
                        $row = mysql_fetch_array($result);
                        $head = $row[0];

                        $sel = "SELECT * FROM firestationonduty where id = $fireSTID";
                        $conn = mysql_query($sel) or die('select available fireST errror');
                        $temp = mysql_fetch_array($conn);
                        for($j = (2 + $head); $j < 12; $j++)
                        {
                            if($temp[$j] == 0)
                            {
                                $full = 0;
                                break;
                            }
                        }
                        if($full == 0)
                            break;
                    }
                    //完成分配消防局
                    $update = "UPDATE firestation SET  firestationID = '$fireSTID' WHERE reportid = '$reportid'";
                    $modlat = mysql_query($update) or die('assign fireST error');

                    //刪除暫存於資料庫的報案地點與各消防局距離
                    $del = "DELETE FROM assigntable_firestation where reportid = '$reportid'";
                    $sql = mysql_query($del) or die('delete error');

                    break;
                }
                else
                   echo " ";
            }
            mysql_close($con);
        ?>
</body>
</html>