package de.benfm.dotmatrixdisplay;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClient {

    static public String get(String requestUrl) throws java.io.IOException
    {
        URL url = new URL(requestUrl);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");

        InputStream inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
        return convertStreamToString(inputStream);
    }

    static private String convertStreamToString(InputStream stream) throws java.io.IOException
    {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder stringBuilder = new StringBuilder();

        String line;
        while ((line = bufferedReader.readLine()) != null)
        {
            stringBuilder.append(line).append('\n');
        }
        stream.close();

        return stringBuilder.toString();
    }
}
