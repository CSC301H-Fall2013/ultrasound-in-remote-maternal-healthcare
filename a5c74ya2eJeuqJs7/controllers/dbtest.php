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
		$data['patients'] = $this->Patients->get_Patients();
		$this->load->view("dbview.php", $data);
		echo "Dbtest file!";
		
	}
}


