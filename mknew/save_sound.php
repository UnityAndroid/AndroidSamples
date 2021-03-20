<?php
session_start();
ob_start();
include('connect.php');
include("getID3/getid3/getid3.php");

if(!isset($_SESSION["username"]))
{
 header("location:login.php");
}

if(!isset($_SESSION["category_id"]))
{
  $cats=mysqli_query($con,"select * from sound_category");
  $main=mysqli_fetch_array($cats,MYSQLI_ASSOC);
  $_SESSION["category_id"]=$main['category_id'];
}

if(isset($_POST['uid']))
{
  $queryy=mysqli_query($con,"select * from sounds where id=".$_POST['uid']."");
  $main=mysqli_fetch_array($queryy,MYSQLI_ASSOC);
}else if(isset($_POST['id']))
{
  if($_FILES['Sound']['name']!="")
    {
   $Icon=$_FILES['Sound'];

   $milliseconds = round(microtime(true) * 1000);
   $name=$milliseconds.".mp3";

   $Icon_path=getcwd()."/Images/sounds/".$name;
   $Icon_url="https://$_SERVER[HTTP_HOST]"."/mkmaster/Images/sounds/".$name;

   move_uploaded_file($_FILES['Sound']['tmp_name'],$Icon_path);

   $getID3 = new getID3;
   $ThisFileInfo = $getID3->analyze($Icon_path);
   $len= @$ThisFileInfo['playtime_string'];

   mysqli_query($con,"update sounds set sound_url='".$Icon_path."',sound_full_url='".$Icon_url."' where id=".$_POST['id']."");

 }

 if(isset($_POST['category_id']))
 {
  mysqli_query($con,"update sounds set category_id=".$_POST['category_id']." where id=".$_POST['id']."");
}

if(isset($_POST['sound_name']))
{
 mysqli_query($con,"update sounds set sound_name='".$_POST['sound_name']."' where id=".$_POST['id']."");
}

}
else if(isset($_POST['sound_name']))
{

  $category_id=$_POST['category_id'];
  $sound_name=$_POST['sound_name'];
  $Icon=$_FILES['Sound'];

  $milliseconds = round(microtime(true) * 1000);
  $name=$milliseconds.".mp3";

  $Icon_path=getcwd()."/Images/sounds/".$name;
  $Icon_url="https://$_SERVER[HTTP_HOST]"."/mkmaster/Images/sounds/".$name;


  move_uploaded_file($_FILES['Sound']['tmp_name'],$Icon_path);
  
  $getID3 = new getID3;
  $ThisFileInfo = $getID3->analyze($Icon_path);
  $len= @$ThisFileInfo['playtime_string'];

  $queryy=mysqli_query($con,"select * from sounds order by position desc limit 1");
  $main=mysqli_fetch_array($queryy,MYSQLI_ASSOC);
  $pos=$main['position']+1;


  mysqli_query($con,"insert into sounds values(0,".$category_id.",'".$sound_name."','".$Icon_path."','".$Icon_url."','".$len."',0,".$pos.")");

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

<body class="fixed-nav sticky-footer bg-dark" id="page-top" onload="get_custom_data('save_sound','show_sound',0,'',0,'<?php echo $_SESSION["category_id"]; ?>');">
  <div class="container-fluid" >
    <div class="card card-register mx-auto mt-5">
      <div class="card-header">Sounds</div>
      <div class="card-body">


       <form name="myform" method="POST" enctype="multipart/form-data" action="save_sound.php">


        <div class="form-group">
          <label for="exampleInputEmail1">Sound Category</label>
          <select class="form-control" name="category_id" required="">

            <?php
            $cats=mysqli_query($con,"select * from sound_category");
            $first=0;
            while ($cat=mysqli_fetch_array($cats,MYSQLI_ASSOC)) {

              if(isset($_REQUEST['uid']))
              {
                if($cat['category_id']==$main['category_id'])
                {
                  ?>
                  <option value="<?php echo $cat['category_id']; ?>" selected="selected"><?php echo $cat['category_name']; ?></option>
                  <?php
                }else{
                  ?>
                  <option value="<?php echo $cat['category_id']; ?>"><?php echo $cat['category_name']; ?></option>
                  <?php
                }
              }else{
                if($first==0)
                {
                  $first=1;
                  ?>

                  <option value="<?php echo $cat['category_id']; ?>" selected="selected"><?php echo $cat['category_name']; ?></option>
                  <?php
                }else
                {
                  ?>
                  <option value="<?php echo $cat['category_id']; ?>"><?php echo $cat['category_name']; ?></option>
                  <?php
                }
              }
            }
            ?>
          </select>
        </div>


        <div class="form-group">
          <label for="exampleInputEmail1">Sound Name</label>
          <?php
          if(isset($_REQUEST['uid']))
          {

           ?>
           <input type="hidden" name="id" value="<?php echo $main['id']; ?>" />
           <input class="form-control" id="exampleInputEmail1" type="text" value="<?php  echo $main['sound_name']; ?>" aria-describedby="emailHelp"  name="sound_name" placeholder="Enter Sound Name">

           <?php
         }
         else
         {
          ?>
          <input class="form-control" id="exampleInputEmail1" type="text" aria-describedby="emailHelp" name="sound_name" required="" placeholder="Enter Sound Name">

          <?php
        }
        ?>
      </div>


    <div class="form-group">            
      <label for="exampleInputName">Sound File</label>
      <div class="form-row">

        <div class="input-group">
          <span class="input-group-btn">
            <span class="btn btn-default btn-file">

              <?php
              if(isset($_REQUEST['uid']))
              {
                ?>
                Browse… <input type="file" id="imgInp" name="Sound">
                <?php
              }
              else{
                ?>

                Browse… <input type="file" id="imgInp" name="Sound" required="">
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
    <i class="fa fa-table"></i> Sounds List</div>
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

