<?php
session_start();
ob_start();
include('connect.php');

if(!$_SESSION["username"])
{
 header("location:login.php");
}

if(isset($_POST['did']))
{
  mysqli_query($con,"delete from apps where id = ".$_POST['did']."");
}
else if(isset($_POST['id']))
{
  if(isset($_FILES['Icon']))
  {
   $milliseconds = round(microtime(true) * 1000);
   $namee=$milliseconds.".png";

   $Icon_path=getcwd()."/Images/apps/".$namee;
   $Icon_url="http://$_SERVER[HTTP_HOST]"."/pkmaster/Images/apps/".$namee;

   move_uploaded_file($_FILES['Icon']['tmp_name'],$Icon_path);

   mysqli_query($con,"update apps set logo='".$Icon_path."',logo_path='".$Icon_url."' where id=".$_POST['id']."");
 }

 if(isset($_POST['name']))
 {
   mysqli_query($con,"update apps set name='".$_POST['name']."' where id=".$_POST['id']."");
 }

 if(isset($_POST['package']))
 {
   mysqli_query($con,"update apps set package='".$_POST['package']."' where id=".$_POST['id']."");
 }

 if(isset($_POST['url']))
 {
   mysqli_query($con,"update apps set url='".$_POST['url']."' where id=".$_POST['id']."");
 }

}
else if(isset($_POST['name'])&&isset($_FILES['Icon']))
{
  $name=$_POST['name'];
  $package=$_POST['package'];
  $url=$_POST['url'];
  $Icon=$_FILES['Icon'];

  $milliseconds = round(microtime(true) * 1000);
  $namee=$milliseconds.".png";

  $Icon_path=getcwd()."/Images/apps/".$namee;
  $Icon_url="http://$_SERVER[HTTP_HOST]"."/pkmaster/Images/apps/".$namee;
  
  move_uploaded_file($_FILES['Icon']['tmp_name'],$Icon_path);
    
 $queryy=mysqli_query($con,"select * from apps order by position desc limit 1");
  $main=mysqli_fetch_array($queryy,MYSQLI_ASSOC);
  $pos=$main['position']+1;
  
  
  mysqli_query($con,"insert into apps values(0,'".$name."','".$package."','".$url."','".$Icon_url."','".$Icon_path."',0,".$pos.")");
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

<body class="fixed-nav sticky-footer bg-dark" id="page-top" onload="get_data('save_apps','show_apps',0,'',0);">
  <div class="container-fluid" >
    <div class="card card-register mx-auto mt-5">
      <div class="card-header">Video Categorys</div>
      <div class="card-body">


       <form name="myform" method="POST" enctype="multipart/form-data" action="save_apps.php">



        <div class="form-group">
          <label for="exampleInputEmail1">App name</label>
          <?php
          if(isset($_REQUEST['uid']))
          {

           ?>
           <input type="hidden" name="uid" value="<?php echo $main['id']; ?>" />
           <input class="form-control" id="exampleInputEmail1" type="text" value="<?php  echo $main['name']; ?>" aria-describedby="emailHelp"  name="name" placeholder="Enter App name">

           <?php
         }
         else
         {
          ?>
          <input class="form-control" id="exampleInputEmail1" type="text" aria-describedby="emailHelp" name="name" required="" placeholder="Enter App name">

          <?php
        }
        ?>
      </div>

      <div class="form-group">
        <label for="exampleInputEmail1">App package</label>
        <?php
        if(isset($_REQUEST['uid']))
        {

         ?>
         <input class="form-control" id="exampleInputEmail1" type="text" value="<?php  echo $main['package']; ?>" aria-describedby="emailHelp"  name="package" placeholder="Enter App package">

         <?php
       }
       else
       {
        ?>
        <input class="form-control" id="exampleInputEmail1" type="text" aria-describedby="emailHelp" name="package" required="" placeholder="Enter App package">

        <?php
      }
      ?>
    </div>

    <div class="form-group">
      <label for="exampleInputEmail1">App url</label>
      <?php
      if(isset($_REQUEST['uid']))
      {

       ?>
       <input class="form-control" id="exampleInputEmail1" type="text" value="<?php  echo $main['url']; ?>" aria-describedby="emailHelp"  name="url" placeholder="Enter App url">

       <?php
     }
     else
     {
      ?>
      <input class="form-control" id="exampleInputEmail1" type="text" aria-describedby="emailHelp" name="url" required="" placeholder="Enter App url">

      <?php
    }
    ?>
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
<button type="submit" class="btn btn-primary btn-block" name="submit">Insert/Update</button>
<a href="index.php" class="btn btn-primary btn-block" style="color:#fff;">Home</a>

</form>

</div>
</div>
<br />
<div class="card mb-3">
  <div class="card-header" >
    <i class="fa fa-table"></i> Apps List</div>
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

