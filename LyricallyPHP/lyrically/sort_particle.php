<?php
include 'connect.php';
if(!$_SESSION["username"])
{
 header("location:login.php");
}

if(isset($_POST['uid']))
{
  $queryy=mysqli_query($con,"select * from sortparticle where id=".$_POST['uid']."");
  $main=mysqli_fetch_array($queryy,MYSQLI_ASSOC);
}
else if(isset($_POST['id']))
{
  if(isset($_POST['theme_name']))
  {
   mysqli_query($con,"update sortparticle set theme_name='".$_POST['theme_name']."' where id=".$_POST['id'].""); 
 }

 if(isset($_POST['particle_id']))
 {
   mysqli_query($con,"update sortparticle set particle_id=".$_POST['particle_id']." where id=".$_POST['id'].""); 
 }
}
else if(isset($_POST['theme_name']))
{
  $theme_name=$_POST['theme_name'];
  $particle_id=$_POST['particle_id'];
  
  $queryy=mysqli_query($con,"select * from sortparticle order by position desc limit 1");
  $main=mysqli_fetch_array($queryy,MYSQLI_ASSOC);
  $pos=$main['position']+1;


  mysqli_query($con,"insert into sortparticle values(0,".$particle_id.",'".$theme_name."',".$pos.")");
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
  <title>Lyrically Beat</title>
  <script type="text/javascript" src="js/miss.js">
  </script>
  <!-- Bootstrap core CSS-->
  <link href="vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">
  <!-- Custom fonts for this template-->
  <link href="vendor/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
  <!-- Custom styles for this template-->
  <link href="css/sb-admin.css" rel="stylesheet">
</head>

<body class="fixed-nav sticky-footer bg-dark" id="page-top" onload="get_data('save_sort_particle','show_sort_particle',0,'',0);">
  <div class="container-fluid" >
    <div class="card card-register mx-auto mt-5">
      <div class="card-header">Sort Particles</div>
      <div class="card-body">
       <form name="myform" method="POST" enctype="multipart/form-data" action="sort_particle.php">



        <div class="form-group">
          <label for="exampleInputEmail1">Particle ID</label>
          <?php
          if(isset($_REQUEST['uid']))
          {

           ?>
           <input class="form-control" id="exampleInputEmail1" type="text" value="<?php  echo $main['particle_id']; ?>" aria-describedby="emailHelp"  name="particle_id" placeholder="Enter Particle ID">
           <?php
         }
         else
         {
          ?>
          <input class="form-control" id="exampleInputEmail1" type="text" aria-describedby="emailHelp" name="particle_id" required="" placeholder="Enter Particle ID">
          <?php
        }
        ?>
      </div>


      <div class="form-group">
        <label for="exampleInputEmail1">Theme Name</label>
        <?php
        if(isset($_REQUEST['uid']))
        {

         ?>
         <input type="hidden" name="category_id" value="<?php echo $main['id']; ?>" />
         <input class="form-control" id="exampleInputEmail1" type="text" value="<?php  echo $main['category_name']; ?>" aria-describedby="emailHelp"  name="theme_name" placeholder="Enter Theme Name">

         <?php
       }
       else
       {
        ?>
        <input class="form-control" id="exampleInputEmail1" type="text" aria-describedby="emailHelp" name="theme_name" required="" placeholder="Enter Theme Name">

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
 }else{
  ?>
  <button type="submit" class="btn btn-primary btn-block" name="submit">Insert</button>
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
    <i class="fa fa-table"></i> Sort Particle List</div>

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

