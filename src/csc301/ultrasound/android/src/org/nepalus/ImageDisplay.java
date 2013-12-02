package org.nepalus;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

public class ImageDisplay extends Activity {
	public static ImageView image;
	public static String Url = "http://ultrasound.azurewebsites.net";
	public static String base = null;
	public static String anno = null;
	public static Bitmap mBase = null;
	public static Bitmap mlayer = null;
	public static Bitmap fuion = null;
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.imagedisplay);
		this.image = (ImageView) findViewById(R.id.ultrasound12);
	
		Intent obj = this.getIntent();
		this.base = obj.getStringExtra("base").substring(1);
		this.anno = obj.getStringExtra("anno").substring(1);
		Toast.makeText(this, "Downloading Annotations From Server!", Toast.LENGTH_LONG).show();
		new Thread(new Runnable(){
            public void run(){
            	System.out.println("begins");
            	try{
            		String baseUrl = ImageDisplay.Url + ImageDisplay.base;
            		String layerUrl = ImageDisplay.Url + ImageDisplay.anno;
            		ImageDisplay.mBase = getBitmapFromURL(baseUrl);
            		ImageDisplay.mlayer = getBitmapFromURL(layerUrl);
            	    ImageDisplay.fuion = combineImages(mlayer, mBase);
            	} catch (Exception e){
            		System.out.println("Error in Bitmap Area");
            	}
            	 runOnUiThread(new Runnable() {
            		 
                     @Override
                     public void run() {
                    	 System.out.println(ImageDisplay.anno);
                     	System.out.println(ImageDisplay.base);
                    	 if(ImageDisplay.mlayer == null || (ImageDisplay.fuion == null)){
                    		 System.out.println("On UI null");
                    		 ImageDisplay.image.getLayoutParams().height = ImageDisplay.mBase.getHeight();
                    		 ImageDisplay.image.setImageBitmap(ImageDisplay.mBase);   
                    	 } else{
                    		 ImageDisplay.image.getLayoutParams().height = ImageDisplay.fuion.getHeight();
                    		 ImageDisplay.image.setImageBitmap(ImageDisplay.fuion);   
                    	 }
                    	 
                    	 
                     }
                 });
            	
            }
		}).start();
		
		
		
	}

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

}
