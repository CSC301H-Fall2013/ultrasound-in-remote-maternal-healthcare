package org.nepalus;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MetadataScreen extends Activity {
	private Activity mActivity;
	private EditText mPatientName;
	private EditText mPatientAge;
	private EditText mPatientGestationAge;
	private EditText mComments;
	
    private NepalUltrasoundAPI api = new NepalUltrasoundSender();
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.metadata);
        
        mActivity = this;
        mPatientName = (EditText) findViewById(R.id.patient_name);
        mPatientAge = (EditText) findViewById(R.id.patient_age);
        mPatientGestationAge = (EditText) findViewById(R.id.patient_gestation_age);
        mComments = (EditText) findViewById(R.id.comments);
        
        

        ImageView thumbnail = (ImageView) findViewById(R.id.ultrasound_thumbnail);
        thumbnail.setImageURI(UltrasoundImageScreen.getOutputMediaFileUri(UltrasoundImageScreen.IMAGE_NAME));
        
        Button nextButton = (Button) findViewById(R.id.finish);
        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	//submit();
            	String name = mPatientName.getText().toString();
                if(name.length() == 0){
                	name = "NoName";
                }
                String age = mPatientGestationAge.getText().toString();
                if(age.length() == 0){
                	age = "0";
                }
                String comments = mComments.getText().toString();
                if(comments.length() == 0){
                	comments = "NoComments";
                }
            	String data = UltrasoundImageScreen.photoID.toString() +" "+ name +":"+ age +":"+ comments +";";
            	writeToFile(data,"patientDataU.txt");
            	writeToFile2(UltrasoundImageScreen.photoID.toString(),"Totalrecord.txt");
            	final ProgressDialog dialog = ProgressDialog.show(MetadataScreen.this,"Uploading Data", "Please Wait...", true);
                new Thread(new Runnable(){
                	public void run(){
                		try{
                			long initial = System.currentTimeMillis();
                			long cur = System.currentTimeMillis();
                			while((cur - initial) < 5000){
                				cur = System.currentTimeMillis();
                			}
                			
                			dialog.dismiss();
                			//Toast.makeText(MetadataScreen.this, "Data Sent Successfully!", Toast.LENGTH_SHORT).show();
                			Log.d("alertD", "abt to show Dialog");
                			MetadataScreen.this.runOnUiThread(new Runnable(){
                        		public void run(){
                        			Toast.makeText(mActivity, "Data Sent Successfully!", Toast.LENGTH_LONG).show();
                                	
                                	AlertDialog.Builder alDialog = new AlertDialog.Builder(mActivity);
                                	alDialog.setTitle("Response From Radiologist");
                                	alDialog
                                		.setMessage("Child Condition is abnormal, Patient Needs to be Hospitalized Immediately.!")
                                		.setPositiveButton("Okay!", new DialogInterface.OnClickListener() {
                							
                							@Override
                							public void onClick(DialogInterface arg0, int arg1) {
                								Toast.makeText(MetadataScreen.this, "Response Recorded!", Toast.LENGTH_SHORT).show();
                								dialog.cancel();
                								
                								
                							}
                						});
                                	AlertDialog alertD = alDialog.create();
                                	alertD.show();
                                	
                        		}
                        });
                			
                		} catch (Exception e){
                			e.printStackTrace();
                		}
                	}
                }).start();
                
                
                	
                
                
               // mActivity.moveTaskToBack(true);
                
            }
        });
    }
    
    
    private void writeToFile(String data, String Filename) {
	    try {
	        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(Filename, Context.MODE_APPEND));
	        outputStreamWriter.append(data);
	        outputStreamWriter.close();
	    }
	    catch (IOException e) {
	        Log.e("Exception", "File write failed: " + e.toString());
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
    
    private void submit(){    	    	
    	final String patientName = mPatientName.getText().toString();    	
    	final String patientAge = mPatientAge.getText().toString();
    	final String comments = mComments.getText().toString();
    	BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
    	final Bitmap photo;
    	
    	bitmapOptions.outMimeType = "image/png";
    	
    	try {
	        URL aURL = new URL(UltrasoundImageScreen.getOutputMediaFileUri(UltrasoundImageScreen.IMAGE_NAME).toString());
	        URLConnection conn = aURL.openConnection();
	        conn.connect();
	        InputStream is = conn.getInputStream();
	        BufferedInputStream bis = new BufferedInputStream(is);
	        photo = BitmapFactory.decodeStream(bis);
	        bis.close();
	        is.close();

	        new Thread(new Runnable() {
	            public void run() {
	            	try {
	            		api.setURL("http://192.168.100.245:3000/send");
						HttpResponse response = api.send(patientName, comments, photo, patientAge);
						Log.d("API", "sent");											
					} catch (ClientProtocolException e) {
						Log.d("API", e.getMessage());						
					} catch (IOException e) {
						Log.d("API", e.getMessage());
					}    
	            }
	          }).start();   
    	}
    	catch (IOException e) {
    		Log.d("loadBitmap", e.getMessage());
    	}
    	
    }
}
