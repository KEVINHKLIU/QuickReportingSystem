<?php
    header('refresh: 3; url = "hospitallist.php"');
	include("MysqlSet.inc.php");

    $exeflag = true;
    $sql = "SELECT reportid, ambulanceID FROM assigntable_hospital where `V/I` = 'I'";
    $result = mysql_query($sql) or die('assign hospital error');
    while($row = mysql_fetch_array($result))
    {
        $exeflag = false;
        $reportid = $row[0];
        $ambID = $row[1];
        break;
    }

    if($exeflag)
    {
        mysql_close($con); 
        die('no waiting mission');
    }
    sleep(1);  
    
    //讀取暫存在資料庫各醫院到報案地點的距離，並做排序
    //利用while(true)作等待，等待Distanse()中的ajax執行完成
    while(true)
    {
        $sql = "SELECT * FROM assigntable_hospital where reportid = '$reportid' && ambulanceID = '$ambID' && `V/I` = 'I'";
        $result = mysql_query($sql) or die('assign hospital error');
        $row = mysql_fetch_array($result);
        if($row[3] == $reportid)                //ajax至少已經完成一筆資料寫入
        {
            sleep(1);                           //等待後續資料寫入
            
            $sql = "SELECT * FROM assigntable_hospital where reportid = '$reportid' && ambulanceID = '$ambID'  && `V/I` = 'I'";
            $result = mysql_query($sql) or die('select error');
             
            $num = 0;
            //auth[]存取醫院地址，dist[]存取距離
            while($row = mysql_fetch_array($result))
            {
                if($row[2] != 0)
                {
                    $listID[$num] = $row[0];
                    $auth[$num] = $row[1];
                    $dist[$num] = $row[2];
                    $num++;
                }
            }

            $upd = "UPDATE assigntable_hospital set `V/I` = 'O' where reportid = '$reportid' && ambulanceID = '$ambID'";
            $quer = mysql_query($upd) or die('change flag error');
            //進行排序，以取得最近的醫院
            if($num > 1)
            {
                for($i = 0; $i < $num; $i++)
                {
                    for($j = 1; $j < ($num - $i); $j++)
                    {
                        if($dist[$j]*1 < $dist[$j-1]*1)
                        {
                            $tempID = $listID[$j];
                            $templen = $dist[$j];
                            $tempaddr = $auth[$j];
                            $listID[$j] = $listID[$j-1];
                            $dist[$j] = $dist[$j-1];
                            $auth[$j] = $auth[$j-1];
                            $listID[$j-1] = $tempID;
                            $dist[$j-1] =  $templen;
                            $auth[$j-1] =  $tempaddr;
                        }
                    }
                }
            }

            for($i = 0; $i < $num; $i++)
            {
                $sql = "SELECT id FROM hospitalinfo where hAddress = '$auth[$i]'";
                $result = mysql_query($sql) or die('select id error');
                $row = mysql_fetch_array($result);
                $hospID = $row[0];
                
                $sql = "SELECT * FROM assigntable_hospital where id = $listID[$i]";
                $result = mysql_query($sql) or die('select id error');
                $row = mysql_fetch_array($result);
                $amb_ID = $row[4];
                $child = $row[5];
                $type = $row[6];
                $burn = $row[7];
                $burn_over_40 = $row[8];
                $poison = $row[9];
                $psycho = $row[10];
                $IM = $row[11];
                $OBS = $row[12];

                $sql = "SELECT * FROM sickbeds where id = $hospID";
                $result = mysql_query($sql) or die('select availabe sickbed error');
                $row = mysql_fetch_array($result);
                if($type == 3)
                {
                    if($child == 1)
                    {
                        if($row[5] * 1 > 0)
                        {
                            $ins = "INSERT INTO hospitallist (reportid, num, hospitalID, distance, ambulanceID, bedinfo) values ($reportid, $num, $hospID, '$dist[$i]', '$amb_ID', '$row[5]')";
                            $result = mysql_query($ins) or die('insert child bedinfo error');
                        }
                        else if($row[5] == 0 || $row[5] == -1)
                        {
                            if($row[2] == -1)
                            {
                                if($row[7] == 0)
                                {
                                    $ins = "INSERT INTO hospitallist (reportid, num, hospitalID, distance, ambulanceID, bedinfo) values ($reportid, $num, $hospID, '$dist[$i]', '$amb_ID', 'not_full')";
                                    $result = mysql_query($ins) or die('insert child bedinfo when normal bed not full error');
                                }
                                else
                                {
                                    $ins = "INSERT INTO hospitallist (reportid, num, hospitalID, distance, ambulanceID, bedinfo) values ($reportid, $num, $hospID, '$dist[$i]', '$amb_ID', 'full')";
                                    $result = mysql_query($ins) or die('insert child bedinfo when normal bed full error');
                                }
                            }
                            else if($row[2] > 0)
                            {
                                $ins = "INSERT INTO hospitallist (reportid, num, hospitalID, distance, ambulanceID, bedinfo) values ($reportid, $num, $hospID, '$dist[$i]', '$amb_ID', 'no_child_bed')";
                                $result = mysql_query($ins) or die('insert burn bedinfo error');
                            }
                            else if($row[2] == 0)
                            {
                                $ins = "INSERT INTO hospitallist (reportid, num, hospitalID, distance, ambulanceID, bedinfo) values ($reportid, $num, $hospID, '$dist[$i]', '$amb_ID', 'no_bed')";
                                $result = mysql_query($ins) or die('insert burn bedinfo error');
                            }
                        }
                    }
                    else
                    {
                        if($row[2] == -1)
                        {
                            if($row[7] == 0)
                            {
                                $ins = "INSERT INTO hospitallist (reportid, num, hospitalID, distance, ambulanceID, bedinfo) values ($reportid, $num, $hospID, '$dist[$i]', '$amb_ID', 'not_full')";
                                $result = mysql_query($ins) or die('insert normal bedinfo when not full error');
                            }
                            else
                            {
                                $ins = "INSERT INTO hospitallist (reportid, num, hospitalID, distance, ambulanceID, bedinfo) values ($reportid, $num, $hospID, '$dist[$i]', '$amb_ID', 'full')";
                                $result = mysql_query($ins) or die('insert normal bedinfo when full error');
                            }
                        }
                        else
                        {
                            $ins = "INSERT INTO hospitallist (reportid, num, hospitalID, distance, ambulanceID, bedinfo) values ($reportid, $num, $hospID, '$dist[$i]', '$amb_ID', '$row[2]')";
                            $result = mysql_query($ins) or die('insert normal bedinfo error');
                        }
                    }
                }
                else if($type == 0)
                {
                    if($row[3] * 1 >= 0)
                    {
                        $ins = "INSERT INTO hospitallist (reportid, num, hospitalID, distance, ambulanceID, bedinfo) values ($reportid, $num, $hospID, '$dist[$i]', '$amb_ID', '$row[3]')";
                        $result = mysql_query($ins) or die('insert burn bedinfo error');
                    }
                    if($row[3] == -1)
                    {
                        $ins = "INSERT INTO hospitallist (reportid, num, hospitalID, distance, ambulanceID, bedinfo) values ($reportid, $num, $hospID, '$dist[$i]', '$amb_ID', 'no_burn_bed')";
                        $result = mysql_query($ins) or die('insert burn bedinfo error');
                    }
                }
                else if($type == 2)
                {
                    if($row[4] == -1)
                    { 
                        if($row[7] == 0)
                        {
                            $ins = "INSERT INTO hospitallist (reportid, num, hospitalID, distance, ambulanceID, bedinfo) values ($reportid, $num, $hospID, '$dist[$i]', '$amb_ID', 'not_full')";
                            $result = mysql_query($ins) or die('insert psycho bedinfo when not full error');
                        }
                        else
                        {
                            $ins = "INSERT INTO hospitallist (reportid, num, hospitalID, distance, ambulanceID, bedinfo) values ($reportid, $num, $hospID, '$dist[$i]', '$amb_ID', 'full')";
                            $result = mysql_query($ins) or die('insert psycho bedinfo when full error');
                        }
                    }
                    else
                    {
                        $ins = "INSERT INTO hospitallist (reportid, num, hospitalID, distance, ambulanceID, bedinfo) values ($reportid, $num, $hospID, '$dist[$i]', '$amb_ID', '$row[4]')";
                        $result = mysql_query($ins) or die('insert psycho bedinfo error');
                    }    
                }
                else if($type == 1)
                {
                    if($row[6] == -1)
                    {
                        $ins = "INSERT INTO hospitallist (reportid, num, hospitalID, distance, ambulanceID, bedinfo) values ($reportid, $num, $hospID, '$dist[$i]', '$amb_ID', 'no_OBS_bed')";
                        $result = mysql_query($ins) or die('insert baby bedinfo with -1 error');
                    }
                    else
                    {
                        $ins = "INSERT INTO hospitallist (reportid, num, hospitalID, distance, ambulanceID, bedinfo) values ($reportid, $num, $hospID, '$dist[$i]', '$amb_ID', '$row[6]')";
                        $result = mysql_query($ins) or die('insert baby bedinfo error');
                    }
                }
            }
           
            //刪除暫存於資料庫的報案地點與各醫院距離
            //$del = "DELETE FROM assigntable_hospital where reportid = '$reportid' && ambulanceID = '$ambID'";
            //$sql = mysql_query($del) or die('delete error');

            break;
        }
        else
           echo " ";
    }	
    mysql_close($con); 
?>