<?php if (!defined('BASEPATH')) exit('No direct script access allowed');

class Dbtest extends CI_Controller
{
	function __construct()
	{
		parent::__construct();

		}

	function index()
	{
		$this->load->database();
		$this->load->model("Patients");
		//$data['patients'] = $this->Patients->get_Patients();
		$results = $this->Patients->get_Patients();
		echo json_encode($results[0]);
		//$this->load->view("dbview.php", $data);
		
		
	}
	
	function callPatientCheck(){
		if (isset($_REQUEST["firstName"]) && isset($_REQUEST["lastName"])
				&& isset($_REQUEST["date"])) {
			$this->load->database();
			$this->load->model("Patients");
			$first = $_REQUEST["firstName"];
			$last =  $_REQUEST["lastName"];
			$dat = $_REQUEST["date"];
			echo $this->Patients->check_Patient($first, $last, $dat);
		} else {
			echo "nothing set!";
		}
	}
	
	
	function insertPatient(){
		if (isset($_REQUEST["firstName"]) && isset($_REQUEST["lastName"])
				&& isset($_REQUEST["date"])) {
			$this->load->database();
			$this->load->model("Patients");
			$first = $_REQUEST["firstName"];
			$last =  $_REQUEST["lastName"];
			$dat = $_REQUEST["date"];
			$country = "Nepal";
			echo $this->Patients->insert_Patient($first, $last, $dat, $country);
		} else {
			echo "nothing set!";
		}
	}
	
	function insertPatientMed(){
		
			$this->load->database();
			$this->load->model("Patients");
			$pid = $_REQUEST["piD"];
			$fcomments =  $_REQUEST["fComments"];
			$img = $_REQUEST["imG"];
			$binary = base64_decode($img);
			file_put_contents('newImage.PNG',$binary);
			$gest = $_REQUEST["gesT"];
			$isbleed = $_REQUEST["isBleed"];
			$preb =  $_REQUEST["preB"];
			$diamFet =  $_REQUEST["diamFet"];
			$diamot =  $_REQUEST["diaMot"];
			$fseen =  $_REQUEST["fSeen"];
			echo $this->Patients->insert_Patient_Med($pid, $fcomments, $binary, $preb, $gest, $isbleed, $diamFet, $diamot, $fseen);
		 
	}
	
	function getPatientMed(){
		if(isset($_REQUEST["piD"])){
			$this->load->database();
			$this->load->model("Patients");
			$pid = $_REQUEST["piD"];
			echo $this->Patients->get_Patient_Med($pid);
		} else{
			echo "nothing set!";
		}
	}
	
	function upload(){
    	
    		$file_path = "";
    		$this->load->database();
    		$file_path = $file_path . basename( $_FILES['uploaded_file']['name']);
    		if(move_uploaded_file($_FILES['uploaded_file']['tmp_name'], $file_path)) {
    			/***  get the image info. ***/
    			$size = getimagesize($file_path);
    			/*** assign our variables ***/
    			$type = $size['mime'];
    			$imgfp = fopen($file_path, 'rb');
    			$size = $size[3];
    			$name = $_FILES['uploaded_file']['name'];
    			$maxsize = 99999999;
    		
    			//$stmt = "INSERT INTO testblob (image_type ,image, image_size, image_name) VALUES (? ,?, ?, ?)";
    			//$this->db->query($stmt, array($type, $imgfp, $size, $name));
    		 	/*** connect to db ***/
       			$dbh = new PDO("sqlsrv:host=ze7duqnsz2.database.windows.net,1433;dbname=ultrasound", 'ultrasound', 'csc301-erie');

                	/*** set the error mode ***/
        		$dbh->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
            

            		/*** our sql query ***/
            		$stmt = $dbh->prepare("INSERT INTO ultrasound.testimage (image_type ,image, image_size, image_name) VALUES (? ,?, ?, ?)");

            		/*** bind the params ***/
            		$stmt->bindParam(1, $type);
            		$stmt->bindParam(2, $imgfp, PDO::PARAM_LOB);
            		$stmt->bindParam(3, $size);
            		$stmt->bindParam(4, $name);
            		/*** execute the query ***/
        		//$stmt->execute();
    		
      			//  $stmt->bindParam(2, $imgfp, PDO::PARAM_LOB);
     
	 		echo "success";
    		} else{
        		echo "fail";
    		}
	}
	
	
	

}
