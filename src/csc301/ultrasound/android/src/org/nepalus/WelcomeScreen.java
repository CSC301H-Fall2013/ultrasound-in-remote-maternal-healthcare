package org.nepalus;

import  com.microsoft.windowsazure.mobileservices.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;



public class WelcomeScreen extends Activity {
	Context area;
	String lastID;
	
	private MobileServiceClient mClient;
	/*
	 * Method gets called when activity is launched.
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 * @param savedInstanceState: saved instance of this activity to extract previous settings.
	 */
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		// Set the context
		area = WelcomeScreen.this;
		// Set display content
		setContentView(R.layout.welcome);
		
		
		
		// Display the user name entered at login screen.
		TextView userName = (TextView) findViewById(R.id.textView1);
		String name = LoginScreen.loginName;
		
		
		
		
	}
	
	
	/*
	 * Gets Executed when application is created or resumed after being paused.
	 * @see android.app.Activity#onResume()
	 */
	 @Override
	protected void onResume() {
		super.onResume();
		// read number of patients registered till now.
		lastID = readFromFile("Totalrecord.txt");
	}
	
	 /*
	  * Handles Add patient record button click event on Welcome menu screen.
	  * @param View: takes in view of the button that was tapped.
	  */
	public void onClick(View v){
		System.out.println(lastID);
		System.out.println(UltrasoundImageScreen.photoID);
		
		// increment the value of photo id for next picture to be clicked.
		if(lastID.length() != 1){
			UltrasoundImageScreen.photoID += 1;
		} else if(lastID.length() == 1) {
			UltrasoundImageScreen.photoID = Integer.valueOf(lastID) + 1;
		}
		System.out.println(lastID);
		System.out.println(UltrasoundImageScreen.photoID);
		//UltrasoundImageScreen.photoID += 1;
		// Initialize the new picture name for next click.
		UltrasoundImageScreen.IMAGE_NAME = "p"+UltrasoundImageScreen.photoID.toString();
		
		// Signal to activate the camera
		UltrasoundImageScreen.cam = true;
		// Launch the Ultrasound Image capturing activity
		Intent i = new Intent(area, UltrasoundImageScreen.class);
		startActivity(i);
	}
	
	
	
	
	 /*
	  * Handles Access patient record button click event on Welcome menu screen.
	  * @param View: takes in view of the button that was tapped.
	  */
	public void onClick1(View v){
		//Laucn Pateint record activity
		Intent i = new Intent(area, patientRecords.class);
		startActivity(i);
		
						
					
		}
	
	
	
	 /*
	  * Handles clear patient record button click event on Welcome menu screen.
	  * @param View: takes in view of the button that was tapped.
	  */
	public void onClick2(View v){
		
		
		//Build the path to file which stores patient records
		File a = getFilesDir();
		String path = a.getAbsolutePath();
		
		
		File b = new File(path +"/patientDataU.txt");
		
		// Delete the file
		boolean deleted = b.delete();
		writeToFile2("0", "Totalrecord.txt");
		//Display message based on delete status
		if(deleted){
			Toast.makeText(WelcomeScreen.this, "Patients Data Cleared!", Toast.LENGTH_SHORT).show();
		} else{
			Toast.makeText(WelcomeScreen.this, "Patients Data Already Cleared!", Toast.LENGTH_SHORT).show();
		}
    	
	}
	
	
	
	/*
	 * Read patient data from filename stored locally.
	 * @param Filename: name of the file from which data needs to be read.
	 * @return Output: return the data read from file in string form.
	 */
	private String readFromFile(String Filename) {
		
	    String ret = ""; // to store string string value read from file

	    try {
	    	// get Input stream from file.
	        InputStream inputStream = openFileInput(Filename);

	        if ( inputStream != null ) {
	        	// Build a reader from input stream
	            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
	            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
	            
	            String receiveString = "";
	            StringBuilder stringBuilder = new StringBuilder();
	            
	            // read entire file
	            while ( (receiveString = bufferedReader.readLine()) != null ) {
	            	// Keep appending all the read code.
	                stringBuilder.append(receiveString);
	            }
	            // Close the input stream.
	            inputStream.close();
	            //Get String val from string builder
	            ret = stringBuilder.toString();
	        }
	    }
	    catch (FileNotFoundException e) {
	        Log.e("login activity", "File not found: " + e.toString());
	    } catch (IOException e) {
	        Log.e("login activity", "Can not read file: " + e.toString());
	    }
	    // return read string.
	    return ret;
	}
	
	/*
	 * Write data to file locally.
	 * @param data: holds data to be written to file
	 * @param Filename: holds name of the file to which data needs to be written.
	 */
	 private void writeToFile2(String data, String Filename) {
		    try {
		    	
		        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(Filename, Context.MODE_PRIVATE));
		        outputStreamWriter.write(data);
		        outputStreamWriter.close();
		    }
		    catch (IOException e) {
		        Log.e("Exception", "File write failed: " + e.toString());
		    } 
		}
}
