<?php

require("config.inc.php");

//if posted data is not empty
if (!empty($_POST)) {

    $query = "INSERT INTO satsang (  satsang_name,satsang_contact,satsang_mail,satsang_contact_mobile,satsang_city,satsang_state,satsang_zip,satsang_country )
    VALUES ( :name, :contact, :email,:mobile,:city,:state,:zip ,:country) ";
    
    //Again, we need to update our tokens with the actual data:
    $query_params = array(

        ':name' => $_POST['satname'],
        ':contact' => $_POST['satcontact'],
        ':email' => $_POST['email'] ,
        ':mobile' =>$_POST ['mobile'],
        ':city' => $_POST['city'],
        ':state' => $_POST['state'] ,
        ':country' =>$_POST ['country'],
         ':zip' =>$_POST ['zip']
    );

    //time to run our query, and create the user
    try {
        $stmt   = $db->prepare($query);
        $result = $stmt->execute($query_params);
    }
    catch (PDOException $ex) {
        // For testing, you could use a die and message. 
                
        //or just use this use this one:
        $response["success"] = 0;
        $response["message"] = "Database Error2. Please Try Again!";
        die(json_encode($response));
    }


    $response["success"] = 1;
    $response["message"] = "Satsang Successfully Added!";
    echo json_encode($response);



    //

} else {
?>
	<h1>Register</h1> 
	<form action="register_sang.php" method="post">
	    Satname:<br />
	    <input type="text" name="satname" value="" />
	    <br /><br /> 
	    Contact:<br />
	    <input type="text" name="satcontact" value="" />
	    <br /><br /> 
            Email:<br /> 
	    <input type="text" name="email" value="" />
	    <br /><br /> 
	    Mobile:<br />
	    <input type="text" name="mobile" value="" />
	    <br /><br />

            City:<br />
	    <input type="text" name="city" value="" />
	    State:<br />
	    <input type="text" name="state" value="" />
	    <br /><br /> 
	    country:<br />
	    <input type="text" name="country" value="" />
	    <br /><br />
	   Zip:<br />
	    <input type="text" name="zip" value="" />
	    <br /><br />

	    <br /><br />
	    <input type="submit" value="Create Satsang" />
	</form>
	<?php
}

?>