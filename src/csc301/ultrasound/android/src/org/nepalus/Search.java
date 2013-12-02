package org.nepalus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Search extends Activity {
	public static EditText name = null;
	public static EditText birth = null;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		name = (EditText) findViewById(R.id.search_name);
		birth = (EditText) findViewById(R.id.birth);
		
		
	}
	
	/*
	 * Call Patient Data with userinput on User Tap on "Search"
	 */
	public void onClick5(View b){
		Intent load = new Intent(Search.this, patientData.class);
		String name = Search.name.getText().toString();
		String Birth = Search.birth.getText().toString();
		Birth.replace(" ", ".");
		name.replace(" ", ".");
		load.putExtra("Title", name);
		load.putExtra("age", Birth);
		startActivity(load);
	}
}
	

