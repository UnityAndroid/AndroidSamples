<?php
include 'connect.php';

include("getID3/getid3/getid3.php");
if(!$_SESSION["username"])
{
     header("location:login.php");
}

if(isset($_POST['did']))
{
  mysqli_query($con,"delete from sounds where id = ".$_POST['did']."");
}
else if(isset($_POST['sound_name']))
{
    

  $category_id=$_POST['category_id'];
  $sound_name=$_POST['sound_name'];
  $Icon=$_FILES['Sound'];

  echo "Size: " . ($_FILES["Sound"]["size"] / 1024) . " Kb<br />";
  
  $milliseconds = round(microtime(true) * 1000);
  $name=$milliseconds.".mp3";
  $Icon_path=getcwd()."/Sounds/".$name;
  $Icon_url="http://$_SERVER[HTTP_HOST]"."/Sounds/".$name;


  move_uploaded_file($_FILES['Sound']['tmp_name'],$Icon_path);
  
      $getID3 = new getID3;
     $ThisFileInfo = $getID3->analyze($Icon_path);
     $len= @$ThisFileInfo['playtime_string'];
       

     mysqli_query($con,"insert into sounds values(0,".$category_id.",'".$sound_name."','".$Icon_path."','".$Icon_url."','".$len."',0)");

}


/*
$cats=mysqli_query($con,"select * from video_category");
if(mysqli_num_rows($cats)>0)
{
  while ($cat=mysqli_fetch_array($cats,MYSQLI_ASSOC)) {

    $category=array();
    $category['Cat_Id']=(int)$cat['Cat_Id'];
    $category['Category_Name']=$cat['Category_Name'];
    $category['Icon_path']=$cat['Icon_path'];
    $category['Icon_url']=$cat['Icon_url'];
    $category['status']=(int)$cat['status'];

    array_push($response['result'],$category);  
  }
}
*/
?>

<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <meta name="description" content="">
  <meta name="author" content="">
  <title>MusicBeat</title>
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
            {
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
         <input type="hidden" name="uid" value="<?php echo $main['Id']; ?>" />
         <input class="form-control" id="exampleInputEmail1" type="text" value="<?php  echo $main['Category_Name']; ?>" aria-describedby="emailHelp"  name="sound_name" placeholder="Enter Sound Name">

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

    <div class="form-group">            <label for="exampleInputName">Sound File</label>
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
  <button type="submit" class="btn btn-primary btn-block" name="submit">Insert/Update</button>
  <a href="index.php" class="btn btn-primary btn-block" style="color:#fff;">Home</a>

</form>

</div>
</div>
<br />
<div class="card mb-3">
  <div class="card-header" >
    <i class="fa fa-table"></i> Sounds List</div>
    <div class="card-body">
      <div class="table-responsive">
        <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0" style="text-align:center;">
          <thead>
            <tr>
              <th>id</th>
              <th>category_id</th>
              <th>sound_name</th>
              
              <th>sound_size</th>
              <th>sound_full_url</th>
              <th>Delete Sound</th>
            </tr>
          </thead>
          <tfoot>
            <tr>
              <th>id</th>
              <th>category_id</th>
              <th>sound_name</th>
              <th>sound_size</th>
              <th>sound_full_url</th>
              <th>Delete Sound</th>
            </tr>
          </tfoot>
          <tbody>

           <?php
           $query=mysqli_query($con,"select * from sounds");
           if($query)
           {

            while ($row=mysqli_fetch_array($query,MYSQLI_ASSOC)) {


              ?>
              <tr>
                <td><?php echo $row["id"];  ?></td>
                <td><?php  echo $row["category_id"]; ?></td>
                <td><?php  echo $row["sound_name"]; ?></td>

                <td><?php  echo $row["sound_size"]; ?></td>
                <td><?php  echo $row["sound_full_url"]; ?></td>
              <!--   <td><a href="save_video_category.php?uaid=<?php echo $row['Cat_Id']; ?>">Edit</td>
              -->
              <td>
               <form action="save_sound.php" method="POST">
                <input type="hidden" name="did" value="<?php echo $row['id']; ?>">
                <input type="submit" name="delete" value="Delete">
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

