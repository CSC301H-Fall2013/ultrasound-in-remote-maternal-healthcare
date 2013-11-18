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
				'result' => -1);
		$results = $query->result();
		if (count($results) > 0){
			$out['result'] = $results[0]->PID;
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
		'Birthdate' => date('m/d/Y', strtotime($date)));
		$this->db->insert('ultrasound.Patients',$da);
		$out = array( 'result' => 1);
		return json_encode($out);
		
		
	}
	
	function insert_Patient_Med($pid, $fcomments,$binary, $preb, $gest, $isbleed, $diamfet, $diamot, $fseen){
		
			
			$preBirth = True;
			$isBleeding = False;
			$fSeenIt = False;
			$curdat = date('Y/m/d H:i:s');
			$intpid = intval($pid);
			$intgest = intval($gest);
			$floatfet = floatval($diamfet);
			$floatmot = floatval($diamot);
			$newbinary = base64_decode($binary);
			
		//$this->db->insert("ultrasound.Records", $da);
		//"DECLARE @image varbinary(max) SET @image = CONVERT(varbinary(max),'$newbinary')
		$this->db->query("DECLARE @image varbinary(max) SET @image = CONVERT(varbinary(max),'$binary')
						INSERT INTO ultrasound.Records (PID, Date, FieldworkerComments, IMGUltrasound
						Prebirth, Gestation, IsBleeding, DiameterFetalHead, DiameterMotherHip, FieldworkerSeen)
						VALUES ('$intpid', '$curdat', '$fcomments', @image, '$preBirth', '$intgest',
						'$isBleeding', '$floatfet', '$floatmot', '$preBirth')");
		$out = array( "result" => 1);
		return json_encode($out);
		
	}
	
	function get_Patient_Med($pid){
		$query = $this->db->query("SELECT RadiologistResponse, FieldworkerComments, Gestation, IsBleeding,
					Prebirth, DiameterFetalHead, DiameterMotherHip FROM ultrasound.Records where PID = $pid");
		return json_encode($query->result()[0]); 
		}
		
}

?>
