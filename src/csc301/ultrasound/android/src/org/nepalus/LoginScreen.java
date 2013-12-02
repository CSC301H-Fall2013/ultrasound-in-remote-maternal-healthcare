package org.nepalus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginScreen extends Activity {
	Context mContext;
	public static String loginName;
	public static Boolean activate;
	public static EditText muserName;
	/*
	 * Method gets called when activity is launched.
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 * @param savedInstanceState: saved instance of this activity to extract previous settings.
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //Set the content
        setContentView(R.layout.login);
        //Set the context
        mContext = this;
        muserName = (EditText) findViewById(R.id.user_name);
        // Handle the on click event for login button
        Button nextButton = (Button) findViewById(R.id.login_submit);
        nextButton.setOnClickListener(new View.OnClickListener() {
        	
            public void onClick(View view) {
            	if(!(LoginScreen.muserName.getText().toString().equals(""))){
            		LoginScreen.loginName = LoginScreen.muserName.getText().toString();
            	// Store the name entered by the user
            	// launch the walk through activity
            	Intent i = new Intent(mContext, WalkthroughScreen.class);
            	startActivity(i);
            	}
            }
        });
    }
    
    /*
     * Determine whether mobile device has internet access or not.
     */
    public  boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager 
              = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    
    
}