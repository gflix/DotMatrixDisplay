package de.benfm.dotmatrixdisplay;

import org.json.JSONObject;

public class JsonHttpClient {

    static public JSONObject get(String requestUrl) throws java.io.IOException, org.json.JSONException
    {
        return new JSONObject(HttpClient.get(requestUrl));
    }

}
