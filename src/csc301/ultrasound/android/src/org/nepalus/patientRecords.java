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
	
	Hashtable<String, String[]> pData = new Hashtable<String, String[]>();
	
	ArrayList<String> pList = new ArrayList<String>();

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		System.out.println("Begin");
		String data = readFromFile("patientDataU.txt");
		System.out.println(data);
		pData = parseRecords(data);
		
		
		//Enumeration<String> coursesE = sData.keys();
		Enumeration<String> patientL = pData.keys();
		
		
		LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    	LinearLayout layout = new LinearLayout(this);
    	layout.setOrientation(LinearLayout.VERTICAL);
    	TextView tv = new TextView(this);
    	tv.setText("Patients");
    	tv.setLayoutParams(params);
    	layout.addView(tv);
    	
    	while(patientL.hasMoreElements()){
    		pList.add(0,patientL.nextElement());
    	}
    	
    	System.out.println(pList.size());
    	
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
    	
    	
    	
    	LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(
    			LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    	ScrollView sv = new ScrollView(this);
    	LinearLayout.LayoutParams lP = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    	
    	//sv.setLayoutParams(lP);
    	layout.setLayoutParams(layoutParam);
    	sv.addView(layout);
    	
    	
    	this.addContentView(sv,lP);
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
	
	private Hashtable<String, String[]> parseRecords(String input){
		Hashtable<String, String[]> output = new Hashtable<String, String[]>();
		if(!input.isEmpty()){
		String[] tokens = input.split(";");
		
		for(String crs: tokens){
			String[] cseDet = crs.split(" ");
			String[] assigns = cseDet[1].split(":");
			output.put(cseDet[0], assigns);
		}
		}
		return output;
	
	}

	@Override
	public void onClick(View b) {
		if(b.getTag().toString().equals("Cbtn")){
			String Title = pList.get(b.getId());
			Intent load = new Intent(patientRecords.this, patientData.class);
			String[] elements = pData.get(Title);
			load.putExtra("ID", Title);
			load.putExtra("Title", elements[0]);
			load.putExtra("age", elements[1]);
			load.putExtra("comments", elements[2]);
			startActivity(load);
		}
			
		
	}

}
