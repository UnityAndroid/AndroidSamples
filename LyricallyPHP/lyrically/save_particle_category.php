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
  mysqli_query($con,"delete from particle_category where category_id = ".$_POST['did']."");
}
else if(isset($_POST['uid']))
{
  $queryy=mysqli_query($con,"select * from particle_category where category_id=".$_POST['uid']."");
  $main=mysqli_fetch_array($queryy,MYSQLI_ASSOC);
}
else if(isset($_POST['category_id']))
{

 if(isset($_FILES['Icon']))
 {
   $Icon=$_FILES['Icon'];
   $milliseconds = round(microtime(true) * 1000);
   $name=$milliseconds.".jpg";

   $icon_path=getcwd()."/Images/particle/icons/".$name;
   $icon_url="http://$_SERVER[HTTP_HOST]"."/lyrically/Images/particle/icons/".$name;
   
   move_uploaded_file($_FILES['Icon']['tmp_name'],$Icon_path);

   mysqli_query($con,"update particle_category set icon_path='".$Icon_path."',icon_url='".$Icon_url."' where category_id=".$_POST['category_id']."");
 }

 if(isset($_POST['category_name']))
 {
   mysqli_query($con,"update particle_category set category_name='".$_POST['category_name']."' where category_id=".$_POST['category_id']."");
 }
 
}
else if(isset($_POST['category_name']))
{
  $category_name=$_POST['category_name'];
  
  $milliseconds = round(microtime(true) * 1000);
  $ext = end((explode(".", $_FILES["img"]["name"])));
  
  $name=$milliseconds.".".$ext;

  $icon_path=getcwd()."/Images/particle/icons/".$name;
  $icon_url="http://$_SERVER[HTTP_HOST]"."/lyrically/Images/particle/icons/".$name;
  
  move_uploaded_file($_FILES['img']['tmp_name'],$icon_path);

  $queryy=mysqli_query($con,"select * from particle_category order by position desc limit 1");
  $main=mysqli_fetch_array($queryy,MYSQLI_ASSOC);
  $pos=$main['position']+1;


  mysqli_query($con,"insert into particle_category values(0,'".$category_name."','".$icon_path."','".$icon_url."',".$pos.")");
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
  <!-- Bootstrap core CSS-->
    <script type="text/javascript" src="js/miss.js">
  </script>
  <link href="vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">
  <!-- Custom fonts for this template-->
  <link href="vendor/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
  <!-- Custom styles for this template-->
  <link href="css/sb-admin.css" rel="stylesheet">
</head>

<body class="fixed-nav sticky-footer bg-dark" id="page-top" onload="get_data('save_particle_category','show_particle_category',0,'',0);">
  <div class="container-fluid" >
    <div class="card card-register mx-auto mt-5">
      <div class="card-header">Particle Category</div>
      <div class="card-body">


       <form name="myform" method="POST" enctype="multipart/form-data" action="save_particle_category.php">



        <div class="form-group">
          <label for="exampleInputEmail1">Category Name</label>
          <?php
          if(isset($_REQUEST['uid']))
          {

           ?>
           <input type="hidden" name="category_id" value="<?php echo $main['category_id']; ?>" />
           <input class="form-control" id="exampleInputEmail1" type="text" value="<?php  echo $main['category_name']; ?>" aria-describedby="emailHelp"  name="category_name" placeholder="Enter Category Name">

           <?php
         }
         else
         {
          ?>
          <input class="form-control" id="exampleInputEmail1" type="text" aria-describedby="emailHelp" name="category_name" required="" placeholder="Enter Category Name">

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
                  Browse… <input type="file" id="imgInp" name="img">
                  <?php
                }
                else{
                  ?>
                  Browse… <input type="file" id="imgInp" name="img" required="">
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
    <i class="fa fa-table"></i> Particleparticle_category Category List</div>
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

