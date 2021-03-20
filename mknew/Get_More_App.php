<?php
include 'connect.php';

$response=array();
if(isset($_REQUEST['android_id'])&&isset($_REQUEST['gcm_id']))
{
	$checkmobile=mysqli_query($con,"select * from users where android_id like '".$_REQUEST['android_id']."'");
	if(mysqli_num_rows($checkmobile)>0)
	{
		mysqli_query($con,"update users set token='".$_REQUEST['gcm_id']."' where android_id like '".$_REQUEST['android_id']."'");
	}else{
		mysqli_query($con,"insert into users(android_id,token) VALUES('".$_REQUEST['android_id']."','".$_REQUEST['gcm_id']."')");
	}
}

$response['flag']=true;
$response['message']="Sucess";
$response['code']= 10;

echo json_encode($response);
?>