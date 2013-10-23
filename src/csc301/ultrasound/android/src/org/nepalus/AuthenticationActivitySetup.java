package org.nepalus;

import java.io.Serializable;

public class AuthenticationActivitySetup implements Serializable 
{

    public String tenant;
    public String clientId;
    public String callback;
    public String connection; //Optional

    /* Intialize varaibles to connect to server.*/
    public AuthenticationActivitySetup(String tenant, String clientId, String callback) 
    {
        this(tenant, clientId, callback, null);
    }
    
    /* Intialize varaibles to connect to server.*/
    public AuthenticationActivitySetup(String tenant, String clientId, String callback, String connection) 
    {
        this.tenant = tenant;
        this.clientId = clientId;
        this.callback = callback;
        this.connection = connection;
    }
}
