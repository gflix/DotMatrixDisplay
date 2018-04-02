package de.benfm.dotmatrixdisplay;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

public class DotMatrixHandler implements Runnable {

    private static final String TAG = "DotMatrixHandler";
    private Handler handler;
    private DotMatrixLayout dotMatrixLayout;
    private Font tinyFont;
    private Font smallFont;
    private Font normalBoldFont;
    private int i = 0;

    public DotMatrixHandler(Context context, Handler handler, DotMatrixLayout dotMatrixLayout)
    {
        this.handler = handler;
        this.dotMatrixLayout = dotMatrixLayout;

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
    }

    public void run()
    {
        if (dotMatrixLayout == null)
        {
            return;
        }

        try
        {
            printTime();
            handler.postDelayed(this, 2000);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void printTime() throws Exception
    {
        String timeString = new SimpleDateFormat("HH:mm").
                format(GregorianCalendar.getInstance().getTime());

        dotMatrixLayout.putString(5, 2, normalBoldFont, timeString);
    }
}
