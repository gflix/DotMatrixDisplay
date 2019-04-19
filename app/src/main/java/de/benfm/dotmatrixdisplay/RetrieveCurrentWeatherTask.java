package de.benfm.dotmatrixdisplay;

import android.os.AsyncTask;

import org.json.JSONObject;

public class RetrieveCurrentWeatherTask extends AsyncTask<Void, Void, JSONObject> {

    protected JSONObject doInBackground(Void... none)
    {
        try
        {
            if (!(openWeatherLocationId instanceof String) || openWeatherLocationId.isEmpty())
            {
                throw new IllegalArgumentException("OpenWeather location ID is empty");
            }
            if (!(openWeatherApiKey instanceof String) || openWeatherApiKey.isEmpty())
            {
                throw new IllegalArgumentException("OpenWeather API key is empty");
            }
            String url =
                "http://api.openweathermap.org/data/2.5/weather?id=" +
                openWeatherLocationId +
                "&APPID=" +
                openWeatherApiKey;
            return JsonHttpClient.get(url);
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

    public String openWeatherLocationId;
    public String openWeatherApiKey;

    private Exception exception = null;
}
