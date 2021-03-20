<?php
session_start();
ob_start();
include('connect.php');

include("getID3/getid3/getid3.php");
if(!$_SESSION["username"])
{
 header("location:login.php");
}

if(!$_SESSION["particle_category"])
{
  $cats=mysqli_query($con,"select * from particle_category");
  $main=mysqli_fetch_array($cats,MYSQLI_ASSOC);
  $_SESSION["particle_category"]=$main['category_id'];
}


if(isset($_POST['uid']))
{
  $queryy=mysqli_query($con,"select * from particle where id=".$_POST['uid']."");
  $main=mysqli_fetch_array($queryy,MYSQLI_ASSOC);
}else if(isset($_POST['id']))
{

 if(isset($_FILES['themeimg']))
 {
   $milliseconds = round(microtime(true) * 1000);
   $ext = end((explode(".", $_FILES["themeimg"]["name"])));
   $name=$milliseconds.".".$ext;

   $img_path=getcwd()."/Images/particle/themes/".$name2;
   $img_url="http://$_SERVER[HTTP_HOST]"."/lyrically/Images/particle/themes/".$name;

   move_uploaded_file($_FILES['themeimg']['tmp_name'],$img_path);

   mysqli_query($con,"update particle set theme_path='".$img_path."',theme_url='".$img_url."' where id=".$_POST['id']."");
 }

 if(isset($_FILES['thumbimg']))
 {
   $milliseconds = round(microtime(true) * 1000);
   $ext = end((explode(".", $_FILES["thumbimg"]["name"])));
   $name=$milliseconds.".".$ext;

   $thumb_path=getcwd()."/Images/particle/thumb/".$name;
   $thumb_url="http://$_SERVER[HTTP_HOST]"."/lyrically/Images/particle/thumb/".$name;

   move_uploaded_file($_FILES['thumbimg']['tmp_name'],$thumb_path);

   mysqli_query($con,"update particle set thumb_path='".$thumb_path."',thumb_url='".$thumb_url."' where id=".$_POST['id']."");
 }

 if(isset($_FILES['particle']))
 {
   $milliseconds = round(microtime(true) * 1000);
   $ext = end((explode(".", $_FILES["particle"]["name"])));
   $name=$milliseconds.".".$ext;

   $filename = pathinfo($_FILES['particle']['name'], PATHINFO_FILENAME);
  
   $file_path=getcwd()."/Images/particle/files/".$name;
   $file_url="http://$_SERVER[HTTP_HOST]"."/lyrically/Images/particle/files/".$name;

   move_uploaded_file($_FILES['particle']['tmp_name'],$file_path);

   mysqli_query($con,"update particle set particle_path='".$file_path."',particle_url='".$file_url."',prefab_name='".$filename."' where id=".$_POST['id']."");
 }

 if(isset($_POST['category_id']))
 {
   mysqli_query($con,"update particle set category_id=".$_POST['category_id']." where id=".$_POST['id']."");
 }

 if(isset($_POST['theme_name']))
 {
   mysqli_query($con,"update particle set theme_name='".$_POST['theme_name']."' where id=".$_POST['id']."");
 }

 if(isset($_POST['prefab_name']))
 {
   mysqli_query($con,"update particle set prefab_name='".$_POST['prefab_name']."' where id=".$_POST['id']."");
 }

}else if(isset($_POST['category_id']))
{

  $category_id=$_POST['category_id'];
  $theme_name=$_POST['theme_name'];
  $prefab_name=$_POST['prefab_name'];
  
  $milliseconds = round(microtime(true) * 1000);
  $ext = end((explode(".", $_FILES["thumbimg"]["name"])));
  $extt = end((explode(".", $_FILES["particle"]["name"])));
  $ext2 = end((explode(".", $_FILES["themeimg"]["name"])));
  
   $filename = pathinfo($_FILES['particle']['name'], PATHINFO_FILENAME);
  

  $name=$milliseconds.".".$ext;
  $namee=$milliseconds.".".$extt;
  $name2=$milliseconds.".".$ext2;
  
  $thumb_path=getcwd()."/Images/particle/thumb/".$name;
  $thumb_url="http://$_SERVER[HTTP_HOST]"."/lyrically/Images/particle/thumb/".$name;
  
  $file_path=getcwd()."/Images/particle/files/".$namee;
  $file_url="http://$_SERVER[HTTP_HOST]"."/lyrically/Images/particle/files/".$namee;
  
  $img_path=getcwd()."/Images/particle/themes/".$name2;
  $img_url="http://$_SERVER[HTTP_HOST]"."/lyrically/Images/particle/themes/".$name2;


  move_uploaded_file($_FILES['thumbimg']['tmp_name'],$thumb_path);
  move_uploaded_file($_FILES['particle']['tmp_name'],$file_path);
  move_uploaded_file($_FILES['themeimg']['tmp_name'],$img_path);

  $queryy=mysqli_query($con,"select * from particle order by position desc limit 1");
  $main=mysqli_fetch_array($queryy,MYSQLI_ASSOC);
  $pos=$main['position']+1;
  
  $sorts=mysqli_query($con,"select * from sortparticle order by position desc limit 1");
  $sort=mysqli_fetch_array($sorts,MYSQLI_ASSOC);
  $position=$sort['position']+1;
  

  mysqli_query($con,"insert into particle values(0,".$category_id.",'".$theme_name."','".$filename."','".$img_path."','".$img_url."','".$thumb_path."','".$thumb_url."','".$file_path."','".$file_url."',".$pos.")");

  $parts=mysqli_query($con,"select * from particle where position=".$pos."");
  $part=mysqli_fetch_array($parts,MYSQLI_ASSOC);
  mysqli_query($con,"insert into sortparticle values(0,".$part['id'].",'".$part['theme_name']."',".$position.")");

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

<body class="fixed-nav sticky-footer bg-dark" id="page-top" onload="get_custom_data('save_particle','show_particle',0,'',0,'<?php echo $_SESSION["particle_category"]; ?>');">
  <div class="container-fluid" >
    <div class="card card-register mx-auto mt-5">
      <div class="card-header">Particles</div>
      <div class="card-body">


       <form name="myform" method="POST" enctype="multipart/form-data" action="save_particle.php">


        <div class="form-group">
          <label for="exampleInputEmail1">Particle Category</label>
          <select class="form-control" name="category_id" required="">
            <?php
            $cats=mysqli_query($con,"select * from particle_category");
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
      <label for="exampleInputEmail1">Theme Name</label>
      <?php
      if(isset($_REQUEST['uid']))
      {
       ?>

       ?>
       <input type="hidden" name="id" value="<?php echo $main['id']; ?>" />
       <input class="form-control" id="exampleInputEmail1" type="text" value="<?php  echo $main['theme_name']; ?>" aria-describedby="emailHelp"  name="theme_name" placeholder="Enter Theme Name">
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

 

<div class="form-group">           
  <label for="exampleInputName">Theme Thumbnail</label>
  <div class="form-row">

    <div class="input-group">
      <span class="input-group-btn">
        <span class="btn btn-default btn-file">

          <?php
          if(isset($_REQUEST['uid']))
          {
            ?>
            Browse… <input type="file" id="imgInn" name="themeimg">
            <?php
          }
          else{
            ?>
            Browse… <input type="file" id="imgInn" name="themeimg" required="">
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

      <img id='img-uploaa' />
      <?php
    }
    else{
      ?>

      <img id='img-uploaa'/>
      <?php
    }
    ?>
  </div>
</div>





<div class="form-group">           
  <label for="exampleInputName">Particle Thumbnail</label>
  <div class="form-row">

    <div class="input-group">
      <span class="input-group-btn">
        <span class="btn btn-default btn-file">

          <?php
          if(isset($_REQUEST['uid']))
          {
            ?>
            Browse… <input type="file" id="imgInp" name="thumbimg">
            <?php
          }
          else{
            ?>
            Browse… <input type="file" id="imgInp" name="thumbimg" required="">
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
  <label for="exampleInputName">Particle</label>
  <div class="form-row">

    <div class="input-group">
      <span class="input-group-btn">
        <span class="btn btn-default btn-file">

          <?php
          if(isset($_REQUEST['uid']))
          {
            ?>
            Browse… <input type="file" id="imgInpp" name="particle">
            <?php
          }
          else{
            ?>
            Browse… <input type="file" id="imgInpp" name="particle" required="">
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

      <img id='img-uploadd' />
      <?php
    }
    else{
      ?>

      <img id='img-uploadd'/>
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
    <i class="fa fa-table"></i> Particle List</div>
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

