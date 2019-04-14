package de.benfm.dotmatrixdisplay;

import android.os.AsyncTask;

import org.json.JSONObject;

public class RetrieveCurrentWeatherTask extends AsyncTask<Void, Void, JSONObject> {

    protected JSONObject doInBackground(Void... none)
    {
        try
        {
            return JsonHttpClient.get("http://nas.benfm.de/public/test.json");
        }
        catch (Exception e)
        {
            this.exception = e;
            return null;
        }
    }

    public Exception getException()
    {
        return this.exception;
    }

    private Exception exception = null;
}
