package de.benfm.dotmatrixdisplay;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.opengl.GLSurfaceView;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class DotMatrixHandler implements Runnable {

    public DotMatrixHandler(Context context, Handler handler, DotMatrix dotMatrix, GLSurfaceView glSurfaceView)
    {
        this.context = context;
        this.handler = handler;
        this.dotMatrix = dotMatrix;
        this.glSurfaceView = glSurfaceView;

        try
        {
            tinyFont = new Font(context.getResources().openRawResource(R.raw.font_3x5));
            smallFont = new Font(context.getResources().openRawResource(R.raw.font_4x6));
            normalBoldFont = new Font(context.getResources().openRawResource(R.raw.font_5x7_bold));
        }
        catch (Exception e)
        {
            Log.e(TAG, "Caught exception: " + e.getMessage());
        }

        retrieveCurrentWeatherTask = new RetrieveCurrentWeatherTask();
    }

    public void run()
    {
        if (dotMatrix == null)
        {
            return;
        }

        try
        {
            Date now = GregorianCalendar.getInstance().getTime();
            updateTime(now);
            updateCurrentWeather(
                now,
                getPreference(R.string.pref_location_id_key, R.string.pref_location_id_default),
                getPreference(R.string.pref_api_key_key, R.string.pref_api_key_default));

            updateDotMatrixDisplay();
            handler.postDelayed(this, 2000);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void updateDotMatrixDisplay() throws Exception
    {
        synchronized (dotMatrix)
        {
            dotMatrix.reset();
            if (time != null)
            {
                dotMatrix.putString(6, 2, normalBoldFont, time);
            }
            if (currentWeather != null)
            {
                String text =
                        Integer.toString((int) currentWeather.getTemperatureCelsius()) + "°C";
                Point textDimension = smallFont.getTextDimension(text);

                dotMatrix.putString((dotMatrix.getColumnCount() - textDimension.x) / 2,12, smallFont,
                        text);
            }
        }

        glSurfaceView.requestRender();
    }

    private void updateTime(Date now)
    {
        time = new SimpleDateFormat("HH:mm").format(now);
    }

    private void updateCurrentWeather(Date now, String openWeatherLocationId, String openWeatherApiKey)
    {
        if (now.getTime() > lastWeatherUpdate.getTime() + weatherUpdateIntervalMilliseconds)
        {
            AsyncTask.Status taskStatus = retrieveCurrentWeatherTask.getStatus();
            if (taskStatus == AsyncTask.Status.PENDING)
            {
                Log.i(TAG, "updateCurrentWeather(GET)");
                retrieveCurrentWeatherTask.openWeatherLocationId = openWeatherLocationId;
                retrieveCurrentWeatherTask.openWeatherApiKey = openWeatherApiKey;
                retrieveCurrentWeatherTask.execute();
            }
            else if (taskStatus == AsyncTask.Status.RUNNING)
            {
                Log.i(TAG, "updateCurrentWeather(still RUNNING)");
            }
            else if (taskStatus == AsyncTask.Status.FINISHED)
            {
                try
                {
                    Exception thrownException = retrieveCurrentWeatherTask.getException();
                    if (thrownException instanceof Exception)
                    {
                        throw thrownException;
                    }
                    currentWeather = new CurrentWeather(retrieveCurrentWeatherTask.get());
                    Log.i(TAG, "updateCurrentWeather(FINISHED)");
                }
                catch (Exception e)
                {
                    currentWeather = null;

                    Toast toast = Toast.makeText(
                        context,
                        "updateCurrentWeather(" + e.toString() + ")",
                        Toast.LENGTH_SHORT);
                    toast.show();
                }
                lastWeatherUpdate = now;
                retrieveCurrentWeatherTask = new RetrieveCurrentWeatherTask();
            }
        }
    }

    private String getPreference(int preferenceResourceId, int preferenceDefaultResourceId)
    {
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(
                context.getResources().getString(preferenceResourceId),
                context.getResources().getString(preferenceDefaultResourceId));
    }

    private static final String TAG = "DotMatrixHandler";
    private Context context;
    private Handler handler;
    private DotMatrix dotMatrix;
    private GLSurfaceView glSurfaceView;
    private Font tinyFont;
    private Font smallFont;
    private Font normalBoldFont;

    private String time = null;

    private static final long weatherUpdateIntervalMilliseconds = 600 * 1000;
    private Date lastWeatherUpdate = new Date(0);
    private RetrieveCurrentWeatherTask retrieveCurrentWeatherTask;
    private CurrentWeather currentWeather = null;
}
