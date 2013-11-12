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
				&& isset($_REQUEST["birthdate"])) {
			$this->load->database();
			$this->load->model("Patients");
			$first = $_REQUEST["firstName"];
			$last =  $_REQUEST["lastName"];
			$dat = $_REQUEST["birthdate"];
			$country = "Nepal";
			echo "Yo!";
		} else {
			echo "nothing set!";
		}
	}
		
}


