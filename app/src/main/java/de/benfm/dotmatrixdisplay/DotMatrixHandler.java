package de.benfm.dotmatrixdisplay;

import android.content.res.Resources;
import android.graphics.Point;
import android.opengl.GLSurfaceView;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class DotMatrixHandler implements Runnable {

    public DotMatrixHandler(Resources resources, Handler handler, DotMatrix dotMatrix, GLSurfaceView glSurfaceView)
    {
        this.handler = handler;
        this.dotMatrix = dotMatrix;
        this.glSurfaceView = glSurfaceView;

        try
        {
            tinyFont = new Font(resources.openRawResource(R.raw.font_3x5));
            smallFont = new Font(resources.openRawResource(R.raw.font_4x6));
            normalBoldFont = new Font(resources.openRawResource(R.raw.font_5x7_bold));
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
            updateCurrentWeather(now);

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

    private void updateCurrentWeather(Date now)
    {
        if (now.getTime() > lastWeatherUpdate.getTime() + weatherUpdateIntervalMilliseconds)
        {
            AsyncTask.Status taskStatus = retrieveCurrentWeatherTask.getStatus();
            if (taskStatus == AsyncTask.Status.PENDING)
            {
                Log.i(TAG, "updateCurrentWeather(GET)");
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
                    currentWeather = new CurrentWeather(retrieveCurrentWeatherTask.get());
                    Log.i(TAG, "updateCurrentWeather(FINISHED)");
                }
                catch (Exception e)
                {
                    currentWeather = null;
                    Log.e(TAG, "updateCurrentWeather(" + e.toString() + ")");
                }
                lastWeatherUpdate = now;
                retrieveCurrentWeatherTask = new RetrieveCurrentWeatherTask();
            }
        }
    }

    private static final String TAG = "DotMatrixHandler";
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
