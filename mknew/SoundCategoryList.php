<?php
include 'connect.php';

$response=array();
$response['data']=array();

$category=mysqli_query($con,"select * from sound_category order by position asc");

if(mysqli_num_rows($category)>0)
{
	$response['flag']=true;
	$response['message']="Sucess";
	$response['code']= 10;

	while($record=mysqli_fetch_array($category,MYSQLI_ASSOC))
	{
		$cat=array();
		$cat['category_id']=(int)$record['category_id'];
		$cat['category_name']=$record['category_name'];
		$cat['status']=(int)$record['status'];

		$cat['sounds']=array();


		$videos=mysqli_query($con,"select * from sounds where category_id=".$record['category_id']." order by position asc");
		while($vidrecord=mysqli_fetch_array($videos,MYSQLI_ASSOC))
		{
			$video=array();
			$video['id']=(int)$vidrecord['id'];
			$video['category_id']=(int)$vidrecord['category_id'];
			$video['sound_name']=$vidrecord['sound_name'];
			$video['sound_url']=$vidrecord['sound_url'];
			$video['sound_full_url']=$vidrecord['sound_full_url'];
			$video['sound_size']=$vidrecord['sound_size'];
			$video['status']=(int)$vidrecord['status'];

			array_push($cat['sounds'],$video);
		}


		array_push($response['data'],$cat);



	}
}else{
	$response['flag']=false;
	$response['message']="Failure";
	$response['code']= 0;
	
}

echo json_encode($response);


?>