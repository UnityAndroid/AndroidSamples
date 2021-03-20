<?php
include 'connect.php';

 
	$response=array();
	if(isset($_REQUEST['android_id'])&&isset($_REQUEST['gcm_id']))
	{
		$response['data']=array();
		$checkmobile=mysqli_query($con,"select * from users where android_id like '".$_REQUEST['android_id']."'");
		if(mysqli_num_rows($checkmobile)>0)
		{
				mysqli_query($con,"update users set token='".$_REQUEST['gcm_id']."' where android_id like '".$_REQUEST['android_id']."'");
		}else{
			mysqli_query($con,"insert into users(android_id,token) VALUES('".$_REQUEST['android_id']."','".$_REQUEST['gcm_id']."')");
		}
		
		$update=mysqli_query($con,"select * from app_info");
		$info=mysqli_fetch_array($update,MYSQLI_ASSOC);

		$response['update']['id']=(int)$info['id'];
		$response['update']['message']=$info['message'];
		$response['update']['status']=(int)$info['status'];
		$response['update']['url']=$info['url'];
		$response['update']['version_code']=(int)$info['version_code'];

		$category=mysqli_query($con,"select * from apps");

		if(mysqli_num_rows($category)>0)
		{
			$response['flag']=true;
			$response['message']="Sucess";
			$response['code']= 10;

			while($record=mysqli_fetch_array($category,MYSQLI_ASSOC))
			{
				$cat=array();
				$cat['id']=(int)$record['id'];
				$cat['name']=$record['name'];
				$cat['url']=$record['url'];
				$cat['logo']=$record['logo'];
				$cat['status']=(int)$record['status'];

				array_push($response['data'],$cat);
			}
		}else{
			$response['flag']=false;
			$response['message']="Failure";
			$response['code']= 0;


		}

	
	}
	
	echo json_encode($response);
?>