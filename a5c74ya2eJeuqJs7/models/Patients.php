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
		
	function check_Patient($first, $last, $dat){
		$query = $this->db->query("DECLARE @p_date DATE  SET @p_date = CONVERT( DATE, '$dat')  SELECT * FROM ultrasound.Patients WHERE FirstName = '$first' 
		AND LastName = '$last' AND Birthdate = @p_date");
		$out = array(
				'result' => 0);
		if (count($query->result()) > 0){
			$out['result'] = 1;
			return json_encode($out);
		} else {
			return json_encode($out);
		}
	 }
	 
	 function insert_Patient($first, $last, $date, $country){
		$da = array(
		'FirstName'=> $first,
		'LastName'=> $last,
		'Country' => $country,
		'Birthdate' => date('d/m/Y', strtotime($date)));
		$this->db->insert('ultrasound.Patients',$da);
		$out = array( 'result' => 1);
		return json_encode($out);
		
		
	}
}

?>
