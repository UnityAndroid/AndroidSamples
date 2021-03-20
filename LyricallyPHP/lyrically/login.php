<?php

session_start();
ob_start();
include('connect.php');

if(isset($_POST['submit']))
{
  $username=$_POST['username'];
  $password=$_POST['password'];

  $query=mysqli_query($con,"select * from user where username like '".$username."' AND password like '".$password."'");

  if(mysqli_num_rows($query)>0)
  {
    $_SESSION['username']=$username;
    header("location:index.php");
  }
  else
  {
    echo "Login Failed";
  }

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
   <title>Lyrically Beat</title> <!-- Bootstrap core CSS-->
  <link href="vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">
  <!-- Custom fonts for this template-->
  <link href="vendor/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
  <!-- Custom styles for this template-->
  <link href="css/sb-admin.css" rel="stylesheet">
</head>

<body class="bg-dark">
  <div class="container">
    <div class="card card-register mx-auto mt-5">
      <div class="card-header">Login  </div>
      <div class="card-body">

       <form name="myform" method="POST" >
        <div class="form-group">
          <div class="form-row">

            <label for="exampleInputName">Username</label>

            <input class="form-control" id="exampleInputName" type="text" name="username" required="" aria-describedby="nameHelp" placeholder="Enter Username">

          </div>
        </div>


        <div class="form-group">
          <div class="form-row">

            <label for="exampleInputName">Password</label>

            <input class="form-control" id="exampleInputName" type="password" name="password" required="" aria-describedby="nameHelp" placeholder="Enter Password">

          </div>
        </div>




        <button type="submit" class="btn btn-primary btn-block" name="submit">Login</button>

        <button type="reset" class="btn btn-primary btn-block" name="cancel">Cancel</button>
      </form>

    </div>
  </div>

  <!-- Bootstrap core JavaScript-->
  <script src="vendor/jquery/jquery.min.js"></script>
  <script src="vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
  <!-- Core plugin JavaScript-->
  <script src="vendor/jquery-easing/jquery.easing.min.js"></script>
</body>

</html>
