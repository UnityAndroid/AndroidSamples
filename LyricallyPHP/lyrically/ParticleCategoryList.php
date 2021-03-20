<?php
include 'connect.php';

$response=array();
$response['data']=array();
$response['bitdata']=array();
$response['sort']=array();
$response['apps']=array();

$category=mysqli_query($con,"select * from particle_category order by position asc");

$response['flag']=true;
$response['message']="Sucess";
$response['code']= 10;

while($record=mysqli_fetch_array($category,MYSQLI_ASSOC))
{
	$cat=array();
	$cat['category_id']=(int)$record['category_id'];
	$cat['category_name']=$record['category_name'];
	$cat['icon_url']=$record['icon_url'];
	$cat['position']=(int)$record['position'];

	$cat['particles']=array();


	$videos=mysqli_query($con,"select * from particle where category_id=".$record['category_id']." order by position desc");
	while($vidrecord=mysqli_fetch_array($videos,MYSQLI_ASSOC))
	{
		$video=array();
		$video['id']=(int)$vidrecord['id'];
		$video['category_id']=(int)$vidrecord['category_id'];
		$video['theme_name']=$vidrecord['theme_name'];
		$video['prefab_name']=$vidrecord['prefab_name'];
		$video['theme_url']=$vidrecord['theme_url'];
		$video['thumb_url']=$vidrecord['thumb_url'];
		$video['particle_url']=$vidrecord['particle_url'];
		$video['is_lock']=1;
		array_push($cat['particles'],$video);
	}

	array_push($response['data'],$cat);
}

	$bits=mysqli_query($con,"select * from bitparticle order by position desc");
	while($bit=mysqli_fetch_array($bits,MYSQLI_ASSOC))
	{
		$vid=array();
		$vid['id']=(int)$bit['id'];
		$vid['category_id']=(int)$bit['category_id'];
		$vid['theme_name']=$bit['theme_name'];
		$vid['prefab_name']=$bit['prefab_name'];
		$vid['theme_url']=$bit['theme_url'];
		$vid['thumb_url']=$bit['thumb_url'];
		$vid['particle_url']=$bit['particle_url'];
		$vid['is_lock']=1;
		array_push($response['bitdata'],$vid);
	}



$i=0;
$sorts=mysqli_query($con,"select * from sortparticle order by position asc");
while($sort=mysqli_fetch_array($sorts,MYSQLI_ASSOC))
{
    $i++;
	$part=array();
	$part['id']=(int)$sort['id'];
	$part['particle_id']=(int)$sort['particle_id'];
	$part['theme_name']=$sort['theme_name'];
	$part['position']=(int)$i;

	array_push($response['sort'],$part);
}


		$apps=mysqli_query($con,"select * from apps order by position asc");
		while($app=mysqli_fetch_array($apps,MYSQLI_ASSOC))
		{
			$apparray=array();
			$apparray['id']=(int)$app['id'];
			$apparray['name']=$app['name'];
			$apparray['package']=$app['package'];
			$apparray['url']=$app['url'];
			$apparray['logo']=$app['logo'];

			array_push($response['apps'],$apparray);
		}
		

echo json_encode($response);


?>