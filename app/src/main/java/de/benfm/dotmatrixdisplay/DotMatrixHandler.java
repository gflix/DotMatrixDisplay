package de.benfm.dotmatrixdisplay;

import android.content.res.Resources;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.util.Log;

import java.text.SimpleDateFormat;
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
    }

    public void run()
    {
        if (dotMatrix == null)
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

        synchronized (dotMatrix)
        {
            dotMatrix.putString(5, 2, normalBoldFont, timeString);
        }

        glSurfaceView.requestRender();
    }

    private static final String TAG = "DotMatrixHandler";
    private Handler handler;
    private DotMatrix dotMatrix;
    private GLSurfaceView glSurfaceView;
    private Font tinyFont;
    private Font smallFont;
    private Font normalBoldFont;
}
