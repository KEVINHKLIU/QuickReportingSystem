<!DOCTYPE html>
<html>
  <head>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no">
    <meta charset="utf-8">
    <title>assign</title>
    <style>
      html, body 
      {
        height: 100%;
        margin: 0;
        padding: 0;
      }
      #map 
      {
        height: 100%;
      }
    </style>
  </head>
  <body>
    <div id="map"></div>
   <?php
    include("MysqlSet.inc.php");
    
    $sql = "SELECT * FROM firestationinfo";
    $result = mysql_query($sql) or die('select error');

    $i=0;
    while($row = mysql_fetch_array($result))
    {
      $address[$i] = $row[2];
      $i++;
    }
    
    ?>
    <script type="text/javascript" src="http://maps.googleapis.com/maps/api/js?sensor=false&libraries=geometry"></script> 
    <script>
  	var addr = <?php echo json_encode($address);?>;
  	var count = <?php echo $i;?>;
 	function init()
 	{
 		m =0;
	    i = 0;
	 	a = new Array();
	 }

	function initMap() 
	{
		init();
	  var map = new google.maps.Map(document.getElementById('map'), 
	  {
	    zoom: 17,
	    center: {lat: 24.1247459, lng:  120.6888125}
	  }
	  );

	  //setMarkers(map);
	 for(i = 40; i < 50; i++)
	 {
	 	addressToLatLng(addr[i]);
	 	//alert(i);
	 	//alert(addr[i]);
	 }
	 
	
	}

	function setMarkers(map) 
	{ 
	  var image = 
	  {
	    url: 'images/letter_i.png',
	    size: new google.maps.Size(30, 32),
	    origin: new google.maps.Point(0, 0),
	    anchor: new google.maps.Point(0, 32)
	  };
	 
	  var shape = 
	  {
	    coords: [1, 1, 1, 20, 18, 20, 18, 1],
	    type: 'poly'
	  };

	    var oldMarker = new google.maps.Marker
	    (
	    { 
		position: new google.maps.LatLng(24.1247459, 120.6888125), 
		map: map, 
		title:"old" 
		}
		); 

		var newMarker = new google.maps.Marker
		(
		{ 
		position: new google.maps.LatLng(24.134555, 120.6888125), 
		map: map, 
		title:"new" 
		}
		); 

		var meters = google.maps.geometry.spherical.computeDistanceBetween(oldMarker.getPosition(), newMarker.getPosition()); 
		//alert(meters);
		//document.getElementById("distance").innerText = meters+"米"; 
	}
	
	function addressToLatLng(addr) 
	{ 
		var geocoder = new google.maps.Geocoder();
	    geocoder.geocode
	    (
	    {
	        address: addr
	    }, 
	    function (results, status)
	    {
	        if (status == google.maps.GeocoderStatus.OK)
	        {
	           //alert(results[0].geometry.location.lat() + "," + results[0].geometry.location.lng());
	           a[m] = results[0].geometry.location.lng();
	           m++;
	           if(m == 10)
	           	transloc(a);
	           //b = results[0].geometry.location.lng();
	        } 
	        else 
	        {
	        	alert("error");
	            //查無經緯度
	        }
	        
	    });
	    
	    
	}

	 function transloc(a)
	           {
	           location.href="insertlatlng.php?lng=" +a;  
	       	   }
    </script>
    <script async defer
        src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDV5nEkXBMfkm57mVLzw7blZxIi3jBLgxU&signed_in=true&callback=initMap"></script>
  </body>
</html>