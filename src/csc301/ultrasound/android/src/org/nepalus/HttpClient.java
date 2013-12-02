package org.nepalus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.preference.PreferenceActivity.Header;
import android.util.Log;



    /*public static JSONObject connect(String url)
    {

        HttpClient httpclient = new DefaultHttpClient();

        // Prepare a request object
        HttpGet httpget = new HttpGet(url); 

        // Execute the request
        HttpResponse response;

        JSONObject json = new JSONObject();

        try {
            response = httpclient.execute(httpget);

            HttpEntity entity = response.getEntity();

            if (entity != null) {

                // A Simple JSON Response Read
                InputStream instream = entity.getContent();
                String result= convertStreamToString(instream);

                json=new JSONObject(result);

                instream.close();
            }

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return json;
    }*/
    /**
     *
     * @param is
     * @return String
     */
   /* public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }*/
    
    public class HttpClient {
        private static final String TAG = "HttpClient";

        public JSONObject SendHttpPost(String URL, List<BasicNameValuePair> nameValuePairs) {

        	DefaultHttpClient httpclient = new DefaultHttpClient();
        	HttpPost httppost = new HttpPost(URL);

        	try {
        	    // Add your dat
        	    /*nameValuePairs.add(new BasicNameValuePair("id", "12345"));
        	    nameValuePairs.add(new BasicNameValuePair("stringdata", "AndDev is Cool!"));*/
        	    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

        	    // Execute HTTP Post Request
        	    HttpResponse response = httpclient.execute(httppost);
        	    BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(),"iso-8859-1"),8);
        	    
        	    StringBuilder content = new StringBuilder();
        	    String line;
        	    while (null != (line = reader.readLine())) {
        	        content.append(line + "\n");
        	    }
        	    try {
        	        JSONObject userObject = new JSONObject(content.toString());
        	        return userObject;
        	        
        	    } catch(Exception ex){
        	        //don't forget this
        	    }

        	} catch (ClientProtocolException e) {
        	    // TODO Auto-generated catch block
        	} catch (IOException e) {
        	    // TODO Auto-generated catch block
        	}
			return null;
    }
    

    public JSONArray SendHttpPostArray(String URL, List<BasicNameValuePair> nameValuePairs) {

        	DefaultHttpClient httpclient = new DefaultHttpClient();
        	HttpPost httppost = new HttpPost(URL);

        	try {
        	    // Add your dat
        	    /*nameValuePairs.add(new BasicNameValuePair("id", "12345"));
        	    nameValuePairs.add(new BasicNameValuePair("stringdata", "AndDev is Cool!"));*/
        	    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

        	    // Execute HTTP Post Request
        	    HttpResponse response = httpclient.execute(httppost);
        	    BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(),"iso-8859-1"),8);
        	    
        	    StringBuilder content = new StringBuilder();
        	    String line;
        	    while (null != (line = reader.readLine())) {
        	        content.append(line + "\n");
        	    }
        	    try {
        	        JSONArray userObject = new JSONArray(content.toString());
        	        return userObject;
        	        
        	    } catch(Exception ex){
        	        //don't forget this
        	    }

        	} catch (ClientProtocolException e) {
        	    // TODO Auto-generated catch block
        	} catch (IOException e) {
        	    // TODO Auto-generated catch block
        	}
			return null;
    }
    

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
        }
    }

