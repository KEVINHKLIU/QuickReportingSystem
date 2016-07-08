<?php
include("MysqlSet.inc.php");

//header('Content-type : text/html; charset = utf-8');

$sql = "SELECT * FROM policeinfo";
$result = mysql_query($sql) or die('select error');

$i = 0;
while($row = mysql_fetch_array($result))
{
    $address[$i]=$row[2];
    echo $address[$i];
    echo "</br>";
    $i++;
}

?>
    <script type="text/javascript" src="http://maps.googleapis.com/maps/api/js?sensor=false&libraries=geometry"></script> 
    <script async defer
        src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDV5nEkXBMfkm57mVLzw7blZxIi3jBLgxU&signed_in=true&callback=initMap"></script>
<script type="text/javascript">
    
    //var count = <?php echo $i;?>;
    //var addr =  <?php echo json_encode($address);?>;
    var addr = "臺中市太平區長龍路二段11巷2號";
    
    addressToLatLng(addr) ;
    //var count = <?php echo $i;?>;
    //var i = 0;
        function addressToLatLng(addr) 
        {
            
            var geocoder = new google.maps.Geocoder();

            geocoder.geocode({

                address: addr

            }, 
            function (results, status)
            {
                if (status == google.maps.GeocoderStatus.OK)
                {
                    alert(results[0].geometry.location.lat() + "," + results[0].geometry.location.lng());
                } 
                else 
                {
                    //查無經緯度
                }

            });
        }
</script>

