package org.nepalus;

import  com.microsoft.windowsazure.mobileservices.*;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images;
import android.util.Base64;
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
	private EditText mIsBleeding;
	private EditText fetalDiameter;
	private EditText hipDiameter;
	private EditText patientIdentification;
	private EditText preBirth;
	private MobileServiceClient mClient;
	
	//Class To represent Patients object to be added to remote database.
	public class Patients{ 
		
		public int PID; 
		
		public String FirstName;
		public String LastName;
		public String Country;
		}
	
	public class Records {
		public int RID;
		public int PID;
		public String Date;
		public int Prebirth;
		public int IsBleeding;
		public int FieldworkerSeen;
		public String FieldworkerComments;
		public float DiameterFetalHead;
		public float DiameterMotherHip;
		public int Gestation;
		public String IMGUltrasound;
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
        mIsBleeding = (EditText) findViewById(R.id.is_bleeding);
    	fetalDiameter = (EditText) findViewById(R.id.fetal_head_diameter);
    	hipDiameter = (EditText) findViewById(R.id.mother_hip_diameter);
    	preBirth = (EditText) findViewById(R.id.pre_birth);
        
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
        Uri image = UltrasoundImageScreen.getOutputMediaFileUri(UltrasoundImageScreen.IMAGE_NAME);
        thumbnail.setImageURI(image);
        try {
			Bitmap bmp=BitmapFactory.decodeStream(getContentResolver().openInputStream(image));
			
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        // Handle the on click event of finish button
        Button nextButton = (Button) findViewById(R.id.finish);
        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	//submit();
            	// Extract Patient information entered by user
            	
            	final Patients pat = new Patients(); 
            	pat.FirstName = "";
            	pat.LastName = "";
            	pat.Country = "Nepal";
            	//pat.Birthdate = 20131112;
            	
            	final Records imgRecord = new Records();
            	
    			
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
                String gage = mPatientGestationAge.getText().toString();
                if(gage.length() == 0){
                	gage = "0";
                } else{
                	imgRecord.Gestation = Integer.valueOf(gage);
                }
                
                String page = mPatientAge.getText().toString();
                if(page.length() == 0){
                	page = "0";
                } else{
                	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                	
                }
                String comments = mComments.getText().toString();
                if(comments.length() == 0){
                	comments = "NoComments";
                } else {
                	imgRecord.FieldworkerComments = comments;
                }
                
                String prebirth = preBirth.getText().toString();
                if(prebirth.length() == 0){
                	prebirth = "No Data";
                } else{
                	imgRecord.Prebirth = Integer.valueOf(prebirth);
                }
                String isbleed = mIsBleeding.getText().toString();
                if(isbleed.length() == 0){
                	isbleed = "NoComments";
                } else{
                	imgRecord.IsBleeding = Integer.valueOf(isbleed);
                }
                String fetalDiam = fetalDiameter.getText().toString();
                if(fetalDiam.length() == 0){
                	fetalDiam = "NoComments";
                }else{
                	imgRecord.DiameterFetalHead = Float.valueOf(fetalDiam);
                }
                String hipDiam = hipDiameter.getText().toString();
                if(hipDiam.length() == 0){
                	hipDiam = "NoComments";
                }else{
                	imgRecord.DiameterMotherHip = Float.valueOf(hipDiam);
                }
                
                
                
                
                
                // Build string data representing patient informaiton.
            	String data = UltrasoundImageScreen.photoID.toString() +" "+ pat.FirstName +"."+pat.LastName +":"+ page +":"+ comments +";";
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
                			
              
                			if((pat.FirstName.length() > 0) || (pat.LastName.length() > 0)){
                				//Insert Patient Details into remote database Patients table
                				System.out.println("About to insert Data to database!");
                				System.out.println(mClient.getTable("Users",Patients.class).toString());
                					
                					//Toast.makeText(MetadataScreen.this, "PID: " + Integer.valueOf(imgRecord.PID).toString(), Toast.LENGTH_SHORT).show();
                				
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
    
    public static String encodeTobase64(Bitmap image)
    {
        Bitmap immagex=image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b,Base64.NO_WRAP);

        Log.e("LOOK", imageEncoded);
        return imageEncoded;
    }
    
    public static Bitmap decodeBase64(String input) 
    {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length); 
    }
    
    
    	
    public Uri getImageUri(Context inContext, Bitmap inImage) {
      ByteArrayOutputStream bytes = new ByteArrayOutputStream();
      inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
      String path = Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
      return Uri.parse(path);
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
