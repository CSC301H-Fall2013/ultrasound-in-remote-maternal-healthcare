package org.nepalus;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


public class patientData extends Activity implements OnClickListener {
	
	
	public String url = "http://ultrasound.azurewebsites.net/annotated/";
	public static Bitmap imageUltra = null;
	public static Bitmap imageBase = null;
	public static Bitmap fusion = null;
	public static ImageView thumbnail = null;
	public static ImageView annotated = null;
	public static String base = "pic.jpg";
	public static String anno = "pic.jpg";
	public static int pid = -1;
	public static LinearLayout la = null;
	public static JSONArray recorData = null;
	public static String firsT = "";
	public static String lasT = "";
	public static String birTH = "";
	/*Class to store details during multiple threads run.*/
	class info{
		public int pid = -1;
		public String response = null;
	}
	
	final info details = new info();
	
	/* Class to display messages to user during multiple threads run.*/
	private class TransferTask extends AsyncTask<String, Void, String>{
		protected String doInBackground(String... urls) {
			return urls[0];
		}
		
		@Override
		protected void onPostExecute(String result) {
			Toast.makeText(patientData.this, result, Toast.LENGTH_SHORT).show();
		}
		
	}
	
	/*Update Details on Main UI, while a thread retrieves information from database in background. */
	private class updateTask extends AsyncTask<JSONArray, Void, JSONArray>{
		/* Execute this function when execute is called on this class's instance.
		 * @params jsonObjects : Array of jsonObjects. 
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		protected JSONArray doInBackground(JSONArray...jsonArrays) {
			return jsonArrays[0];
		}
		
		@Override
		/* After Do in Background is finished. Execute this function.
		 * @params result : Instance of JSON holding data from database.
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		protected void onPostExecute(JSONArray result) {
			LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			
			for(int i = 0; i < result.length(); i++){
				
	    		Button tmp = new Button(patientData.this);
	    		try {
	    			String mess = "";
	    			if(result.getJSONObject(i).get("RadiologistResponse").toString().equals("null")){
	    				mess += "Not Responded: ";
	    			} else{
	    				mess += "Responded: ";
	    			}
	    			mess += result.getJSONObject(i).get("Date").toString();
					tmp.setText(mess);
				} catch (JSONException e) {
					System.out.println("Error Setting Text");
				}
	    		tmp.setTag(new String("Cbtn"));
	    		tmp.setId(i);
	    		tmp.setLayoutParams(params);
	    		tmp.setOnClickListener((OnClickListener) patientData.this);
	    		patientData.la.addView(tmp);
	    	}
	    	
		}
		
	}
	
	/*
	 * Method gets called when activity is first created.
	 * @params savedInstanceState : saved Instance of the activity is passed if it exists.
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set the content to be displayed.
		setContentView(R.layout.metadata2);
		// get the intent object to extract passed information
		Intent act = getIntent();
		// Below block extracts information passed on from previous activity.
        String Title = getIntent().getStringExtra("Title");
		final String age = act.getStringExtra("age");
		
		
		final String name = Title.replace(".", " ");
		String[] nameTok = name.split(" ");
		if(nameTok.length > 1){
;			this.firsT = nameTok[0];
		
			this.lasT= nameTok[1];
		} else{
			this.firsT = name;
			this.lasT = "";
		}
		this.birTH = age.replace(".", " ");
		this.la = (LinearLayout) (findViewById(R.id.layout1));
		TextView textview=(TextView) findViewById(R.id.textmsg);
		this.la.removeView(textview);
		Button tmp = new Button(this);
		tmp.setText("Post New Record");
		LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		tmp.setTag(new String("Dbtn"));
		tmp.setId(-1);
		tmp.setLayoutParams(params);
		tmp.setOnClickListener((OnClickListener) patientData.this);
		patientData.la.addView(tmp);
		
		/* Display a Progress Dialog to indicate information grab from database 
		 * about specific patient.
		 */
		final ProgressDialog dialog = ProgressDialog.show(patientData.this,"Downloaing Data", "Please Wait...", true);
        new Thread(new Runnable(){
        	public void run(){
        		try{
        				//Extract Name and Birthdate.
        				String message = "";
        				HttpClient conn = new HttpClient();
        				String FirstName = "";
        				String LastName = "";
        				String[] nameTok = name.split(" ");
        				if(nameTok.length > 1){
        		;			FirstName = nameTok[0];
        				
        					LastName= nameTok[1];
        				} else{
        					FirstName = name;
        					LastName = "";
        				}
        				String bday = age.replace(".", " ");
        				
        				//Build HTTP request to call PHP Web service controllers.
        				List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
        				nameValuePairs.add(new BasicNameValuePair("firstName", FirstName));
                	    nameValuePairs.add(new BasicNameValuePair("lastName", LastName));
                	    System.out.println(FirstName);
                	    System.out.println(LastName);
                	    
                	    System.out.println(bday);
                	    nameValuePairs.add(new BasicNameValuePair("date", bday));
        				JSONObject output = conn.SendHttpPost("http://ultrasound.azurewebsites.net/index.php/dbtest/callPatientCheck", nameValuePairs);
        				
        				// Check if http request failed or not.
        				if(output != null){
        					int pid = output.getInt("result");
        					// Passed, Store pid of patient.
        					details.pid = pid;
        					patientData.pid = pid;
        					if(pid == -1){
        						
        						// Patient Non - existent.
        						message = "Patient Details does not exist in database";
        						new TransferTask().execute(message);
        						
        					} else {
        						// Patient Exists.
        						// Prepare to extract Patient's Medical Info from DB.
        						List<BasicNameValuePair> nameValues = new ArrayList<BasicNameValuePair>();
                				nameValues.add(new BasicNameValuePair("piD", String.valueOf(pid)));
                				JSONArray output1;
                    			
                    			output1 = conn.SendHttpPostArray("http://ultrasound.azurewebsites.net/index.php/dbtest/getPatientMed", nameValues);
                    			patientData.recorData = output1;
                    			new updateTask().execute(output1);
                    			try {
            						JSONObject a = output1.getJSONObject(0);
            						System.out.println(a.getString("IMGAref").toString());
            					} catch (JSONException e) {
            						message = "Network Error!";
            						new TransferTask().execute(message);
            					}
                    			
                    			
                    			
                			
        					}
        				} else {
        					message = "Network Error!";
    						new TransferTask().execute(message);
        				}
        				
        				
      
        			
        			
        			dialog.dismiss();
        			//Toast.makeText(MetadataScreen.this, "Data Sent Successfully!", Toast.LENGTH_SHORT).show();
        			Log.d("alertD", "abt to show Dialog");
        			
        			
        		} catch (Exception e){
        			e.printStackTrace();
        		}
        	}
        }).start();
		
        
        
