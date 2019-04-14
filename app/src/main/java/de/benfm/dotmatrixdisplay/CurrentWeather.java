package de.benfm.dotmatrixdisplay;

import org.json.JSONObject;

public class CurrentWeather {

    public CurrentWeather(JSONObject json) throws org.json.JSONException
    {
        if (json == null)
        {
            throw new java.lang.IllegalArgumentException("argument is null");
        }
        this.temperatureKelvin = json.getJSONObject("main").getDouble("temp");
    }

    public double temperatureKelvin = 0;
}
