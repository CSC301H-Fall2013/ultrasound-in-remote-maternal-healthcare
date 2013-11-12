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
		if (isset($_REQUEST["pid"]) && isset($_REQUEST["fcomments"])
				&& isset($_REQUEST["img"]) && isset($_REQUEST["gest"])
				&& isset($_REQUEST["isbleed"]) && isset($_REQUEST["preb"])
				&& isset($_REQUEST["diamFet"]) && isset($_REQUEST["diamot"])
				&& isset($_REQUEST["fseen"])  ){
			$this->load->database();
			$this->load->model("Patients");
			$pid = $_REQUEST["pid"];
			$fcomments =  $_REQUEST["fcomments"];
			$img = $_REQUEST["img"];
			$gest = $_REQUEST["gest"];
			$isbleed = $_REQUEST["isbleed"];
			$preb =  $_REQUEST["preb"];
			$diamFet =  $_REQUEST["diamFet"];
			$diamot =  $_REQUEST["diamot"];
			$fseen =  $_REQUEST["fseen"];
			echo $this->Patients->insert_Patient_Med($pid, $fcomments, $img, $preb, $gest, $isbleed, $diamFet, $diamot, $fseen);
		} else {
			echo "nothing set!";
		}
	}
	
	
		
}


