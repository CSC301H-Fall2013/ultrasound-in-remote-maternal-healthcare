package org.nepalus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;


public class patientData extends Activity {
	
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
		String age = act.getStringExtra("age");
		String coments = act.getStringExtra("comments");
		String iD = act.getStringExtra("ID");
		String imageId = "p" + iD;
		
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
        ageText.setText(age);
        nameText.setText(Title);
        comText.setText(coments);
		
	}
}
