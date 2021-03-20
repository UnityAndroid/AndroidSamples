<?php
include 'connect.php';

if(!$_SESSION["username"])
{
     header("location:login.php");
}

if(isset($_POST['did']))
{
	mysqli_query($con,"delete from video_category where Cat_Id = ".$_POST['did']."");
}
else if(isset($_POST['Category_Name'])&&isset($_FILES['Icon']))
{
	$Category_Name=$_POST['Category_Name'];
	$Icon=$_FILES['Icon'];

 $check=mysqli_query($con,"select * from video_category where Category_Name like '".$Category_Name."'");
 if(mysqli_num_rows($check)==0)
 {
  $milliseconds = round(microtime(true) * 1000);
  $name=$milliseconds.".jpg";

  $Icon_path=getcwd()."/Images/video_category/".$name;
  $Icon_url="http://$_SERVER[HTTP_HOST]"."/Images/video_category/".$name;
  
  $back_path=getcwd()."/Images/video_category_background/".$name;
  $back_url="http://$_SERVER[HTTP_HOST]"."/Images/video_category_background/".$name;
  echo $Icon_path;
  move_uploaded_file($_FILES['Icon']['tmp_name'],$Icon_path);
  move_uploaded_file($_FILES['back_img']['tmp_name'],$back_path);

  
  echo("insert into video_category values(0,'".$Category_Name."','".$Icon_path."','".$Icon_url."',0,'".$back_path."','".$back_url."')");
}else{
  $error['errorcode']=1;
  $error['errormsg']="Category Already Exists";

  array_push($response['errorResult'],$error);
}
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
      <div class="card-header">Video Categorys</div>
      <div class="card-body">


       <form name="myform" method="POST" enctype="multipart/form-data" action="save_video_category.php">



        <div class="form-group">
          <label for="exampleInputEmail1">Category Name</label>
          <?php
          if(isset($_REQUEST['uid']))
          {

           ?>
           <input type="hidden" name="uid" value="<?php echo $main['Cat_Id']; ?>" />
           <input class="form-control" id="exampleInputEmail1" type="text" value="<?php  echo $main['Category_Name']; ?>" aria-describedby="emailHelp"  name="Category_Name" placeholder="Enter Category Name">

           <?php
         }
         else
         {
          ?>
          <input class="form-control" id="exampleInputEmail1" type="text" aria-describedby="emailHelp" name="Category_Name" required="" placeholder="Enter Category_Name">

          <?php
        }
        ?>
      </div>
      
       <div class="form-group">            <label for="exampleInputName">Icon</label>
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
        
        
        <div class="form-group">            
          <label for="exampleInputName">Background</label>
          <div class="form-row">
            <div class="input-group">
              <span class="input-group-btn">
                <span class="btn btn-default btn-file">
                  <?php
                  if(isset($_REQUEST['uid']))
                  {
                    ?>
                    Browse… <input type="file" id="imgInpp" name="back_img">
                    <?php
                  }
                  else{
                    ?>
                    Browse… <input type="file" id="imgInpp" name="back_img" required="">
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
    <button type="submit" class="btn btn-primary btn-block" name="submit">Insert/Update</button>
    <a href="index.php" class="btn btn-primary btn-block" style="color:#fff;">Home</a>

  </form>

</div>
</div>
<br />
<div class="card mb-3">
  <div class="card-header" >
    <i class="fa fa-table"></i> Video Category List</div>
    <div class="card-body">
      <div class="table-responsive">
        <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0" style="text-align:center;">
          <thead>
            <tr>
              <th>Cat_Id</th>
              <th>Category_Name</th>
              <th>Icon_url</th>
              <th>Delete Category</th>
            </tr>
          </thead>
          <tfoot>
            <tr>
              <th>Cat_Id</th>
              <th>Category_Name</th>
              <th>Icon_url</th>
              <th>Delete Category</th>
            </tr>
          </tfoot>
          <tbody>

           <?php
           $query=mysqli_query($con,"select * from video_category");
           if($query)
           {

            while ($row=mysqli_fetch_array($query,MYSQLI_ASSOC)) {


              ?>
              <tr>
                <td><?php echo $row["Cat_Id"];  ?></td>
                <td><?php  echo $row["Category_Name"]; ?></td>
                <td><?php  echo $row["Icon_url"]; ?></td>
              <!--   <td><a href="save_video_category.php?uaid=<?php echo $row['Cat_Id']; ?>">Edit</td>
 -->
                 <td>
                   <form action="save_video_category.php" method="POST">
                    <input type="hidden" name="did" value="<?php echo $row['Cat_Id']; ?>">
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

