<!DOCTYPE html>
<html>
  <head>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no">
    <meta charset="utf-8">
    <title>報案分佈</title>
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
    
    $sql = "SELECT * FROM recordt";
    $result = mysql_query($sql) or die('select error');

    $i=0;
    while($row = mysql_fetch_array($result))
    {
      $lng[$i] = $row[9];
      $lat[$i] = $row[10];
      $i++;
    }
    
    ?>

    <script>
    var latitude = <?php echo json_encode($lat);?>;
    var longitude = <?php echo json_encode($lng);?>;

function initMap() 
{
  var map = new google.maps.Map(document.getElementById('map'), 
  {
    zoom: 17,
    center: {lat: 24.1247459, lng:  120.6888125}
  }
  );

  setMarkers(map);
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

  for (var i = 0; i < latitude.length; i++) 
  {
    var lt = latitude[i]*1;
    var ln = longitude[i]*1;
    var marker = new google.maps.Marker
    (
    {
      position: {lat: lt, lng: ln},
      map: map,
      icon: image,
      shape: shape,
      title: "beach[0]",
      zIndex: "beach[3]"
    }
    );
  }
}
    </script>
    
    <script async defer
        src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDV5nEkXBMfkm57mVLzw7blZxIi3jBLgxU&signed_in=true&callback=initMap"></script>
  </body>
</html>