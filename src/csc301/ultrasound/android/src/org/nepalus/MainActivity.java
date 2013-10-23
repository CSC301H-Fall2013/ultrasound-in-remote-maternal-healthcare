package org.nepalus;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends Activity {

	static final String ClientID = "D7INoBoLpHW9kigYCWxlyY4oPTiUj3jg";
	static final String Tenant = "ultrasound";
	static final String Callback = "https://localhost/client";
    static final String Connection = "Username-Password-Authentication"; //any other connection
    Context mcontext;
    public boolean result = false;
    
    
    /*
	 * Method gets called when activity is launched.
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 * @param savedInstanceState: saved instance of this activity to extract previous settings.
	 */
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        //set the content view
        setContentView(R.layout.activity_main);
        mcontext = this;
        // Get login button from xml
        Button login = (Button) findViewById(R.id.login);
        //Set event handler for login button
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent authActivity = new Intent(MainActivity.this,
                        org.nepalus.AuthenticationActivity.class);

                AuthenticationActivitySetup setup;
                setup = new AuthenticationActivitySetup(Tenant, ClientID, Callback, Connection);
                
                authActivity.putExtra(AuthenticationActivity.AUTHENTICATION_SETUP, setup);
                // Launch authentication activity to login.
                startActivityForResult(authActivity, AuthenticationActivity.AUTH_REQUEST_COMPLETE);
            }
        });
        
        //Get login widget button from xml.
        Button login_widget = (Button) findViewById(R.id.login_widget);
        // Set event handler for login wdiget button.
        login_widget.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent authActivity = new Intent(MainActivity.this,
                        org.nepalus.AuthenticationActivity.class);

                AuthenticationActivitySetup setup;
                setup = new AuthenticationActivitySetup(Tenant, ClientID, Callback);

                authActivity.putExtra(AuthenticationActivity.AUTHENTICATION_SETUP, setup);
                
                //Launch authentication activity to login.
                startActivityForResult(authActivity, AuthenticationActivity.AUTH_REQUEST_COMPLETE);
            }
        });
        
        
    }
    
    /* Called when Authentication activity returns
     * @param requestCode Flag to represent authentication request from user side.
     * @param resultCode  Result from authenticating user with server.
     * @param authActivityResult Resulting intent carrying data passed by authentication activity.
     * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent authActivityResult) 
    {
        super.onActivityResult(requestCode, resultCode, authActivityResult);

        switch(requestCode)
        {
            case AuthenticationActivity.AUTH_REQUEST_COMPLETE:
                if (resultCode==RESULT_OK)
                {
                	//Launch the interface on successful login.
                	Intent i = new Intent(MainActivity.this, org.nepalus.LoginScreen.class);
                	startActivity(i);
                	
                	/* Code to be used later. */
                	
                	/*this.result = true;
                    AuthenticationActivityResult result;
                    result = (AuthenticationActivityResult) authActivityResult.getSerializableExtra(AuthenticationActivity.AUTHENTICATION_RESULT);

                    String userInfoUrl = String.format("https://api.auth0.com/userinfo?access_token=%s", result.accessToken);
                    
                    new AsyncTask<String, Void, JSONObject>() 
                    {
                    	@Override
            	    	protected JSONObject doInBackground(String... url) 
                    	{
            	          JSONObject json = RestJsonClient.connect(url[0]);
            	          
            	          return json;
            	        }
            	        
            	        @Override
            	        protected void onPostExecute(JSONObject user) 
            	        {
            	        	 try 
            	        	 {
								((TextView)findViewById(R.id.user)).setText(user.toString(2));
								
            	        	 } 
            	        	 catch (JSONException e) 
            	        	 {
								e.printStackTrace();
            	        	 }
            	        }
                    }.execute(userInfoUrl);
                }
                break;*/
                }
        }
    }

    /*Create and display the menu 
     * @param menu Instance (base) layer for menu's xml display.
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
