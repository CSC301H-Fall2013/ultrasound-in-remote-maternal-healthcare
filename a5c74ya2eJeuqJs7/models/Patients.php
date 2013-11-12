<?php
class Patients extends CI_Model
{
	

	function __construct()
	{
		parent::__construct();
	}

	function get_Patients(){
		$query = $this->db->query("SELECT * FROM ultrasound.Patients");
		return $query->result(); }
		
	function check_Patient($first, $last){
		$query = $this->db->query("SELECT * FROM ultrasound.Patients WHERE FirstName = $first AND LastName = $last");
		if (count($query->result()) > 0){
			return 1;
		} else {
			return 0;
		}
	 }
		
}

?>