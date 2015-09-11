<?php
//copy of comments.php working version
require("config.inc.php");
//initial query
	   // $query = "Select * FROM comments where username =:username";
$query ="SELECT u.username,ur.weeks_count, ur.cumulative_count,g.global_counts 
FROM regd_users as u LEFT JOIN user_transactions as ur ON u.mobilenumber = ur.mobile_no
 LEFT JOIN global_counts as g ON u.global_counter_ref = global_counter_ref WHERE (u.username=:username') ";

    //Update query
    $query_params = array(':username' =>  $_POST['username']);
//echo 'query_params' ;
//echo json_encode($query_params);

//execute query
//echo'querying try';
try {
    $stmt   = $db->prepare($query);
    $result = $stmt->execute($query_params);
//echo json_encode($stmt);
//echo json_encode($result);

}
catch (PDOException $ex) {
    $response["success"] = 0;
    $response["message"] = "Database Error!";
    die(json_encode($response));
}

// Finally, we can retrieve all of the found rows into an array using fetchAll
$rows = $stmt->fetchAll();
//echo 'Rows'.json_encode($rows);

if ($rows) {
    $response["success"] = 1;
    $response["message"] = "Post Available!";
    $response["posts"]   = array();

    foreach ($rows as $row) {
        $post             = array();

        //this line is new:
        $post["post_id"]  = $row["post_id"];

        $post["username"] = $row["username"];
        $post["title"]    = $row["title"];
        $post["message"]  = $row["message"];


        //update our repsonse JSON data
        array_push($response["posts"], $post);
    }
    // echoing JSON response
    echo json_encode($response);


} else {
    $response["success"] = 0;
    $response["message"] = "No Post Available!";
    die(json_encode($response));
}

?>
