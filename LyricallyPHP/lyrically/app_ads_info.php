<?php
session_start();
ob_start();
include('connect.php');

if(!$_SESSION["username"])
{
 header("location:login.php");
}
if(isset($_POST['upid']))
{
 
 

 if(isset($_POST['gstatus']))
 {
   mysqli_query($con,"update app_info set gstatus='".$_POST['gstatus']."' where id=1"); 
 }

 if(isset($_POST['fstatus']))
 {
   mysqli_query($con,"update app_info set fstatus='".$_POST['fstatus']."' where id=1"); 
 }

 if(isset($_POST['g2status']))
 {
   mysqli_query($con,"update app_info set g2status='".$_POST['g2status']."' where id=1"); 
 }

 if(isset($_POST['f2status']))
 {
   mysqli_query($con,"update app_info set f2status='".$_POST['f2status']."' where id=1"); 
 }


}



/* $status=$_POST['status'];
 $url=$_POST['url'];
 $version_code=$_POST['version_code'];
 $appid=$_POST['appid'];
 $banner=$_POST['banner'];
 $inter=$_POST['inter'];
 $reward=$_POST['reward'];
 $native=$_POST['native'];

 mysqli_query($con,"update app_info set message='".$message."',status=".$status.",url='".$url."',version_code=".$version_code.",appid='".$appid."',banner='".$banner."',inter='".$inter."',reward='".$reward."',native='".$native."' where id=".$_POST['upid']."");
}*/

$mains=mysqli_query($con,"select * from app_info");
$main=mysqli_fetch_array($mains,MYSQLI_ASSOC);
?>

<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <meta name="description" content="">
  <meta name="author" content="">
  <title>Lyrically Bit</title>
  <!-- Bootstrap core CSS-->
  <link href="vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">
  <!-- Custom fonts for this template-->
  <link href="vendor/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
  <!-- Custom styles for this template-->
  <link href="css/sb-admin.css" rel="stylesheet">
</head>

<body class="fixed-nav sticky-footer bg-dark" id="page-top" >
  <div class="container-fluid" >
    <div class="card card-register mx-auto mt-5">
      <div class="card-header">Apps Ads Info</div>
      <div class="card-body">
       <form name="myform" method="POST" enctype="multipart/form-data" action="app_ads_info.php">







<div class="form-group">
  <label for="exampleInputEmail1">Google Status</label>
  <?php
  if(isset($_REQUEST['uid']))
  {

   ?>
   <input type="hidden" name="upid" value="<?php  echo $main['id']; ?>">
   <input class="form-control" id="exampleInputEmail1" type="text" value="<?php  echo $main['gstatus']; ?>" aria-describedby="emailHelp"  name="gstatus" placeholder="Enter Google Status">
   <?php
 }
 else
 {
  ?>
  <input class="form-control" id="exampleInputEmail1" type="text" aria-describedby="emailHelp" name="gstatus" required="" placeholder="Enter Google Status">
  <?php
}
?>
</div>

<div class="form-group">
  <label for="exampleInputEmail1">Facebook Status</label>
  <?php
  if(isset($_REQUEST['uid']))
  {

   ?>
   <input class="form-control" id="exampleInputEmail1" type="text" value="<?php  echo $main['fstatus']; ?>" aria-describedby="emailHelp"  name="fstatus" placeholder="Enter Facebook Status">
   <?php
 }
 else
 {
  ?>
  <input class="form-control" id="exampleInputEmail1" type="text" aria-describedby="emailHelp" name="fstatus" required="" placeholder="Enter Facebook Status">
  <?php
}
?>
</div>




<div class="form-group">
  <label for="exampleInputEmail1">Google 2 Status</label>
  <?php
  if(isset($_REQUEST['uid']))
  {

   ?>
   <input class="form-control" id="exampleInputEmail1" type="text" value="<?php  echo $main['g2status']; ?>" aria-describedby="emailHelp"  name="g2status" placeholder="Enter Google 2 Status">
   <?php
 }
 else
 {
  ?>
  <input class="form-control" id="exampleInputEmail1" type="text" aria-describedby="emailHelp" name="g2status" required="" placeholder="Enter Google Status">
  <?php
}
?>
</div>

<div class="form-group">
  <label for="exampleInputEmail1">Facebook 2 Status</label>
  <?php
  if(isset($_REQUEST['uid']))
  {

   ?>
   <input class="form-control" id="exampleInputEmail1" type="text" value="<?php  echo $main['f2status']; ?>" aria-describedby="emailHelp"  name="f2status" placeholder="Enter Facebook 2 Status">
   <?php
 }
 else
 {
  ?>
  <input class="form-control" id="exampleInputEmail1" type="text" aria-describedby="emailHelp" name="f2status" required="" placeholder="Enter Facebook 2 Status">
  <?php
}
?>
</div>





</div>
<div class="form-group">
  <div class="form-row">
  </div>
</div>
<?php
if(isset($_REQUEST['uid']))
{

 ?>
 <button type="submit" class="btn btn-primary btn-block" name="submit">Update</button>

 <?php
}
?>
<a href="index.php" class="btn btn-primary btn-block" style="color:#fff;">Home</a>

</form>

</div>
</div>
<br />
<div class="card mb-3">
  <div class="card-header" >
    <i class="fa fa-table"></i> App Info</div>
    <div class="card-body">
      <div class="table-responsive">
        <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0" style="text-align:center;">
          <thead>
            <tr>
              <th>id</th>
              <th>version_code</th>
              <th>Google</th>
              <th>Facebook</th>
              <th>App 2 Google</th>
              <th>App 2 Facebook</th>
              <th>Update</th>
            </tr>
          </thead>
          <tfoot>
            <tr>
              <th>id</th>
              <th>version_code</th>
              <th>Google</th>
              <th>Facebook</th>
              <th>App 2 Google</th>
              <th>App 2 Facebook</th>
              <th>Update</th>
            </tr>
          </tfoot>
          <tbody>

           <?php
           $query=mysqli_query($con,"select * from app_info");
           if($query)
           {

            while ($row=mysqli_fetch_array($query,MYSQLI_ASSOC)) {


              ?>
              <tr>
                <td><?php echo $row["id"];  ?></td>
                <td><?php  echo $row["version_code"]; ?></td>
                <td><?php  echo $row["gstatus"]; ?></td>
                <td><?php  echo $row["fstatus"]; ?></td>
                <td><?php  echo $row["g2status"]; ?></td>
                <td><?php  echo $row["f2status"]; ?></td>
             
              <td>
               <form action="app_ads_info.php" method="POST">
                <input type="hidden" name="uid" value="<?php echo $row['id']; ?>">
                <input type="submit" name="delete" value="Update">
              </form>
            </td>

          </tr>
          <?php
        }
      }
      ?>
    </tbody>
  </table>
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

