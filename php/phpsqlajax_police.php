<?php
include("MysqlSet.inc.php");

// Start XML file, create parent node
$doc = domxml_new_doc("1.0");
$node = $doc->create_element("markers");
$parnode = $doc->append_child($node);


// Select all the rows in the markers table
$query = "SELECT * FROM policeinfo";
$result = mysql_query($query);
if (!$result) 
{
  die('Invalid query: ' . mysql_error());
}

header("Content-type: text/xml");

// Iterate through the rows, adding XML nodes for each
while ($row = @mysql_fetch_assoc($result))
{
  // ADD TO XML DOCUMENT NODE
  $node = $doc->create_element("marker");
  $newnode = $parnode->append_child($node);
  $newnode->set_attribute("address", $row['address']);
}

$xmlfile = $doc->dump_mem();
echo $xmlfile;
?>