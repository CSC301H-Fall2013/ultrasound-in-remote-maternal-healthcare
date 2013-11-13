package csc301.ultrasound.global;

import csc301.ultrasound.global.Secrets;
import csc301.ultrasound.model.User;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;

import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.codec.binary.Base64;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * Class that handles user authorization.
 */
public class Authentication
{
	/**
	 * Authenticate a user.
	 *
	 * @param email The email address of the registered user.
	 * @param password The password of the registered user.
	 * @return A populated instance of csc301.ultrasound.model.User
	 */
	public User authenticate(String email, String password)
	{
		User authedUser = null;
		
		try
		{
			// POST the authentication request
			URL authBaseURL = new URL("https://ultrasound.auth0.com/oauth/ro");
			
			String authData = String.format("client_id=%s&" + 
			                                "client_secret=%s&" +
					                        "connection=Username-Password-Authentication&" + 
			                                "username=%s&" + 
					                        "password=%s&" + 
			                                "grant_type=password&" + 
					                        "scope=openid profile", 
					                        Secrets.AUTH_CLIENT_ID, Secrets.AUTH_CLIENT_SECRET, email, password);
			
			HttpsURLConnection connection = (HttpsURLConnection)authBaseURL.openConnection();
			connection.setRequestMethod("POST");
			
			connection.setDoOutput(true);
			
			DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
			dos.writeBytes(authData);
			dos.flush();
			dos.close();
			
			// check if we received error code 200 (success). Otherwise, the authenication failed.
			if (connection.getResponseCode() == 200)
			{
				BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				
				String responseLine = null;
				StringBuffer response = new StringBuffer();
				
				while ((responseLine = br.readLine()) != null)
					response.append(responseLine);
				
				br.close();
				
				// parse the json response
				JsonElement responseJElement = new JsonParser().parse(response.toString());
			    String responseToken = responseJElement.getAsJsonObject().get("id_token").getAsString();
			    
			    // remove the identifier
			    responseToken = responseToken.split("\\.")[1];
			    
			    // convert the idToken from base 64
			    Base64 decoder = new Base64(true);
			    
			    String idToken = new String(decoder.decode(responseToken));
			    
			    // parse the info about the user
			    JsonElement decodedJElement = new JsonParser().parse(idToken);
			    
			    // Auth0 qualifies the user id with the service that the authentication was provided by,
			    // in this case Auth0; delimited by a pipe. To get the ID, split on the pipe and grab
			    // the last index.
			    String  qualifiedID = decodedJElement.getAsJsonObject().get("user_id").getAsString();
			    
			    int     id        = Integer.parseInt(qualifiedID.split("\\|")[1]);
			    String  name      = decodedJElement.getAsJsonObject().get("nickname").getAsString();
				String  location  = decodedJElement.getAsJsonObject().get("location").getAsString();
				int     phone     = decodedJElement.getAsJsonObject().get("phone").getAsInt();
				int     authlevel = decodedJElement.getAsJsonObject().get("authlevel").getAsInt();
				
				// create the internal representation of the user
				authedUser = new User(id, email, name, location, phone, authlevel);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return authedUser;
	}
}
