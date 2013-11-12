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
		if (isset($_REQUEST["firstName"]) && isset($_REQUEST["lastName"])) {
			$this->load->database();
			$this->load->model("Patients");
			$first = $_REQUEST["firstName"];
			$last =  $_REQUEST["lastName"];
			echo $this->Patients->check_Patient($first, $last);
		} else {
			echo "nothing set!";
		}
	}
		
}


