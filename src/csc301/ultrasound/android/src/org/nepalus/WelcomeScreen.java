package org.nepalus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


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
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		area = WelcomeScreen.this;
		setContentView(R.layout.welcome);
		TextView userName = (TextView) findViewById(R.id.textView1);
		String name = LoginScreen.loginName;
		if(LoginScreen.loginName.length() > 0){
			userName.setText("Welcome " + name + "!");
		}
		
		
		
	}
	
	private String readFromFile(String Filename) {

	    String ret = "";

	    try {
	        InputStream inputStream = openFileInput(Filename);

	        if ( inputStream != null ) {
	            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
	            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
	            String receiveString = "";
	            StringBuilder stringBuilder = new StringBuilder();

	            while ( (receiveString = bufferedReader.readLine()) != null ) {
	                stringBuilder.append(receiveString);
	            }

	            inputStream.close();
	            ret = stringBuilder.toString();
	        }
	    }
	    catch (FileNotFoundException e) {
	        Log.e("login activity", "File not found: " + e.toString());
	    } catch (IOException e) {
	        Log.e("login activity", "Can not read file: " + e.toString());
	    }

	    return ret;
	}
	
	 @Override
	protected void onResume() {
		super.onResume();
		lastID = readFromFile("Totalrecord.txt");
	}
	
	public void onClick(View v){
		System.out.println(lastID);
		System.out.println(UltrasoundImageScreen.photoID);
		
		if(lastID.length() != 1){
			UltrasoundImageScreen.photoID += 1;
		} else if(lastID.length() == 1) {
			UltrasoundImageScreen.photoID = Integer.valueOf(lastID) + 1;
		}
		System.out.println(lastID);
		System.out.println(UltrasoundImageScreen.photoID);
		//UltrasoundImageScreen.photoID += 1;
		UltrasoundImageScreen.IMAGE_NAME = "p"+UltrasoundImageScreen.photoID.toString();
		UltrasoundImageScreen.cam = true;
		Intent i = new Intent(area, UltrasoundImageScreen.class);
		startActivity(i);
	}
	
	public void onClick1(View v){
		
		Intent i = new Intent(area, patientRecords.class);
		startActivity(i);
	}
	
	public void onClick2(View v){
		
		File a = getFilesDir();
		String path = a.getAbsolutePath();
		
		
		File b = new File(path +"/patientDataU.txt");
		writeToFile2("0", "Totalrecord.txt");
		boolean deleted = b.delete();
		if(deleted){
			Toast.makeText(WelcomeScreen.this, "Patients Data Cleared!", Toast.LENGTH_SHORT).show();
		} else{
			Toast.makeText(WelcomeScreen.this, "Patients Data Already Cleared!", Toast.LENGTH_SHORT).show();
		}
    	
	}
	
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
