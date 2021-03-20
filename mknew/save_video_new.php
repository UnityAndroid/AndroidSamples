<?php
include 'connect.php';


if(!isset($_SESSION["catid"]))
{
	$cats=mysqli_query($con,"select * from video_category");
	$main=mysqli_fetch_array($cats,MYSQLI_ASSOC);
	$_SESSION["catid"]=$main['Cat_Id'];
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
	<script type="text/javascript" src="js/miss.js">
	</script>
	<!-- Bootstrap core CSS-->
	<link href="vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">
	<!-- Custom fonts for this template-->
	<link href="vendor/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
	<!-- Custom styles for this template-->
	<link href="css/sb-admin.css" rel="stylesheet">
</head>

<body class="fixed-nav sticky-footer bg-dark" id="page-top" onload="get_custom_data('save_video_new','show_video_new',0,'',0,'<?php echo $_SESSION["catid"]; ?>');">
	<div class="container-fluid" >
		<div class="card card-register mx-auto mt-5">
			<div class="card-header">Videos</div>
			<div class="card-body">


				<form name="myform" method="POST" enctype="multipart/form-data" action="save_video_new.php">
				
					
				<a href="index.php" class="btn btn-primary btn-block" style="color:#fff;">Home</a>

			</form>

		</div>
	</div>
	<br />
	<div class="card mb-3">
		<div class="card-header" >
			<i class="fa fa-table"></i> Video List</div>
			<div style="display: flex;align-items: center;align-content: center;">
				<div style="margin: 0 auto;">
					<img id="loading_spinner" src="Images/loading.gif" style=" display:none;width:100px;">
				</div>
			</div>
			<div id="data"></div> 
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

