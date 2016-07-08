<?php
include("MysqlSet.inc.php");



$query = mysql_query("SELECT 醫院, 一般病床, 燒傷病床, 嬰兒床 FROM sickbeds");

$category = array();
$category['name'] = '醫院';  //醫院

$series1 = array();
$series1['name'] = '一般病床'; //一般

$series2 = array();
$series2['name'] = '燒傷病床'; //燒傷

$series3 = array();
$series3['name'] = '嬰兒床'; //嬰兒床

$count = 0;
while($r = mysql_fetch_array($query)) {
	$count++;
	if($count== 7 )
		break;
    $category['data'][] = $r['醫院'];
    $series1['data'][] = $r['一般病床'];
    $series2['data'][] = $r['燒傷病床'];
    $series3['data'][] = $r['嬰兒床'];   
}

$result = array();
array_push($result,$category);
array_push($result,$series1);
array_push($result,$series2);
array_push($result,$series3);


print json_encode($result, JSON_NUMERIC_CHECK);

mysql_close($con);
?> 
