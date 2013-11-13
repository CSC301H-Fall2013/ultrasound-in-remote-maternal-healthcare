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
		'Birthdate' => date('d/m/Y', strtotime($date)));
		$this->db->insert('ultrasound.Patients',$da);
		$out = array( 'result' => 1);
		return json_encode($out);
		
		
	}
	
	function insert_Patient_Med($pid, $fcomments,$binary, $preb, $gest, $isbleed, $diamfet, $diamot, $fseen){
		$da = array(
			"PID" => intval($pid),
			"Date" => date("Y/m/d H:i:s"),
			"FieldworkerComments" => $fcomments,
			"IMGUltrasound" => $binary,
			"Prebirth" => ($preb == "true"),
			"Gestation" => intval($gest),
			"IsBleeding" => ($isbleed == "true"),
			"DiameterFetalHead" => floatval($diamfet),
			"DiameterMotherHip" => floatval($diamot),
			"FieldworkerSeen" => ($fseen == "true"));
			
			$curdat = date('Y/m/d H:i:s');
		//$this->db->insert("ultrasound.Records", $da);
		$this->db->simple_query("DECLARE @image varbinary(max) SET @image = CONVERT(varbinary(max),$binary)
						INSERT INTO ultrasound.Records (PID, Date, FieldworkerComments, IMGUltrasound,
						Prebirth, Gestation, IsBleeding, DiameterFetalHead, DiameterMotherHip, FieldworkerSeen)
						VALUES (intval($pid),$curdat, $fcomments, @image,  $da->Prebirth,  intval($gest),
						$da->ISBleeding, floatval($diamfet), floatval($diamot), $da->FieldworkerSeen) ");
		$out = array( "result" => 1);
		return json_encode($out);
		
	}
}

?>
