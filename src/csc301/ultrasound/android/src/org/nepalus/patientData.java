package org.nepalus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

public class patientData extends Activity {
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.metadata2);
		Intent act = getIntent();
        String Title = getIntent().getStringExtra("Title");
		String age = act.getStringExtra("age");
		String coments = act.getStringExtra("comments");
		String iD = act.getStringExtra("ID");
		String imageId = "p" + iD;
		
		ImageView thumbnail = (ImageView) findViewById(R.id.ultrasound_thumbnail2);
        thumbnail.setImageURI(UltrasoundImageScreen.getOutputMediaFileUri(imageId));
        
        EditText ageText = (EditText) findViewById(R.id.patient_age2);
        EditText nameText = (EditText) findViewById(R.id.patient_name2);
        EditText comText = (EditText) findViewById(R.id.comments2);
        ageText.setText(age);
        nameText.setText(Title);
        comText.setText(coments);
		
	}
}
