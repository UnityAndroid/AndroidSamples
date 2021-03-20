<?php
include 'connect.php';


$response=array();
$response['data']=array();
$response['ads']=array();

$ads=mysqli_query($con,"select * from app_info");
while($ad=mysqli_fetch_array($ads,MYSQLI_ASSOC))
{
	$data=array();
	$data['id']=$ad['id'];
	$data['message']=$ad['message'];
	$data['status']=$ad['status'];
	$data['url']=$ad['url'];
	$data['version_code']=$ad['version_code'];
	$data['appid']=$ad['appid'];
	$data['banner']=$ad['banner'];
	$data['inter']=$ad['inter'];
	$data['reward']=$ad['reward'];
	$data['gnative']=$ad['native'];
	$data['fbanner']=$ad['fbanner'];
	$data['finter']=$ad['finter'];
	$data['fnative']=$ad['fnative'];
	$data['gstatus']=$ad['gstatus'];
	$data['fstatus']=$ad['fstatus'];
	$data['g2status']=$ad['g2status'];
	$data['f2status']=$ad['f2status'];


	
	array_push($response['ads'],$data);
	
}


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
		$cat['background']=$record['back_url'];
		$cat['videos']=array();


		$videos=mysqli_query($con,"select * from videos where Cat_Id=".$record['Cat_Id']." order by position asc");
		while($vidrecord=mysqli_fetch_array($videos,MYSQLI_ASSOC))
		{
			$video=array();
			$video['Id']=(int)$vidrecord['Id'];
			$video['Cat_Id']=(int)$vidrecord['Cat_Id'];
			
			$video['Theme_Id']=(int)$vidrecord['Theme_Id'];
			$video['Theme_Name']=$vidrecord['Theme_Name'];
			$video['Thumnail_Big']=$vidrecord['Thumnail_Url'];
			$video['Thumnail_Small']=$vidrecord['Thumnail_Url'];
			$video['SoundName']=$vidrecord['SoundName'];
			$video['SoundFile']=$vidrecord['SoundFile'];
			$video['sound_size']=$vidrecord['sound_size'];
			$video['GameobjectName']=(int)$vidrecord['GameobjectName'];
			$video['Is_Preimum']=(int)$vidrecord['Is_Preimum'];
			$video['Status']=(int)$vidrecord['Status'];
			$video['lyrics']=$vidrecord['lyrics'];
			
			array_push($cat['videos'],$video);
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