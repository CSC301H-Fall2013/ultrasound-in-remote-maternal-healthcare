package org.nepalus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginScreen extends Activity {
	Context mContext;
	public static String loginName;
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
        
        // Handle the on click event for login button
        Button nextButton = (Button) findViewById(R.id.login_submit);
        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	// Store the name entered by the user
            	// launch the walk through activity
            	Intent i = new Intent(mContext, WalkthroughScreen.class);
            	startActivity(i);
            }
        });
    }
}