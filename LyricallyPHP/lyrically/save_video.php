<?php
session_start();
ob_start();
include('connect.php');

if(!$_SESSION["username"])
{
 header("location:login.php");
}

if(!$_SESSION["catid"])
{
    $cats=mysqli_query($con,"select * from video_category");
    $main=mysqli_fetch_array($cats,MYSQLI_ASSOC);
    $_SESSION["catid"]=$main['Cat_Id'];
}

if(isset($_POST['uid']))
{
  $queryy=mysqli_query($con,"select * from videos where Id=".$_POST['uid']."");
  $main=mysqli_fetch_array($queryy,MYSQLI_ASSOC);
}
else if(isset($_POST['Id']))
{
  if(isset($_FILES['Thumnail']))
  {
   $Icon=$_FILES['Thumnail'];
   $milliseconds = round(microtime(true) * 1000);
   $name=$milliseconds.".jpg";

   $Icon_path=getcwd()."/Images/video_images/".$name;
   $Icon_url="http://$_SERVER[HTTP_HOST]"."/lyrically/Images/video_images/".$name;

   move_uploaded_file($_FILES['Thumnail']['tmp_name'],$Icon_path);

   mysqli_query($con,"update videos set Thumnail_Path='".$Icon_path."',Thumnail_Url='".$Icon_url."' where Id=".$_POST['Id']."");
 }

 if(isset($_POST['Cat_Id']))
 {
   mysqli_query($con,"update videos set Cat_Id=".$_POST['Cat_Id']." where Id=".$_POST['Id']."");
 }

 if(isset($_POST['Theme_Id']))
 {
   mysqli_query($con,"update videos set Theme_Id=".$_POST['Theme_Id']." where Id=".$_POST['Id']."");
 }

 if(isset($_POST['Theme_Name']))
 {
   mysqli_query($con,"update videos set Theme_Name='".$_POST['Theme_Name']."' where Id=".$_POST['Id']."");
 }

 if(isset($_POST['GameobjectName']))
 {
   mysqli_query($con,"update videos set GameobjectName='".$_POST['GameobjectName']."' where Id=".$_POST['Id']."");
 }

 if(isset($_POST['Sound_Id']))
 {

   $sounds=mysqli_query($con,"select * from sounds where id=".$_POST['Sound_Id']."");
   $sound=mysqli_fetch_array($sounds,MYSQLI_ASSOC);

   mysqli_query($con,"update videos set SoundName='".$sound['sound_name']."',SoundFile='".$sound['sound_full_url']."',sound_size='".$sound['sound_size']."',lyrics='".$sound['lyrics']."' where Id=".$_POST['Id']."");
 }
 
}
else if(isset($_POST['Theme_Name'])&&isset($_FILES['Thumnail']))
{
  $Cat_Id=$_POST['Cat_Id'];
  
  $Theme_Id=$_POST['Theme_Id'];
  $Theme_Name=$_POST['Theme_Name'];
  $Icon=$_FILES['Thumnail'];
  $GameobjectName=$_POST['GameobjectName'];
  $Sound_Id=$_POST['Sound_Id'];

  $sounds=mysqli_query($con,"select * from sounds where id=".$Sound_Id."");
  $sound=mysqli_fetch_array($sounds,MYSQLI_ASSOC);

  $milliseconds = round(microtime(true) * 1000);
  $name=$milliseconds.".jpg";

  $Icon_path=getcwd()."/Images/video_images/".$name;
  $Icon_url="http://$_SERVER[HTTP_HOST]"."/lyrically/Images/video_images/".$name;

  move_uploaded_file($_FILES['Thumnail']['tmp_name'],$Icon_path);
    
    
  $queryy=mysqli_query($con,"select * from videos order by position desc limit 1");
  $main=mysqli_fetch_array($queryy,MYSQLI_ASSOC);
  $pos=$main['position']+1;
  
  mysqli_query($con,"insert into videos values(0,".$Cat_Id.",".$Theme_Id.",'".$Theme_Name."','".$Icon_path."','".$Icon_url."','".$sound['sound_name']."','".$sound['sound_full_url']."','".$sound['sound_size']."',".$GameobjectName.",0,0,".$pos.",'".$sound['lyrics']."')");

  $catid=$_POST['catid'];
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

<body class="fixed-nav sticky-footer bg-dark" id="page-top" onload="get_custom_data('save_video','show_video',0,'',0,'<?php echo $_SESSION["catid"]; ?>');">
  <div class="container-fluid" >
    <div class="card card-register mx-auto mt-5">
      <div class="card-header">Videos</div>
      <div class="card-body">


       <form name="myform" method="POST" enctype="multipart/form-data" action="save_video.php">
        <div class="form-group">
          <label for="exampleInputEmail1">Video Category</label>
          <select class="form-control" name="Cat_Id" required="">
            <?php
            $cats=mysqli_query($con,"select * from video_category");
            $first=0;
            while ($cat=mysqli_fetch_array($cats,MYSQLI_ASSOC)) {

             if(isset($_REQUEST['uid']))
             {
              if($cat['Cat_Id']==$main['Cat_Id'])
              {
                ?>
                <option value="<?php echo $cat['Cat_Id']; ?>" selected="selected"><?php echo $cat['Category_Name']; ?></option>
                <?php
              }else{
                ?>
                <option value="<?php echo $cat['Cat_Id']; ?>"><?php echo $cat['Category_Name']; ?></option>
                <?php
              }
            }else{
             if($first==0)
             {
              $first=1;
              ?>
              <option value="<?php echo $cat['Cat_Id']; ?>" selected="selected"><?php echo $cat['Category_Name']; ?></option>
              <?php
            }else
            {
              ?>
              <option value="<?php echo $cat['Cat_Id']; ?>"><?php echo $cat['Category_Name']; ?></option>
              <?php
            }
          }


        }
        ?>
      </select>
    </div>

    <div class="form-group">
     <label for="exampleInputEmail1">Filter ID</label>
         <?php
      if(isset($_REQUEST['uid']))
      {
       ?>
       <input class="form-control" id="exampleInputEmail1" type="text" value="<?php  echo $main['Theme_Id']; ?>" aria-describedby="emailHelp"  name="Theme_Id" placeholder="Enter Filter ID">
       <?php
     }
     else
     {
      ?>
      <input class="form-control" id="exampleInputEmail1" type="text" aria-describedby="emailHelp" name="Theme_Id" required="" placeholder="Enter Filter ID">

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
     <input type="hidden" name="Id" value="<?php echo $main['Id']; ?>" />
     <input class="form-control" id="exampleInputEmail1" type="text" value="<?php  echo $main['Theme_Name']; ?>" aria-describedby="emailHelp"  name="Theme_Name" placeholder="Enter Theme Name">
     <?php
   }
   else
   {
    ?>
    <input class="form-control" id="exampleInputEmail1" type="text" aria-describedby="emailHelp" name="Theme_Name" required="" placeholder="Enter Theme Name">

    <?php
  }
  ?>
</div>

<div class="form-group">            
  <label for="exampleInputName">Thumbnail</label>
  <div class="form-row">

    <div class="input-group">
      <span class="input-group-btn">
        <span class="btn btn-default btn-file">

          <?php
          if(isset($_REQUEST['uid']))
          {

            ?>

            Browse… <input type="file" id="imgInp" name="Thumnail">
            <?php
          }
          else{
            ?>

            Browse… <input type="file" id="imgInp" name="Thumnail" required="">
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

<div class="form-group">
  <label for="exampleInputEmail1">Sound</label>
  <select class="form-control" name="Sound_Id" required="">

    <?php
    $sounds=mysqli_query($con,"select * from sounds");
    $first=0;
    while ($sound=mysqli_fetch_array($sounds,MYSQLI_ASSOC)) {
      if(isset($_REQUEST['uid']))
      {
        if($sound['sound_full_url']==$main['SoundFile'])
        {
          ?>
          <option value="<?php echo $sound['id']; ?>" selected="selected"><?php echo $sound['sound_name']; ?></option>
          <?php
        }else{
         ?>
         <option value="<?php echo $sound['id']; ?>"><?php echo $sound['sound_name']; ?></option>
         <?php
       }
     }else{

      if($first==0)
      {
        $first=1;
        ?>
        <option value="<?php echo $sound['id']; ?>" selected="selected"><?php echo $sound['sound_name']; ?></option>
        <?php
      }else
      {
        ?>
        <option value="<?php echo $sound['id']; ?>"><?php echo $sound['sound_name']; ?></option>
        <?php
      }
    }
  }
  ?>
</select>
</div>

<div class="form-group">
   <label for="exampleInputEmail1">Lyrics Template ID</label>
       <?php
  if(isset($_REQUEST['uid']))
  {

    ?>
    <input class="form-control" id="exampleInputEmail1" type="text" value="<?php  echo $main['GameobjectName']; ?>" aria-describedby="emailHelp"  name="GameobjectName" placeholder="Enter Lyrics Template ID">

    <?php
  }
  else
  {
    ?>
    <input class="form-control" id="exampleInputEmail1" type="text" aria-describedby="emailHelp" name="GameobjectName" required="" placeholder="Enter Lyrics Template ID">

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

