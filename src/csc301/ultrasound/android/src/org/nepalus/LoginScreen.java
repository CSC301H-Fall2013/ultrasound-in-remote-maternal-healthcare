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
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        mContext = this;
        
        Button nextButton = (Button) findViewById(R.id.login_submit);
        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	EditText name = (EditText) findViewById(R.id.nameFill);
            	loginName = name.getText().toString();
            	Intent i = new Intent(mContext, WalkthroughScreen.class);
            	startActivity(i);
            }
        });
    }
}