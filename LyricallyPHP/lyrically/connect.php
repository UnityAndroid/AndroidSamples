<?php

ini_set('display_errors', TRUE);
session_start();
ob_start();
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET, POST, PATCH, PUT, DELETE, OPTIONS');
header('Access-Control-Allow-Headers: Origin, Content-Type, X-Auth-Token');
$con = new mysqli("localhost","recrrygy_inoventic","123123","recrrygy_lyrically");
   
?>
	