package org.nepalus;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


public class patientData extends Activity {
	
	/*Class to store details during multiple threads run.*/
	class info{
		public int pid = -1;
		public String response = null;
	}
	
	final info details = new info();
	
	/* Class to display messages to user during multiple threads run.*/
	private class TransferTask extends AsyncTask<String, Void, String>{
		protected String doInBackground(String... urls) {
			return urls[0];
		}
		
		@Override
		protected void onPostExecute(String result) {
			Toast.makeText(patientData.this, result, Toast.LENGTH_SHORT).show();
		}
		
	}
	
	/*Update Details on Main UI, while a thread retrieves information from database in background. */
	private class updateTask extends AsyncTask<JSONObject, Void, JSONObject>{
		/* Execute this function when execute is called on this class's instance.
		 * @params jsonObjects : Array of jsonObjects. 
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		protected JSONObject doInBackground(JSONObject...jsonObjects) {
			return jsonObjects[0];
		}
		
		@Override
		/* After Do in Background is finished. Execute this function.
		 * @params result : Instance of JSON holding data from database.
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		protected void onPostExecute(JSONObject result) {
			try{
				
			/* Update the UI with details from database. */
			EditText gest = (EditText) findViewById(R.id.patient_gestation_age2);
			gest.setText(result.getString("Gestation"));
			EditText comm = (EditText) findViewById(R.id.comments2);
			comm.setText(result.getString("FieldworkerComments"));
			EditText fet = (EditText) findViewById(R.id.fetal_head_diameter2);
			fet.setText(result.getString("DiameterFetalHead"));
			EditText mot = (EditText) findViewById(R.id.mother_hip_diameter2);
			mot.setText(result.getString("DiameterMotherHip"));
			EditText isb = (EditText) findViewById(R.id.is_bleeding2);
			isb.setText(result.get("IsBleeding").toString());
			EditText preb = (EditText) findViewById(R.id.pre_birth2);
			preb.setText(result.get("Prebirth").toString());
			
			/* Present the response from Radiologist to fieldworker.*/
			if(result.getString("RadiologistResponse") != null){
			new AlertDialog.Builder(patientData.this)
		    .setTitle("Response From Radiologist")
		    .setMessage(result.getString("RadiologistResponse"))
		    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		            //DO nothing
		        }
		     }).show();
			}
			} catch (Exception e){
				//Exception occured. Write to console.
				System.out.println("Something wrong with json update object");
			}
			
		}
		
	}
	
	/*
	 * Method gets called when activity is first created.
	 * @params savedInstanceState : saved Instance of the activity is passed if it exists.
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		// Set the content to be displayed.
		setContentView(R.layout.metadata2);
		// get the intent object to extract passed information
		Intent act = getIntent();
		// Below block extracts information passed on from previous activity.
        String Title = getIntent().getStringExtra("Title");
		final String age = act.getStringExtra("age");
		String coments = act.getStringExtra("comments");
		String iD = act.getStringExtra("ID");
		String imageId = "p" + iD;
		final String name = Title.replace(".", " ");
		
		
		/* Display a Progress Dialog to indicate information grab from database 
		 * about specific patient.
		 */
		final ProgressDialog dialog = ProgressDialog.show(patientData.this,"Downloaing Data", "Please Wait...", true);
        new Thread(new Runnable(){
        	public void run(){
        		try{
        				//Extract Name and Birthdate.
        				String message = "";
        				HttpClient conn = new HttpClient();
        				String FirstName = "";
        				String LastName = "";
        				String[] nameTok = name.split(" ");
        				if(nameTok.length > 1){
        		;			FirstName = nameTok[0];
        					LastName= nameTok[1];
        				} else{
        					FirstName = name;
        					LastName = "";
        				}
        				String bday = age.replace(".", " ");
        				
        				//Build HTTP request to call PHP Web service controllers.
        				List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
        				nameValuePairs.add(new BasicNameValuePair("firstName", FirstName));
                	    nameValuePairs.add(new BasicNameValuePair("lastName", LastName));
                	    nameValuePairs.add(new BasicNameValuePair("date", bday));
        				JSONObject output = conn.SendHttpPost("http://ultrasound.azurewebsites.net/index.php/dbtest/callPatientCheck", nameValuePairs);
        				
        				// Check if http request failed or not.
        				if(output != null){
        					int pid = output.getInt("result");
        					// Passed, Store pid of patient.
        					details.pid = pid;
        					if(pid == -1){
        						
        						// Patient Non - existent.
        						message = "Patient Details does not exist in database";
        						new TransferTask().execute(message);
        						
        					} else {
        						// Patient Exists.
        						// Prepare to extract Patient's Medical Info from DB.
        						List<BasicNameValuePair> nameValues = new ArrayList<BasicNameValuePair>();
                				nameValues.add(new BasicNameValuePair("piD", String.valueOf(pid)));
                				output = conn.SendHttpPost("http://ultrasound.azurewebsites.net/index.php/dbtest/getPatientMed", nameValues);
                				
                				if(output != null){
                					//Get Response from radiologist for patient
                					details.response = output.getString("RadiologistResponse");
                					
                					new updateTask().execute(output);
                				} else{
                					message = "Patient Medical Record cannot be retrieved or not present.";
            						new TransferTask().execute(message);
                				}
        					
        					}
        				} else {
        					System.out.println("Null Json.");
        				}
        				
        				
      
        			
        			
        			dialog.dismiss();
        			//Toast.makeText(MetadataScreen.this, "Data Sent Successfully!", Toast.LENGTH_SHORT).show();
        			Log.d("alertD", "abt to show Dialog");
        			
        			
        		} catch (Exception e){
        			e.printStackTrace();
        		}
        	}
        }).start();
		
		
		// Get the Image holding block from display
		ImageView thumbnail = (ImageView) findViewById(R.id.ultrasound_thumbnail2);
		// Set the image to be displayed in the block.
        thumbnail.setImageURI(UltrasoundImageScreen.getOutputMediaFileUri(imageId));
        
        // Get the Text Fields blocks from display
        EditText ageText = (EditText) findViewById(R.id.patient_age2);
        EditText nameText = (EditText) findViewById(R.id.patient_name2);
        EditText comText = (EditText) findViewById(R.id.comments2);
        
        // Add Information into text blocks to be displayed.
        // Information is what we extract from Intent object
       
        System.out.println(age);
        String bday = age.replace(".", " ");
   
        
        //String bday = ageTokens[0] + " " + ageTokens[1] + " " + ageTokens[2];
        ageText.setText(bday);
        nameText.setText(Title);
        comText.setText(coments);
		
	}
	
	/*Called when user clicks on get response.
	 * @params v : View in which button was clicked.
	 */
	public void onClick3(View v){
						//Display message received from radiologist
        				new AlertDialog.Builder(patientData.this)
        				.setTitle("Response From Radiologist")
        				.setMessage(getResponse())
        				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
        					public void onClick(DialogInterface dialog, int which) { 
	            //DO nothing
        					}
        				}).show();
        		
	}
	
	/* Get response from radiologist. */
	public String getResponse(){
		//To be implemented for persistent checking. One time checking is done in beginning.
    	return "No new Update From Radiologist";
       
        
 
	}
}
