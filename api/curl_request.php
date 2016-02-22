<?php


function httpPost($url,$params)
{
  $postData = '';
   //create name value pairs seperated by &
   foreach($params as $k => $v) 
   { 
      $postData .= $k . '='.$v.'&'; 
   }
   $postData = rtrim($postData, '&');
 
    $ch = curl_init();  
 
    curl_setopt($ch,CURLOPT_URL,$url);
    curl_setopt($ch,CURLOPT_RETURNTRANSFER,true);
    curl_setopt($ch,CURLOPT_HEADER, false); 
    curl_setopt($ch, CURLOPT_POST, count($postData));
        curl_setopt($ch, CURLOPT_POSTFIELDS, $postData);    
 
    $output=curl_exec($ch);
 
    curl_close($ch);
    return $output;
 
}
$params = array(
   "neighborRange" => 20,
   "nodeSizeBy" => 30,
   "nodeCentrailityThreashhold"=>  3,
   "selectedLayout"=>"FruchtermanReingold"
);
 //print_r($_POST['options']);
echo httpPost("http://localhost:8182/gephi",$_POST['filters']);
//echo httpGetWithErros("http://localhost:8080/gephi?a=e&b=p");