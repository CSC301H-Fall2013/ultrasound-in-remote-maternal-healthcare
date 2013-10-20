import urllib
import urllib2
import json
import sys
import re
import traceback

def createUser():
    if (len(sys.argv) - 1) != 9:
        print 'Incorrect number of arguments specified.'
        sys.exit('Usage: clientID clientSecret email password firstname lastname phone location authlevel')
    
    try:
        clientID     = sys.argv[1]
        clientSecret = sys.argv[2]
        email        = sys.argv[3]
        password     = sys.argv[4]
        connection   = "Username-Password-Authentication"
        firstname    = sys.argv[5]
        lastname     = sys.argv[6]
        phone        = sys.argv[7]
        location     = sys.argv[8]
        authlevel    = sys.argv[9]

        if (not emailValid(email)):       sys.exit("Invalid email address.")
        if (len(password)  < 6):          sys.exit("Password is too short.")
        if (len(firstname) < 2):          sys.exit("First name is too short.")
        if (len(lastname)  < 2):          sys.exit("Last name is too short.")
        if (len(phone)     < 9):          sys.exit("Phone number is too short.")
        if (len(location)  < 2):          sys.exit("Location is too short.")
        if not authlevelValid(authlevel): sys.exit("Invalid authlevel.")
    except Exception, e:
        print 'Error with supplied arguments'
        sys.exit(e)

    accessToken = getAccessToken(clientID, clientSecret)

    # create the user
    url = 'https://ultrasound.auth0.com/api/users/?access_token=%s' % accessToken

    data = { "email": email,
             "password": password,
             "connection": connection,
             "firstname": firstname,
             "lastname": lastname,
             "phone": int(phone),
             "location": location,
             "authlevel": int(authlevel) }

    request = urllib2.Request(url, json.dumps(data), {'Content-Type': 'application/json'})
    response = None

    try:
        response = urllib2.urlopen(request)
        response_str = response.read()
        response.close()

        if (response.getcode() == 200):
            print 'Success'
    except urllib2.HTTPError, err:
        if (err.code == 500):
            sys.exit('Failed with error code %d. Email already in use?' % err.code)
        else:
            sys.exit('Failed with error code %d' % err.code)

def getAccessToken(clientID, clientSecret):
    url = 'https://ultrasound.auth0.com/oauth/token'
    data = 'client_id=%s&client_secret=%s&type=web_server&grant_type=client_credentials' % (clientID, clientSecret)
    
    request = urllib2.Request(url, data)
    response = urllib2.urlopen(request)
    response_str = response.read()
    response.close()

    response_parsed = json.loads(response_str)

    return response_parsed['access_token']

def authlevelValid(authlevel):
    return (isint(authlevel) and (int(authlevel) >= 1 and int(authlevel) <= 3))

def emailValid(email):
    return (re.compile(r'^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}$', re.IGNORECASE|re.DOTALL).search(email) != None)

def isint(str):
    try: 
        int(str)
        return True
    except ValueError:
        return False

createUser()
