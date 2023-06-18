
<?php
    $host = "localhost"; //on same server we keep localhost
    $user = "id20065698_4i_s";  //username of the database
    $pass = "=zw-pwa7uJC}ZG|!";   //password of the database
    $db = "id20065698_blood_bank";  //name of database

    $con = mysqli_connect($host,$user,$pass,$db);

    if($con){
        //echo "Connected to Database";
    }else{
        //echo "Failed to connect ".mysqli_connect_error();
    }
?>