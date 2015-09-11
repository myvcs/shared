<?php
 //addtransactions2.php
//load and connect to MySQL database stuff
require("config.inc.php");

if (!empty($_POST)) {
		
$query="update user_transactions ur INNER JOIN global_counts g ON ur.global_trans_ref=g.ref_id 
  SET ur.weeks_count = :weekcnt,ur.cumulative_count=(ur.cumulative_count+:weekcnt),
   ur.week_ended= :weekend, g.global_counts =(:weekcnt+g.global_counts) where ur.username=:username";
      
    $query_params = array(
        ':username' => $_POST['username'],
	':weekcnt'=> $_POST['weekcnt'],
	':weekend' => $_POST['weekend']
    );

echo json_encode($query_params);
	//execute query
    try {
        $stmt   = $db->prepare($query);
        $result = $stmt->execute($query_params);
    }


    catch (PDOException $ex) {
        $response["success"] = 0;
        $response["message"] = "Database Error. Couldn't add post!";
        die(json_encode($response));
    }

    $response["success"] = 1;
    $response["message"] = "Username Successfully Added!";
   die( json_encode($response));

} 

else {
?>
		<h1>Add Comment</h1>
		<form action="addtransactions2.php" method="post">
		  Username:<br />
		   <input type="text" name="username" placeholder="username" />
		    <br /><br />
		   Count:<br />
		    <input type="text" name="weekcnt" placeholder="post count" />
		    <br /><br />
			Week ending:<br />
		    <input type="date" name="weekend" placeholder="post date" />
		    <br /><br />
		    <input type="submit" value="Add Comment" />
		</form>
	<?php
}

?>