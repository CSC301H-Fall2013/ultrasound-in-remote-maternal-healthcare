package org.nepalus;

import  com.microsoft.windowsazure.mobileservices.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;


import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;



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
	private MobileServiceClient mClient;
	
	//Class To represent Patients object to be added to remote database.
	public class Patients { 
		
		public int Id; 
		
		public String FirstName;
		public String LastName;
		public int Age;
		public String Gender;
		}
	
	
    private NepalUltrasoundAPI api = new NepalUltrasoundSender();
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the display content
        setContentView(R.layout.metadata);
        
        // Get Editable text fields from display.
        mActivity = this;
        mPatientName = (EditText) findViewById(R.id.patient_name);
        mPatientAge = (EditText) findViewById(R.id.patient_age);
        mPatientGestationAge = (EditText) findViewById(R.id.patient_gestation_age);
        mComments = (EditText) findViewById(R.id.comments);
        
        try {
			mClient = new MobileServiceClient( "https://ultrasound.azure-mobile.net/", "uPQqAbRBcGKnSurAaFbXGKkYylpTAK93", this );
			Toast.makeText(MetadataScreen.this, mClient.getAppKey(), Toast.LENGTH_SHORT).show();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			Toast.makeText(MetadataScreen.this, "No client connection.", Toast.LENGTH_SHORT).show();
		} 
        
        // Get Image object from display
        ImageView thumbnail = (ImageView) findViewById(R.id.ultrasound_thumbnail);
        // Set the image to be displayed in image box via image object.
        thumbnail.setImageURI(UltrasoundImageScreen.getOutputMediaFileUri(UltrasoundImageScreen.IMAGE_NAME));
        
        // Handle the on click event of finish button
        Button nextButton = (Button) findViewById(R.id.finish);
        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	//submit();
            	// Extract Patient information entered by user
            	
            	final Patients pat = new Patients(); 
            	pat.FirstName = "";
            	pat.LastName = "";
            	pat.Gender = "F";
    			
            	String name = mPatientName.getText().toString();
                if(name.length() == 0){
                	name = "NoName";
               
                } else{
                	String[] tokens =  name.split(" ");
                	
                	pat.FirstName = tokens[0];
                	if(tokens.length > 1){
                		pat.LastName = name.split(" ")[1];
                	}
                }
                String age = mPatientGestationAge.getText().toString();
                if(age.length() == 0){
                	age = "0";
                } else{
                	pat.Age = new Integer(age);
                }
                String comments = mComments.getText().toString();
                if(comments.length() == 0){
                	comments = "NoComments";
                }
                // Build string data representing patient informaiton.
            	String data = UltrasoundImageScreen.photoID.toString() +" "+ pat.FirstName +"."+pat.LastName +":"+ age +":"+ comments +";";
            	// Write the new patient data to patient record file
            	File a = getFilesDir();
            	String path = a.getAbsolutePath();
        		
        		
        		File b = new File(path +"/patientDataU.txt");
        		if(b.exists()){
        			writeToFile(data,"patientDataU.txt");
        		} else{
        			writeToFile2(data,"patientDataU.txt");
        		}
            	
            	// Record the last Patient Id
            	writeToFile2(UltrasoundImageScreen.photoID.toString(),"Totalrecord.txt");
            	
            	// Display Progress Dialog of uploading information.
            	final ProgressDialog dialog = ProgressDialog.show(MetadataScreen.this,"Uploading Data", "Please Wait...", true);
                new Thread(new Runnable(){
                	public void run(){
                		try{
                			long initial = System.currentTimeMillis();
                			long cur = System.currentTimeMillis();
                			while((cur - initial) < 5000){
                				cur = System.currentTimeMillis();
                			}
                			
                			
                			if((pat.FirstName.length() > 0) || (pat.LastName.length() > 0)){
                				//Insert Patient Details into remote database Patients table
                				mClient.getTable(Patients.class).insert(pat,
                					new TableOperationCallback<Patients>() { 
                						public void onCompleted(Patients entity, Exception exception, ServiceFilterResponse response) {
                							if (exception == null) { 
                								// Insertion succeeded.
                								Toast.makeText(MetadataScreen.this, "Data sent successfully", Toast.LENGTH_SHORT).show();
                								} else { 
                									//Insertion Failed.
                									Toast.makeText(MetadataScreen.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                									} } });
                			} else{
                				Toast.makeText(MetadataScreen.this, "Full Details not provided.", Toast.LENGTH_SHORT).show();
                			}
                			
                			dialog.dismiss();
                			//Toast.makeText(MetadataScreen.this, "Data Sent Successfully!", Toast.LENGTH_SHORT).show();
                			Log.d("alertD", "abt to show Dialog");
                			
                			
                			/* CODE TO BE USED LATER. */
                			// Display the mock of Response from Radiologist
                			/*MetadataScreen.this.runOnUiThread(new Runnable(){
                        		public void run(){
                        			
                                	
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
                        });*/
                			
                		} catch (Exception e){
                			e.printStackTrace();
                		}
                	}
                }).start();
                
               // mActivity.moveTaskToBack(true);
                
            }
        });
    }
    
    /*
     * Append data to file
     * @param data: data that needs to be written to file
     * @param Filename: name of the file to which data needs to be written to.
     */
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
    /*
     * OverWrite data to file
     * @param data: data that needs to be written to file
     * @param Filename: name of the file to which data needs to be written to.
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
    
    
    //Below code is not required at the moment.
   /* private void submit(){    	    	
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
    	
    }*/
}
