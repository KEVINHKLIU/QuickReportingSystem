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
        function Distance(reportLoc, hospital, id) 
        {
            var start = reportLoc;                     //導航的起點，reportLoc為報案地點地址
            var end = hospital;                        //導航的終點，hospital為醫院地址
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
                            url: "get_distance_hospital.php",
                            dataType: 'json',
                            type: "GET", 
                            data: { dist: dist, address: hospital, id: id},
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
            header('refresh: 3; url = "distance_hospital.php"');
            include("MysqlSet.inc.php");                   //建立與資料庫連線

            $exeflag = true;
            $sql = "SELECT * FROM assigntable_hospital where `V/I` = 'V'";
            $result = mysql_query($sql) or die('select valid report error');
            while($temp = mysql_fetch_array($result))
            {
                $exeflag = false;
                $reportid = $temp[3];
                $ambID = $temp[4];
                break;
            }
            
            if($exeflag)
            {
                mysql_close($con); 
                die('no waiting mission');
            }    
            
            $n = 0;
            
            $sql = "SELECT * FROM assigntable_hospital where `V/I` = 'V'";
            $result = mysql_query($sql) or die('select valid report error');
            while($temp = mysql_fetch_array($result))
            {
                if($temp[3] == $reportid && $temp[4] == $ambID)
                {
                    $addr[$n] = $temp[1];
                    $n++;
                }
                else
                    break;
            }

            for($i = 0; $i < $n; $i++)
            {
                $sql = "SELECT latitude, longitude FROM hospitalinfo where hAddress = '$addr[$i]'";
                $result = mysql_query($sql) or die('select hospital address error');
                $row = mysql_fetch_array($result);
                $hosplat[$i] = $row[0];
                $hosplng[$i] = $row[1];
            }

            $sql = "SELECT 緯度, 經度, 地址 FROM recordt where id = $reportid";
            $result = mysql_query($sql) or die('select reportLoc address error');
            $row = mysql_fetch_array($result);
            $latitude = $row[0];
            $longitude = $row[1];
            $address = $row[2];
            //以報案地點為圓心，取半徑500公尺的範圍，搜尋醫院。
            //因google api最多只能計算十筆資料，所以範圍內醫院不得大於10間
            if($i >= 10)
            {
                $lat_range = 0.005;
                $lng_range = 0.005; 
                while(true)
                { 
                    $k = 0;
                    $latn = $latitude * 1 + $lat_range;             //以報案地點的經緯度進行取範圍
                    $lats = $latitude * 1 - $lat_range;
                    $lnge = $longitude * 1 + $lng_range;
                    $lngw = $longitude * 1 - $lng_range;

                    //將所有醫院的地址與經緯度，依序從資料庫取出
                   
                    //將在範圍內的醫院地址存入circle中
                    for($n = 0; $n < $i ; $n++)
                    {
                        if($hosplat[$n] * 1 < $latn * 1 && $hosplat[$n] * 1 > $lats * 1 && $hosplng[$n] * 1 < $lnge * 1 && $hosplng[$n] * 1 > $lngw * 1)
                        {
                            $circle[$k] = $addr[$n];
                            $k ++;
                        }
                    }
                    if($k >= 5 && $k < 10)               //範圍內醫院數量，至少五間且少於十間
                        break;                          //符合條件，離開篩選程序
                    if($k < 5)                         //範圍內沒有警察局
                    {                                   //加大範圍半徑500公尺，繼續進行篩選
                        $lat_range += 0.005;
                        $lng_range += 0.005;
                    }
                    if($k >= 10)                        //範圍內醫院數量大於10間
                    {
                        $lat_range -= 0.01;             //縮小範圍半徑1000公尺，繼續進行篩選
                        $lng_range -= 0.01;
                    }
                }
            }
            else
            {
                $k = $i;
                for($n = 0; $n < $i ; $n++)
                    $circle[$n] = $addr[$n];
            }

            $upd = "UPDATE assigntable_hospital set `V/I` = 'I' where reportid = '$reportid' && ambulanceID = '$ambID'";
            $quer = mysql_query($upd) or die('change flag error');
            
            //將醫院地址、報案地點地址、報案ID，傳入Distance()，進行距離運算
            for($i = 0; $i < $k; $i++)
            { 
                echo "<script>Distance('$address', '$circle[$i]', '$reportid'); </script>";
            }
            mysql_close($con);
        ?>
</body>
</html>