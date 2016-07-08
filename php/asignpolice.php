<html>
<head>

<!--<meta http-equiv=Refresh content=10;url=asignpolice.php>-->
<meta charset="UTF-8">
<link href="https://developers.google.com/maps/documentation/javascript/examples/default.css"
        rel="stylesheet" />
<script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false&language=zh-TW"></script>
<script type="text/javascript" src="http://code.jquery.com/jquery-1.9.1.js"></script>

</head>
<body>  

<script src="http://code.jquery.com/jquery-latest.min.js" type="text/javascript"></script>
<script type="text/javascript">

    function Distance(a, b, id) 
    {
        var start = a;
        var end = b;
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
                
                $.ajax({
                url: "test.php", 
                type: "GET", 
                data: { dist: dist, address:a, id:id},
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
    header('refresh: 5; url = "asignpolice.php"');

    include("MysqlSet.inc.php");
    $sql = "SELECT * FROM police";
    $result = mysql_query($sql) or die('select police error');

    $pattern = "/\已轉送至處理單位/i";
    while($temp = mysql_fetch_array($result))
    {
        if(preg_match($pattern, $temp[11]))
        {
            $reportid = $temp[1];
            $address = $temp[8];
            $longitude = $temp[9];
            $latitude = $temp[10];
            $update = "UPDATE police SET status = '處理中' WHERE reportid = '$reportid'";
            $modlat = mysql_query($update) or die('update status error');
            break;
        }
    }

    //$reportid = 178;
    //$address = "412台灣台中市大里區樹王路3號";
    //$longitude = "120.66936226756403";
    //$latitude = "24.10810810810811";
    $lat_range = 0.005;
    $lng_range = 0.005; 
    while(true)
    {
        $latn = $latitude*1 + $lat_range;
        $lats = $latitude*1 - $lat_range;
        $lnge = $longitude*1 + $lng_range;
        $lngw = $longitude*1 - $lng_range;

        $policeloc = "select * from policeinfo";
        $result = mysql_query($policeloc) or die('connect policeinfo error');

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
            if($pollat[$n]*1 < $latn*1 && $pollat[$n]*1 > $lats*1 && $pollng[$n]*1 < $lnge*1 && $pollng[$n]*1 > $lngw*1)
            {
                $circle[$k] = $addr[$n];
                $k ++;
            }
        }
        if($k > 0 && $k < 10)
            break;
        if($k == 0)
        {
            $lat_range += 0.02;
            $lng_range += 0.02;
        }
        if($k >= 10)
        {
            $lat_range -= 0.01;
            $lng_range -= 0.01;
        }
    }
    
    for($i = 0; $i <$k; $i++)
    { 
        echo "<script>Distance('$circle[$i]', '$address', '$reportid'); </script>";
    }
    //include ("sortdist.php");
 
    ?>
</body>
</html>