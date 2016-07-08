<html>
<head>
    <meta charset="UTF-8">
    <link href="https://developers.google.com/maps/documentation/javascript/examples/default.css"
        rel="stylesheet" />
</head>
<body>
    <form name="myform_police" action="" method="post" onsubmit="return checkSubmit();"> 
    <input type="hidden" name="auth_police" value=""> 
    </form>
    <script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false&language=zh-TW"></script>
    <script type="text/javascript" src="http://code.jquery.com/jquery-1.9.1.js"></script>
    <?php
    include("MysqlSet.inc.php");
    
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

    ?>
    <script type="text/javascript">

    <?php
    if(!(isset($_POST["auth_police"])))
   	    echo "Main();";
    ?>

    function Main()
    {
    	Initial();

    	var i;
    	for(i = 0; i < k; i++)
		{
			Distance(i);
		}
    }

    function Initial()
    {
        checkSubmitFlg = false;
    	assign = new Array();
        paddr = <?php echo json_encode($circle);?>;
        raddr = <?php echo json_encode($address);?>;
    	m = 0;
    }

    function Distance(num) 
    {
        var start = paddr[num];
        var end = raddr;
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
                assign[m] = dist;
                m++;
                if(m == k)
                    transloc(assign);
            }
        });
    }
    function transloc(x)
    {
        document.myform_police.auth_police.value = x; 
        document.myform_police.submit(); 
    }

    function checkSubmit()
    { 
        if(checkSubmitFlg == true)
            return false; //當表單被提交過一次後checkSubmitFlg將變為true,根據判斷將無法進行提交。 

        checkSubmitFlg = true; 

        return true; 
    } 
    </script>
    <?php 
    if(isset($_POST["auth_police"]))
    { 
        echo "sss";
        $auth = $_POST["auth_police"]; 
        $count = 0;
        $num = $k;
        $n = 0;

        for($n = 0; $n < $num; $n++)
        {
            $out[$i]="";
        }

        $countid = 0;

        while($auth[$i] != "")
        {
            if($auth[$i] != ',')
            {
                $out[$count] = $out[$count].$auth[$i];
                $i++;
            }
            else
            {
                $i++;
                $count++;
            }
        }

        for($i = 0; $i < $num * 1; $i++)
        {
            for($j = 0; $j < ($num * 1 - $i); $j ++)
            {
                if($out[$j] > $out[$j+1])
                {
                    $templen = $out[$j];
                    $tempaddr = $circle[$j];
                    $out[$j] = $out[$j+1];
                    $circle[$j] = $circle[$j+1]; 
                    $out[$j+1] =  $templen;
                    $circle[$j+1] =  $tempaddr;
                }
            }
        }

        $sql = "select id from policeinfo where pAddress = '$circle[0]'";
        $result =mysql_query($sql) or die('error3');
        $row = mysql_fetch_array($result);
        $update = "UPDATE police SET  policestationID = $row[0] WHERE reportid = $reportid";
        $modlat = mysql_query($update) or die('error3');

        mysql_close($con);
        } 
       
    ?> 
</body>
</html>