<?php
include 'connect.php';

$category=mysqli_query($con,"select * from apps");

if(mysqli_num_rows($category)>0)
{
	while($record=mysqli_fetch_array($category,MYSQLI_ASSOC))
	{
		$path=$record['logo_path'];
		$path2=$record['logo'];

		$path=str_replace("/home/x9h5da9wh37k/public_html/magically","/home4/recrrygy/inoventic/lyrically",$path);
		$path2=str_replace("http://shreeinfinityinfotech.com/magically","http://inoventic.online/lyrically",$path2);
		
		mysqli_query($con,"update apps set logo_path='".$path."',logo='".$path2."' where id=".$record['id']."");
	


		/*$path=$record['SoundFile'];

		$path=str_replace("http://shreeinfinityinfotech.com/magically","http://inoventic.online/lyrically",$path);
		
		mysqli_query($con,"update videos set SoundFile='".$path."' where Id=".$record['Id']."");
	

	   */

	}
	echo "completed";
}

?>