        // Get the Text Fields blocks from display
        EditText ageText = (EditText) findViewById(R.id.patient_age2);
        EditText nameText = (EditText) findViewById(R.id.patient_name2);
       
        // Add Information into text blocks to be displayed.
        // Information is what we extract from Intent object
       
        System.out.println(age);
        String bday = age.replace(".", " ");
   
        
      
        ageText.setText(bday);
        nameText.setText(Title);
        
		
	}
	
	@Override
	public void onClick(View b) {
		if(!isNetworkAvailable()){
			new TransferTask().execute("No Network Connection Available!");
			return;
		}
		if(b.getTag().toString().equals("Cbtn")){
			String message = "";
			JSONObject cur = null;
			final View button = b;
			try {
			    cur = patientData.recorData.getJSONObject(b.getId());
			    message += "Response: ";
			    message += cur.getString("RadiologistResponse").toString();
			    message += "\n";
			    message += "Gestation: ";
			    message += cur.getString("Gestation").toString();
			    message += "\n";
			    message += "Fielworker Comments: ";
			    message += cur.getString("FieldworkerComments").toString();
			    message += "\n";
			    message += "Diameter Fetal Head: ";
			    message += cur.getString("DiameterFetalHead").toString();
			    message += "\n";
			    message += "Diameter Mother Hip: ";
			    message += cur.getString("DiameterMotherHip").toString();
			    message += "\n";
			    message += "Is Bleeding: ";
			    message += cur.get("IsBleeding").toString();
			    message += "\n";
			    message += "Prebirth: ";
			    message += cur.get("Prebirth").toString();
			    message += "\n";
			    
				
			} catch (JSONException e) {
				System.out.println("Error Creating message");
			}
			
			//TextView textview=(TextView) findViewById(R.id.textmsg);
			ScrollView sv = new ScrollView(this);
			LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	    	TextView tv = new TextView(this);
	    	tv.setLayoutParams(params);
	    	tv.setText(message);

			sv.addView(tv);
			new AlertDialog.Builder(patientData.this)
			.setTitle("Response From Radiologist")
			//.setMessage(message)
			.setView(sv)
			.setPositiveButton("Display Annotated Response", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) { 
					try{
					JSONObject tar = patientData.recorData.getJSONObject(button.getId());
					// get Patient Details
					// Pass patient details to launching intent.
					if(tar != null){
						Intent load = new Intent(patientData.this, ImageDisplay.class);
						String base = tar.getString("IMGUref").toString();
						String anno = tar.getString("IMGAref").toString();
						load.putExtra("base", base);
						load.putExtra("anno", anno);
						startActivity(load);
					} else {
						Toast.makeText(patientData.this, "Image Cannot be Displayed!", Toast.LENGTH_SHORT);
					}
					} catch (Exception e){
						System.out.println("Error Extracting Image Details");
					}
				}
			}).show();
		} 
		else{
			if(!(patientData.pid == -1)){
			String user = LoginScreen.loginName;
			System.out.println("Load Camera");
			String date = new SimpleDateFormat("ddMMyyyyHHMMSS").format(new Date());
			String stamp = user + date;
			UltrasoundImageScreen.IMAGE_NAME = stamp;
			MetadataScreen.oldEntry = true;
			MetadataScreen.First = this.firsT;
			MetadataScreen.Last = this.lasT;
			MetadataScreen.Birth = this.birTH;
			// Signal to activate the camera
			UltrasoundImageScreen.cam = true;
			Intent load = new Intent(patientData.this, UltrasoundImageScreen.class);
			startActivity(load);
			} else{
				new TransferTask().execute("Cannot Add Record for Non-existent Patient!");
			}
		}
		
	}
	
	

	
	
	/*
	 * Download image from internet server
	 * @param imageurl: url link of image to download
	 */
	public Bitmap getBitmapFromURL(String imageUrl) {
		try {
			URL url = new URL(imageUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			return myBitmap;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	/*
	 * Layer two images on top of each other and return combined bitmap.
	 * @params  c: Bitmap of top layer.
	 * 			s: Bitmap of bottom layer.
	 */
	public Bitmap combineImages(Bitmap c, Bitmap s) { // can add a 3rd parameter 'String loc' if you want to save the new image - left some code to do that at the bottom 
	    Bitmap cs = null; 
	 
	    int width, height = 0; 
	 
	    width = s.getWidth();
	    height = s.getHeight();
	 
	    cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888); 
	 
	    Canvas comboImage = new Canvas(cs); 
	 
	    
	    comboImage.drawBitmap(s, 0f, 0f, null);
	    comboImage.drawBitmap(c, 0f, 0f, null); 
	 
	   
	 
	    return cs; 
	  } 
	
	/*
	 * Determine whether mobile device is connected to internet.
	 */
	 public  boolean isNetworkAvailable() {
	        ConnectivityManager connectivityManager 
	              = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	    }

}
