package org.nepalus;

import  com.microsoft.windowsazure.mobileservices.*;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
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





import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
	private TextView messageText;
    private Button uploadButton, btnselectpic;
    private ImageView imageview;
    private int serverResponseCode = 0;
    private ProgressDialog dialog = null;
       
    private String upLoadServerUri = null;
    private static String imagepath=null;
	public static Boolean oldEntry = false;
	public static String First = "";
	public static String Last = "";
	public static String Birth = "";
	
	
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
    	upLoadServerUri = "http://ultrasound.azurewebsites.net/index.php/dbtest/upload";
        if(this.oldEntry){
        	mPatientName.setText(this.First + " " + this.Last);
        	mPatientAge.setText(this.Birth);
        }
        
        
        // Get Image object from display
        ImageView thumbnail = (ImageView) findViewById(R.id.ultrasound_thumbnail);
        // Set the image to be displayed in image box via image object.
        final Uri image = UltrasoundImageScreen.getOutputMediaFileUri(UltrasoundImageScreen.IMAGE_NAME);
        thumbnail.setImageURI(image);
        
   
        
        // Handle the on click event of finish button
        Button nextButton = (Button) findViewById(R.id.finish);
        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	if(!isNetworkAvailable()){
        			new TransferTask().execute("No Network Connection Available!");
        			return;
        		}
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
                	new TransferTask().execute("Name Details Invalid!");
            		return;
               
                } else{
                	try{
                	String[] tokens =  name.split(" ");
                	
                	pat.FirstName = tokens[0];
                	if(tokens.length > 1){
                		pat.LastName = name.split(" ")[1];
                	}
                	} catch (Exception e){
                		new TransferTask().execute("Name Details Invalid!");
                		return;
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
                	new TransferTask().execute("Birth Details Invalid!");
            		return;
                } else{
                	pat.Birthdate = page;
                	try{
                	String[] tokens = page.split(" ");
                	bday = tokens[0]+"."+tokens[1]+"."+tokens[2];
                	System.out.println(pat.Birthdate);
                	} catch (Exception e){
                		new TransferTask().execute("Birth Details Invalid!");
                		return;
                	}
                	
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
            	String dir = UltrasoundImageScreen.getMediaDir();
            	MetadataScreen.imagepath = dir + File.separator + UltrasoundImageScreen.IMAGE_NAME + ".jpg";
            	System.out.println("Printing out image path");
            	System.out.println(MetadataScreen.imagepath);
            	File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
      	        Environment.DIRECTORY_PICTURES), UltrasoundImageScreen.IMAGE_DIRECTORY);
        		
        		File b = new File(path +"/patientDataU.txt");
        		if(!MetadataScreen.oldEntry){
        			if(b.exists()){
        				writeToFile(data,"patientDataU.txt");
        			} else{
        				writeToFile2(data,"patientDataU.txt");
        			}
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
                								message = "Uploading Image to Server!";
                    							new TransferTask().execute(message);
                								uploadFile(MetadataScreen.imagepath);
                								
                								//Prepare to insert medical data inside.
                								nameValues.add(new BasicNameValuePair("piD", String.valueOf(pid)));
                                        	    nameValues.add(new BasicNameValuePair("fComments", imgRecord.FieldworkerComments));
                                        	    String prefix = "./ultrasound/";
                                        	    String basename = prefix +  UltrasoundImageScreen.IMAGE_NAME + ".png";
                                        	    
                                        	    System.out.println("Basename: " + basename);
                                        	    nameValues.add(new BasicNameValuePair("imG", basename));
                                        	    nameValues.add(new BasicNameValuePair("gesT", String.valueOf(imgRecord.Gestation)));
                                        	    nameValues.add(new BasicNameValuePair("isBleed", String.valueOf(imgRecord.IsBleeding)));
                                        	    nameValues.add(new BasicNameValuePair("preB", String.valueOf(imgRecord.Prebirth)));
                                        	    nameValues.add(new BasicNameValuePair("diamFet", String.valueOf(imgRecord.DiameterFetalHead)));
                                        	    nameValues.add(new BasicNameValuePair("diaMot", String.valueOf(imgRecord.DiameterMotherHip)));
                                        	    nameValues.add(new BasicNameValuePair("fSeen", String.valueOf(0)));
                                        	    message = "Uploading medical data to database!";
                    							new TransferTask().execute(message);
                                        	    //Insert Medical Data into Records.
                                        	    output = conn.SendHttpPost("http://ultrasound.azurewebsites.net/index.php/dbtest/insertPatientMed", nameValues);
                                        	    
                                        	    //Insert image into records.
                                        	    
                                        	    
                                        	    
                							}
                						}else{
                							message = "Insertion of Patient unsuccessful!";
                							new TransferTask().execute(message);
    
                						}
                					} else {
                						if(!MetadataScreen.oldEntry){
                							message = "Patient Already Present!";
                							new TransferTask().execute(message);
                						} else{
                							List<BasicNameValuePair> nameValues = new ArrayList<BasicNameValuePair>();
                							message = "Uploading Image to Server!";
                							new TransferTask().execute(message);
                							int pid = output.getInt("result");
            								uploadFile(MetadataScreen.imagepath);
            								
            								//Prepare to insert medical data inside.
            								nameValues.add(new BasicNameValuePair("piD", String.valueOf(pid)));
                                    	    nameValues.add(new BasicNameValuePair("fComments", imgRecord.FieldworkerComments));
                                    	    String prefix = "./ultrasound/";
                                    	    String basename = prefix +  UltrasoundImageScreen.IMAGE_NAME + ".png";
                                    	    
                                    	    System.out.println("Basename: " + basename);
                                    	    nameValues.add(new BasicNameValuePair("imG", basename));
                                    	    nameValues.add(new BasicNameValuePair("gesT", String.valueOf(imgRecord.Gestation)));
                                    	    nameValues.add(new BasicNameValuePair("isBleed", String.valueOf(imgRecord.IsBleeding)));
                                    	    nameValues.add(new BasicNameValuePair("preB", String.valueOf(imgRecord.Prebirth)));
                                    	    nameValues.add(new BasicNameValuePair("diamFet", String.valueOf(imgRecord.DiameterFetalHead)));
                                    	    nameValues.add(new BasicNameValuePair("diaMot", String.valueOf(imgRecord.DiameterMotherHip)));
                                    	    nameValues.add(new BasicNameValuePair("fSeen", String.valueOf(0)));
                                    	    
                                    	    message = "Uploading medical data to database!";
                							new TransferTask().execute(message);
                                    	    //Insert Medical Data into Records.
                                    	    output = conn.SendHttpPost("http://ultrasound.azurewebsites.net/index.php/dbtest/insertPatientMed", nameValues);
                							
                						}
                					
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
    
    /*
     * Upload file to the server
     * @param sourceFileUri: Path of the file to be uploaded.
     */
    public int uploadFile(String sourceFileUri) {
   
	  
	  String fileName = sourceFileUri;

	  HttpURLConnection conn = null;
	  DataOutputStream dos = null;  
	  String lineEnd = "\r\n";
	  String twoHyphens = "--";
	  String boundary = "*****";
	  int bytesRead, bytesAvailable, bufferSize;
	  byte[] buffer;
	  int maxBufferSize = 1 * 1024 * 1024; 
	  File sourceFile = new File(sourceFileUri); 
   
	  if (!sourceFile.isFile()) {
 	  
       
        
        Log.e("uploadFile", "Source File not exist :"+imagepath);
        
        runOnUiThread(new Runnable() {
            public void run() {
         	   messageText.setText("Source File not exist :"+ imagepath);
            }
        }); 
        
        return 0;
    
   }
   else
   {
        try { 
     	   
         	 // open a URL connection to the Servlet
            FileInputStream fileInputStream = new FileInputStream(sourceFile);
            URL url = new URL(upLoadServerUri);
            
            // Open a HTTP  connection to  the URL
            conn = (HttpURLConnection) url.openConnection(); 
            conn.setDoInput(true); // Allow Inputs
            conn.setDoOutput(true); // Allow Outputs
            conn.setUseCaches(false); // Don't use a Cached Copy
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            conn.setRequestProperty("uploaded_file", fileName);
            
            
            dos = new DataOutputStream(conn.getOutputStream());
  
            dos.writeBytes(twoHyphens + boundary + lineEnd); 
            dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
         		                     + fileName + "\"" + lineEnd);
            
            dos.writeBytes(lineEnd);
  
            // create a buffer of  maximum size
            bytesAvailable = fileInputStream.available(); 
  
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];
  
            // read file and write it into form...
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);  
              
            while (bytesRead > 0) {
         	   
              dos.write(buffer, 0, bufferSize);
              bytesAvailable = fileInputStream.available();
              bufferSize = Math.min(bytesAvailable, maxBufferSize);
              bytesRead = fileInputStream.read(buffer, 0, bufferSize);   
              
             }
  
            // send multipart form data necesssary after file data...
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
  
            // Responses from the server (code and message)
            serverResponseCode = conn.getResponseCode();
            String serverResponseMessage = conn.getResponseMessage();
             
            Log.i("uploadFile", "HTTP Response is : " 
         		   + serverResponseMessage + ": " + serverResponseCode);
            
            if(serverResponseCode == 200){
         	   
                        
            }    
            
            //close the streams //
            fileInputStream.close();
            dos.flush();
            dos.close();
             
       } catch (MalformedURLException ex) {
     	  
         
           ex.printStackTrace();
          
           
           Log.e("Upload file to server", "error: " + ex.getMessage(), ex);  
       } catch (Exception e) {
     	  
          
           e.printStackTrace();
           
          
           Log.e("Upload file to server Exception", "Exception : "  + e.getMessage(), e);  
       }
           
       return serverResponseCode; 
       
    } // End else block 
  }
    
    
    /*
     * Determine whether mobile device is connected to internet or not.
     */
	public  boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager 
		= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

    
    
 

	
}
