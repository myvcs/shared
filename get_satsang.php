<?php
 
    require("config.inc.php");
    $q=mysql_query("SELECT * FROM satsang");
    while($row=mysql_fetch_assoc($q))
            $json_output[]=$row;
      
    print(json_encode($json_output));
      
    mysql_close();
     
?>