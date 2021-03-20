<?php
include 'connect.php';


$response=array();
$response['data']=array();
$response['data_new']=array();
$response['ads_data']=array();


$category=mysqli_query($con,"select * from video_category order by position asc");

if(mysqli_num_rows($category)>0)
{
	$response['flag']=true;
	$response['message']="Sucess";
	$response['code']= 10;

	while($record=mysqli_fetch_array($category,MYSQLI_ASSOC))
	{
		$cat=array();
		$cat['Cat_Id']=(int)$record['Cat_Id'];
		$cat['Category_Name']=$record['Category_Name'];
		$cat['Icon']=$record['Icon_url'];
		$cat['status']=(int)$record['status'];
		$cat['videos']=array();

		$videos=mysqli_query($con,"select * from videos where Cat_Id=".$record['Cat_Id']." order by position asc");
		while($vidrecord=mysqli_fetch_array($videos,MYSQLI_ASSOC))
		{
			$video=array();
			$video['Id']=(int)$vidrecord['Id'];
			$video['Cat_Id']=(int)$vidrecord['Cat_Id'];
			$video['Theme_Name']=$vidrecord['Theme_Name'];
			$video['Thumnail_Big']=$vidrecord['Thumnail_Url'];
			$video['bundle_url']=$vidrecord['bundle_url'];
			$video['SoundName']=$vidrecord['SoundName'];
			$video['SoundFile']=$vidrecord['SoundFile'];
			$video['sound_size']=$vidrecord['sound_size'];
			$video['GameobjectName']=$vidrecord['GameobjectName'];
			$video['Status']=(int)$vidrecord['Status'];
			
			array_push($cat['videos'],$video);
		}

		array_push($response['data'],$cat);
	}


		$videonew=mysqli_query($con,"select * from videos_new order by new_position asc");
		while($vidrecord=mysqli_fetch_array($videonew,MYSQLI_ASSOC))
		{
			$video=array();
			$video['Id']=(int)$vidrecord['Id'];
			$video['Cat_Id']=(int)$vidrecord['Cat_Id'];
			$video['Theme_Name']=$vidrecord['Theme_Name'];
			$video['Thumnail_Big']=$vidrecord['Thumnail_Url'];
			$video['bundle_url']=$vidrecord['bundle_url'];
			$video['SoundName']=$vidrecord['SoundName'];
			$video['SoundFile']=$vidrecord['SoundFile'];
			$video['sound_size']=$vidrecord['sound_size'];
			$video['GameobjectName']=$vidrecord['GameobjectName'];
			$video['Status']=(int)$vidrecord['Status'];
			
			array_push($response['data_new'],$video);
		}

		$adnew=mysqli_query($con,"select * from app_info");
		while($adrecord=mysqli_fetch_array($adnew,MYSQLI_ASSOC))
		{
			$ads=array();
			$ads['id']=(int)$adrecord['id'];
			$ads['version_code']=$adrecord['version_code'];
			$ads['appid']=$adrecord['appid'];

			$ads['gbanner']=$adrecord['gbanner'];
			$ads['ginter']=$adrecord['ginter'];
			$ads['greward']=$adrecord['greward'];
			$ads['gnative']=$adrecord['gnative'];

			$ads['fbanner']=$adrecord['fbanner'];
			$ads['finter']=$adrecord['finter'];
			$ads['fnative']=$adrecord['fnative'];


			$ads['adsBanner']=$adrecord['adsBanner'];
			$ads['adsInterstitial']=$adrecord['adsInterstitial'];
			$ads['adsNative']=$adrecord['adsNative'];
			$ads['adsRewarded']=$adrecord['adsRewarded'];
			
			array_push($response['ads_data'],$ads);
		}


}else{
	$response['flag']=false;
	$response['message']="Failure";
	$response['code']= 0;
	
}

echo json_encode($response);

?>