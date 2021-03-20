<?php
session_start();
ob_start();
include('connect.php');

if(!$_SESSION["username"])
{
     header("location:login.php");
}

    if(isset($_POST['title']))
	{
		date_default_timezone_set('Asia/Kolkata'); 

		$apiKey = "AAAAtwkk6_8:APA91bHwOFB_zib2M3y9Mm3ezO4U14O2wpGWWBd7wmq7O7C7ohOMVVUep90UYoCz305fzD65qB-TTmw4faJT_wZ2_i8yzYRY2sUbJTxAs4_BBLE-zrvw4hYkD9WNQyJlrCnaiB5qaVG9";
		$registrationIDs=array();
		
		$url = 'https://fcm.googleapis.com/fcm/send';
		$message=$_POST['message'];
		$title=$_POST['title'];
        $Icon=$_FILES['Icon'];

        $milliseconds = round(microtime(true) * 1000);
        $namee=$milliseconds.".png";

        $Icon_path=getcwd()."/Images/notify/".$namee;
        $Icon_url="http://$_SERVER[HTTP_HOST]"."/pkmaster/Images/notify/".$namee;
  
        move_uploaded_file($_FILES['Icon']['tmp_name'],$Icon_path);


		mysqli_query($con,"insert into notify values(0,'".$title."','".$message."','".date("d-m-Y H:i:s")."','".$Icon_path."','".$Icon_url."')");

		/*$regs=mysqli_query($con,"select * from users order by id desc limit 50");
		while ($reg=mysqli_fetch_array($regs,MYSQLI_ASSOC)) {
			array_push($registrationIDs,$reg['token']);
		}
		*/
		
		$fields = array(
			'to' => "/topics/mkmaster",
			'data' => array( "text" => $message,"title" => $title,"icon" => $Icon_url),
      'notification' => array( "text" => $message,"title" => $title,"icon" => $Icon_url),
      
		);
		
     /*	$fields = array(
			'registration_ids' => $registrationIDs,
			'data' => array( "text" => $message,"title" => $title,"icon" => $Icon_url)
		);*/

		$headers = array(
			'Authorization:key='. $apiKey,
			'Content-Type: application/json'
		);

		$ch = curl_init();

    // Set the URL, number of POST vars, POST data
		curl_setopt( $ch, CURLOPT_URL, $url);
		curl_setopt( $ch, CURLOPT_POST, true);
		curl_setopt( $ch, CURLOPT_HTTPHEADER, $headers);
		curl_setopt( $ch, CURLOPT_RETURNTRANSFER, true);
    //curl_setopt( $ch, CURLOPT_POSTFIELDS, json_encode( $fields));

		curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
    // curl_setopt($ch, CURLOPT_POST, true);
    // curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
		curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode( $fields));

    // Execute post
		$result = curl_exec($ch);

    // Close connection
		curl_close($ch);

	}
	
?>

<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <meta name="description" content="">
  <meta name="author" content="">
  <title>PKmaster</title>
  <!-- Bootstrap core CSS-->
  <link href="vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">
  <!-- Custom fonts for this template-->
  <link href="vendor/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
  <!-- Custom styles for this template-->
  
  <script type="text/javascript" src="functions.js"></script>
  <link href="css/sb-admin.css" rel="stylesheet">
</head>

<body class="fixed-nav sticky-footer bg-dark" id="page-top" onload="app_notify('notify_data','app_notify','show','0','0','0')">
  <div class="container-fluid" >
    <div class="card card-register mx-auto mt-5">
      <div class="card-header">Notifications</div>
      <div class="card-body">


       <form name="myform" method="POST" enctype="multipart/form-data" action="app_notify.php">

         <div class="form-group">
          <label for="exampleInputEmail1">Title</label>

          <input class="form-control" id="title" type="text" aria-describedby="emailHelp" name="title" required="" placeholder="Enter Title">
        </div>



        <div class="form-group">
          <label for="exampleInputEmail1">Message</label>

          <input class="form-control" id="message" type="text" aria-describedby="emailHelp" name="message" required="" placeholder="Enter Message">
        </div>


      
       <div class="form-group">           
    <label for="exampleInputName">Icon</label>
    <div class="form-row">

      <div class="input-group">
        <span class="input-group-btn">
          <span class="btn btn-default btn-file">

            <?php
            if(isset($_REQUEST['uid']))
            {
              ?>
              Browse… <input type="file" id="imgInp" name="Icon">
              <?php
            }
            else{
              ?>

              Browse… <input type="file" id="imgInp" name="Icon" required="">
              <?php
            }
            ?>
          </span>
        </span>
        <input type="text" class="form-control" readonly>
      </div>
      <?php
      if(isset($_REQUEST['uid']))
      {
        ?>
        <img id='img-upload' />
        <?php
      }
      else{
        ?>
        <img id='img-upload'/>
        <?php
      }
      ?>
    </div>
  </div>
</div>



<div class="form-group">
  <div class="form-row">
  </div>
</div>

     <button type="submit" class="btn btn-primary btn-block" name="insert" >Insert</button>
      <a href="index.php" class="btn btn-primary btn-block" style="color:#fff;">Home</a>

    </form>

  </div>
</div>
<br />
<div class="card mb-3">
  <div class="card-header" >
    <i class="fa fa-table"></i> App Notifications</div>
    <div class="card-body">
      <div class="table-responsive" id="notify_data">

      </div>
    </div>
    <div class="card-footer small text-muted">Updated yesterday at 11:59 PM</div>
  </div>


</div>
<!-- Bootstrap core JavaScript-->
<script src="vendor/jquery/jquery.min.js"></script>
<script src="vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
<!-- Core plugin JavaScript-->
<script src="vendor/jquery-easing/jquery.easing.min.js"></script>
<!-- File Upload JavaScript-->

<script src="js/file-upload.js"></script>

</body>

</html>

