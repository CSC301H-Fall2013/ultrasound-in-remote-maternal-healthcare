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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;


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
import android.os.AsyncTask;
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
	
	
	
	private class TransferTask extends AsyncTask<String, Void, String>{
		/*Run this function in background when execute is called.
		 * @urls: array of string urls.
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		protected String doInBackground(String... urls) {
			return urls[0];
		}
		
		@Override
		/* Run this after every doInbackground is called.
		 * @result: String returned from doInbackground call.
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		protected void onPostExecute(String result) {
			Toast.makeText(MetadataScreen.this, result, Toast.LENGTH_SHORT).show();
		}
		
	}
	
	/* Convert Bitmap to bytearray.
	 * @b: bitmap to be translated to its bytearray form.*/
	 
	public byte[] bitmapToByteArray(Bitmap b)
	{
	    ByteArrayOutputStream stream = new ByteArrayOutputStream();
	    b.compress(Bitmap.CompressFormat.PNG, 100, stream);
	    byte[] byteArray = stream.toByteArray();
	    return byteArray;
	}
	
	/**
	 * Compress the provided byte array using ZLib. Note that it is not guaranteed that the
	 * resultant byte array will be smaller than the original.
	 *
	 * @param toCompress The byte array to compress
	 * @return The compressed byte array. null if an error occured.
	 */
	public byte[] compress(byte toCompress[])
	{
		try 
		{
			Deflater compressor = new Deflater(Deflater.BEST_COMPRESSION);
			
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			DeflaterOutputStream compressedStream = new DeflaterOutputStream(stream, compressor);
			
			compressedStream.write(toCompress);
			compressedStream.close();
			
			stream.flush();
			
			compressor.end();
			
			return stream.toByteArray();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	//Class To represent Patients object to be added to remote database.
	
	public class Patients{ 
		
		public int PID; 
		
		public String FirstName;
		public String LastName;
		public String Country;
		public String Birthdate;
		public String message;
		}
	

	
	/*Class to repsent Patients medical data.*/
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
		public String image;
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
        
        
        
        // Get Image object from display
        ImageView thumbnail = (ImageView) findViewById(R.id.ultrasound_thumbnail);
        // Set the image to be displayed in image box via image object.
        final Uri image = UltrasoundImageScreen.getOutputMediaFileUri(UltrasoundImageScreen.IMAGE_NAME);
        thumbnail.setImageURI(image);
        try {
			Bitmap bmp=BitmapFactory.decodeStream(getContentResolver().openInputStream(image));
			//byte[] imgBytes = bitmapToByteArray(bmp);
			//byte[] compressed = compress(imgBytes);
			//String imgString = Base64.encodeToString(compressed, Base64.NO_WRAP);
			//imgRecord.image = imgString;
			
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
            	
            	//* Initiaize Medical Details received from user's input at UI.
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
                
                String bday = "dd.mm.yy";
                String page = mPatientAge.getText().toString();
                if(page.length() == 0){
                	page = "0";
                } else{
                	pat.Birthdate = page;
                	String[] tokens = page.split(" ");
                	bday = tokens[0]+"."+tokens[1]+"."+tokens[2];
                	System.out.println(pat.Birthdate);
                	
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
                
                // Initializaiton Finishes here
         
                
                // Build string data representing patient informaiton.
            	String data = UltrasoundImageScreen.photoID.toString() +" "+ pat.FirstName +"."+pat.LastName +":"+ bday +":"+ comments +";";
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
            	final HttpClient conn = new HttpClient();
       
            	// Display Progress Dialog of uploading information.
            	final ProgressDialog dialog = ProgressDialog.show(MetadataScreen.this,"Uploading Data", "Please Wait...", true);
                new Thread(new Runnable(){
                	public void run(){
                		try{
                			String message = "";
              
                			if((pat.FirstName.length() > 0) || (pat.LastName.length() > 0)){
                				//Insert Patient Details into remote database Patients table
                				System.out.println("About to insert Data to database!");
                				List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
                				nameValuePairs.add(new BasicNameValuePair("firstName", pat.FirstName));
                        	    nameValuePairs.add(new BasicNameValuePair("lastName", pat.LastName));
                        	    nameValuePairs.add(new BasicNameValuePair("date", pat.Birthdate));
                				JSONObject output = conn.SendHttpPost("http://ultrasound.azurewebsites.net/index.php/dbtest/callPatientCheck", nameValuePairs);
                				if(output != null){
                					if(output.getInt("result")== -1){
                						
                						message = "Patient Not Present!";
                						new TransferTask().execute(message);
                						//Make HTTP POST
                						output = conn.SendHttpPost("http://ultrasound.azurewebsites.net/index.php/dbtest/insertPatient", nameValuePairs);
                						if(output != null){
                							if(output.getInt("result")== 1){
                								message = "Inserted Patient Successfully!";
                								new TransferTask().execute(message);
                								
                								//Make HTTP Post
                								output = conn.SendHttpPost("http://ultrasound.azurewebsites.net/index.php/dbtest/callPatientCheck", nameValuePairs);
                								List<BasicNameValuePair> nameValues = new ArrayList<BasicNameValuePair>();
                								int pid = output.getInt("result");
                								System.out.println(pid);
                								
                								//Prepare to insert medical data inside.
                								nameValues.add(new BasicNameValuePair("piD", String.valueOf(pid)));
                                        	    nameValues.add(new BasicNameValuePair("fComments", imgRecord.FieldworkerComments));
                                        	    nameValues.add(new BasicNameValuePair("imG", "101010101"));
                                        	    nameValues.add(new BasicNameValuePair("gesT", String.valueOf(imgRecord.Gestation)));
                                        	    nameValues.add(new BasicNameValuePair("isBleed", String.valueOf(imgRecord.IsBleeding)));
                                        	    nameValues.add(new BasicNameValuePair("preB", String.valueOf(imgRecord.Prebirth)));
                                        	    nameValues.add(new BasicNameValuePair("diamFet", String.valueOf(imgRecord.DiameterFetalHead)));
                                        	    nameValues.add(new BasicNameValuePair("diaMot", String.valueOf(imgRecord.DiameterMotherHip)));
                                        	    nameValues.add(new BasicNameValuePair("fSeen", String.valueOf(0)));
                                        	    
                                        	    //Insert Medical Data into Records.
                                        	    output = conn.SendHttpPost("http://ultrasound.azurewebsites.net/index.php/dbtest/insertPatientMed", nameValues);
                                        	    
                							}
                						}else{
                							message = "Insertion of Patient unsuccessful!";
                							new TransferTask().execute(message);
    
                						}
                					} else {
                						message = "Patient Already Present!";
                						new TransferTask().execute(message);
                					
                					}
                				} else {
                					System.out.println("Null Json.");
                				}
                				
                				
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
    
    
    /* Encode bitmap to base64 String. 
     * @image: bitmap of image to be encoded to base64. */
     
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
    
    /* Decode Base64 Stirng to Bitmap.
     * @input: bas64 encoded input string.
     */
    public static Bitmap decodeBase64(String input) 
    {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length); 
    }
    
    
    /*Get ImageURI from its bitmap.
     * @Context: current activity on UI
     * @inImage: bitmap of image to be converted to Uri
     */
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
