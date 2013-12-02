package org.nepalus;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

public class UltrasoundImageScreen extends Activity {
	static String IMAGE_DIRECTORY = "ultrasound";
	public static Integer photoID = 0;
	public static String IMAGE_NAME="p0";
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	public static boolean cam = true;
	Context mContext;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ultrasoundimage);
        mContext = this;
        if(!cam){
        	onBackPressed();
        }
    }
    

    @Override
	protected void onResume() {
		super.onResume();
		if (cam){
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        Uri fileUri = getOutputMediaFileUri(IMAGE_NAME); // create a file to save the image
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

        // start the image capture Intent
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        cam = false;
		}
	}

	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Image captured and saved to fileUri specified in the Intent
            	Log.d("Picture ok", "Picture successfully received!");
            	Intent i = new Intent(this, MetadataScreen.class);
            	startActivity(i);
            } else if (resultCode == RESULT_CANCELED) {
            	//return to previous screen
            	Log.d("Back press", "Going Back");
            	onBackPressed();
            } else {
                //TODO Image capture failed, advise user
            }
        }
    }

	/** Create a file Uri for saving an image */
	public static Uri getOutputMediaFileUri(String name){
	      return Uri.fromFile(getOutputMediaFile(name));
	}
	
	/** Create a File for saving an image */
	public static File getOutputMediaFile(String name){

		String externalStorageState = Environment.getExternalStorageState();
		boolean isExternalStorageWriteable = Environment.MEDIA_MOUNTED.equals(externalStorageState);
		if (!isExternalStorageWriteable) {
		    //TODO inform user that no SD card available
			return null;
		}
		
	    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
	              Environment.DIRECTORY_PICTURES), IMAGE_DIRECTORY);
	    // This location works best if you want the created images to be shared
	    // between applications and persist after the app has been uninstalled.

	    // Create the storage directory if it does not exist
	    if (! mediaStorageDir.exists()){
	        if (! mediaStorageDir.mkdirs()){
	            //TODO handle directory creation error
	            return null;
	        }
	    }

	    //TODO Create a unique media file name for each invocation
	    File mediaFile  = new File(mediaStorageDir.getPath() + File.separator +
	        name + ".jpg");

	    return mediaFile;
	}
	
	public static String getMediaDir(){
		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
	              Environment.DIRECTORY_PICTURES), IMAGE_DIRECTORY);
		return mediaStorageDir.getAbsolutePath();
	}
}
