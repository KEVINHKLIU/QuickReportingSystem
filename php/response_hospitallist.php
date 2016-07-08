<?php
	if(isset($_POST["request"]))
	{
		include("MysqlSet.inc.php");
		$password = $_POST["password"];
		$id = $_POST["reportID"];
		$id = str_replace("reportID:  ", "",$id);

		$sql = "SELECT count(*), num from hospitallist where reportid = $id && ambulanceID = '$password' && VI = 'V'";
	    $result = mysql_query($sql) or die('select hospital error');
	    while($row = mysql_fetch_array($result))
	    {
	    	if($row[0] == $row[1])
	    	{
	    		echo "1";
	    	}
	    	else
	    		echo "0";
	    }
		mysql_close($con);
	}	
	if(isset($_POST["getHosp"]))
	{  
		include("MysqlSet.inc.php");
		$password = $_POST["password"];
		$id = $_POST["reportID"];
		$id = str_replace("reportID:  ", "",$id);

		$i = 0;
		$sql = "SELECT * from hospitallist where reportid = $id && ambulanceID = '$password' && VI = 'V'";
        $result = mysql_query($sql) or die('select hospital error');
        while($row = mysql_fetch_array($result))
        {
            $ID[$i] = $row[3];
            $dist[$i] = $row[4];
            $info[$i] = $row[6];
            $i++;
        }
		 
		$n = 0;
		if($i < 5)
		{
			echo $i;
		    while($n < $i)
		    {
		        $sql = "SELECT * from hospitalinfo where id = $ID[$n]";
		        $result = mysql_query($sql) or die('select hospital error');
		        $row = mysql_fetch_array($result);
		        echo "#".$row[3].'#'.$row[1].'#'.$row[9].'#'.$dist[$n].'#'.$info[$n];
		        $n++;
		    }
		}
		else
		{
			echo "5";
		    while($n < 5)
		    {
		        $sql = "SELECT * from hospitalinfo where id = $ID[$n]";
		        $result = mysql_query($sql) or die('select hospital error');
		        $row = mysql_fetch_array($result);
		        echo "#".$row[3].'#'.$row[1].'#'.$row[9].'#'.$dist[$n].'#'.$info[$n];
		        $n++;
		    }
		}
		$upd = "UPDATE hospitallist SET VI = 'I' WHERE reportid = $id && ambulanceID = '$password'";
		$result = mysql_query($upd) or die('update hospital list VI error');
		mysql_close($con);
	}
?>