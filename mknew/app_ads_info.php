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
 
 
 if(isset($_POST['adsBanner']))
 {
   mysqli_query($con,"update app_info set adsBanner='".$_POST['adsBanner']."' where id=1"); 
 }

 if(isset($_POST['adsInterstitial']))
 {
   mysqli_query($con,"update app_info set adsInterstitial='".$_POST['adsInterstitial']."' where id=1"); 
 }

 if(isset($_POST['adsNative']))
 {
   mysqli_query($con,"update app_info set adsNative='".$_POST['adsNative']."' where id=1"); 
 }

 if(isset($_POST['adsRewarded']))
 {
   mysqli_query($con,"update app_info set adsRewarded='".$_POST['adsRewarded']."' where id=1"); 
 }

 if(isset($_POST['version_code']))
 {
   mysqli_query($con,"update app_info set version_code='".$_POST['version_code']."' where id=1"); 
 }

if(isset($_POST['appid']))
 {
   mysqli_query($con,"update app_info set appid=".$_POST['appid']." where id=1"); 
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
  <title>PKmaster</title>
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
  <label for="exampleInputEmail1">Version Code</label>
  <?php
  if(isset($_REQUEST['uid']))
  {

   ?>
   <input type="hidden" name="upid" value="<?php  echo $main['id']; ?>">
   <input class="form-control" id="exampleInputEmail1" type="text" value="<?php  echo $main['version_code']; ?>" aria-describedby="emailHelp"  name="version_code" placeholder="Enter version_code">
   <?php
 }
 else
 {
  ?>
  <input class="form-control" id="exampleInputEmail1" type="text" aria-describedby="emailHelp" name="version_code" required="" placeholder="Enter version_code">
  <?php
}
?>
</div>

<div class="form-group">
  <label for="exampleInputEmail1">Test Ads</label>
  <?php
  if(isset($_REQUEST['uid']))
  {

   ?>
   <input type="hidden" name="upid" value="<?php  echo $main['id']; ?>">
   <input class="form-control" id="exampleInputEmail1" type="text" value="<?php  echo $main['appid']; ?>" aria-describedby="emailHelp"  name="appid" placeholder="Enter appid">
   <?php
 }
 else
 {
  ?>
  <input class="form-control" id="exampleInputEmail1" type="text" aria-describedby="emailHelp" name="appid" required="" placeholder="Enter appid">
  <?php
}
?>
</div>


<div class="form-group">
  <label for="exampleInputEmail1">adsBanner</label>
  <?php
  if(isset($_REQUEST['uid']))
  {

   ?>
   <input type="hidden" name="upid" value="<?php  echo $main['id']; ?>">
   <input class="form-control" id="exampleInputEmail1" type="text" value="<?php  echo $main['adsBanner']; ?>" aria-describedby="emailHelp"  name="adsBanner" placeholder="Enter adsBanner">
   <?php
 }
 else
 {
  ?>
  <input class="form-control" id="exampleInputEmail1" type="text" aria-describedby="emailHelp" name="adsBanner" required="" placeholder="Enter adsBanner">
  <?php
}
?>
</div>

<div class="form-group">
  <label for="exampleInputEmail1">adsInterstitial</label>
  <?php
  if(isset($_REQUEST['uid']))
  {

   ?>
   <input class="form-control" id="exampleInputEmail1" type="text" value="<?php  echo $main['adsInterstitial']; ?>" aria-describedby="emailHelp"  name="adsInterstitial" placeholder="Enter adsInterstitial">
   <?php
 }
 else
 {
  ?>
  <input class="form-control" id="exampleInputEmail1" type="text" aria-describedby="emailHelp" name="adsInterstitial" required="" placeholder="Enter adsInterstitial">
  <?php
}
?>
</div>




<div class="form-group">
  <label for="exampleInputEmail1">adsNative</label>
  <?php
  if(isset($_REQUEST['uid']))
  {

   ?>
   <input class="form-control" id="exampleInputEmail1" type="text" value="<?php  echo $main['adsNative']; ?>" aria-describedby="emailHelp"  name="adsNative" placeholder="Enter GadsNative">
   <?php
 }
 else
 {
  ?>
  <input class="form-control" id="exampleInputEmail1" type="text" aria-describedby="emailHelp" name="adsNative" required="" placeholder="Enter adsNative">
  <?php
}
?>
</div>

<div class="form-group">
  <label for="exampleInputEmail1">adsRewarded</label>
  <?php
  if(isset($_REQUEST['uid']))
  {

   ?>
   <input class="form-control" id="exampleInputEmail1" type="text" value="<?php  echo $main['adsRewarded']; ?>" aria-describedby="emailHelp"  name="adsRewarded" placeholder="Enter adsRewarded">
   <?php
 }
 else
 {
  ?>
  <input class="form-control" id="exampleInputEmail1" type="text" aria-describedby="emailHelp" name="adsRewarded" required="" placeholder="Enter adsRewarded">
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
              <th>Test Ads</th>
              <th>adsBanner</th>
              <th>adsInterstitial</th>
              <th>adsNative</th>
              <th>adsRewarded</th>
              <th>Update</th>
            </tr>
          </thead>
          <tfoot>
            <tr>
              <th>id</th>
              <th>version_code</th>
              <th>Test Ads</th>
              <th>adsBanner</th>
              <th>adsInterstitial</th>
              <th>adsNative</th>
              <th>adsRewarded</th>
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
                <td><?php  echo $row["appid"]; ?></td>
                <td><?php  echo $row["adsBanner"]; ?></td>
                <td><?php  echo $row["adsInterstitial"]; ?></td>
                <td><?php  echo $row["adsNative"]; ?></td>
                <td><?php  echo $row["adsRewarded"]; ?></td>
             
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

