package org.nepalus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;




import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;




public class patientRecords extends Activity implements OnClickListener {
	
	// Initialize hash table to hold patient information.
	Hashtable<String, String[]> pData = new Hashtable<String, String[]>();
	
	// initialize list to hold unique patient id's.
	ArrayList<String> pList = new ArrayList<String>();
	
	/*
	 * Method gets called when activity is launched.
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 * @param savedInstanceState: saved instance of this activity to extract previous settings.
	 */
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		System.out.println("Begin");
		//Read Patient Records data from the file stored locally.
		String data = readFromFile("patientDataU.txt");
		System.out.println(data);
		// Parse the data read from file into a hash table.
		pData = parseRecords(data);
		
		
		//Enumeration<String> coursesE = sData.keys();
		Enumeration<String> patientL = pData.keys();
		
		//Dynamically Build the User Interface to display patients as a list.
		// Set the Display and layout settings.
		LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    	LinearLayout layout = new LinearLayout(this);
    	layout.setOrientation(LinearLayout.VERTICAL);
    	// Initialize a text view to be displayed.
    	TextView tv = new TextView(this);
    	tv.setText("Patients");
    	tv.setLayoutParams(params);
    	layout.addView(tv);
    	
    	// Build a list of patient id's from hashtable
    	while(patientL.hasMoreElements()){
    		pList.add(0,patientL.nextElement());
    	}
    	
    	System.out.println(pList.size());
    	
    	
    	/* Iterate over all patient id's and build buttons for each patient
    	  wiith their name as text. */
    	for(String crse: pList){
    		String[] lki = pData.get(crse);
    		Button tmp = new Button(this);
    		tmp.setText(lki[0]);
    		tmp.setTag(new String("Cbtn"));
    		tmp.setId(pList.indexOf(crse));
    		tmp.setLayoutParams(params);
    		tmp.setOnClickListener(this);
    		layout.addView(tmp);
    		
    		for(String abc: lki){
    			System.out.println(abc);
    		}
    	}
    	System.out.println(pList.size());
    	
    	
    	// Turn the display into a scroll view in case patient exceed screen display size.
    	LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(
    			LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    	ScrollView sv = new ScrollView(this);
    	LinearLayout.LayoutParams lP = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    	
    	//sv.setLayoutParams(lP);
    	layout.setLayoutParams(layoutParam);
    	sv.addView(layout);
    	
    	
    	this.addContentView(sv,lP);
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
	 * Parses the string read from file and stores data in hash table with patient ids
	 * as keys.
	 * @param input: it is the string read from file and needs to be parsed.
	 * @return Output: Returns a hash table with patient id as key pointing to string
	 * array containing patient data.
	 */
	private Hashtable<String, String[]> parseRecords(String input){
		// Build a new hash table
		Hashtable<String, String[]> output = new Hashtable<String, String[]>();
		
		// Proceed if input string is not empty.
		if(!input.isEmpty()){
		// Tokenize the input argument
		String[] tokens = input.split(";");
		
		for(String crs: tokens){
			// Extract data from tokens and place in hash table.
			String[] cseDet = crs.split(" ");
			String[] assigns = cseDet[1].split(":");
			output.put(cseDet[0], assigns);
		}
		}
		return output;
	
	}

	
	/*
	 * Handles events of button clicks for individual patients.
	 * @param View b: b stands for button, takes in the View of specific button tapped on screen.
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View b) {
		// Check if the Button represents patient tag
		if(b.getTag().toString().equals("Cbtn")){
			// Get the Title Id for the patient
			String Title = pList.get(b.getId());
			// Initialize new intent to load patient data activity
			Intent load = new Intent(patientRecords.this, patientData.class);
			// get Patient Details
			// Pass patient details to launching intent.
			String[] elements = pData.get(Title);
			load.putExtra("ID", Title);
			load.putExtra("Title", elements[0]);
			load.putExtra("age", elements[1]);
			load.putExtra("comments", elements[2]);
			
			// Launch the intent.
			startActivity(load);
		}
			
		
	}

}
