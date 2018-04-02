package de.benfm.dotmatrixdisplay;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.util.Log;

import java.util.Random;

public class FullscreenActivity extends AppCompatActivity
{
    private static final String TAG = "FullscreenActivity";
    private DotMatrixLayout mGridLayout;
    private Random random = new Random();
    private Font tinyFont;
    private Font smallFont;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");

        try
        {
            tinyFont = new Font(getResources().openRawResource(R.raw.font_3x5));
            smallFont = new Font(getResources().openRawResource(R.raw.font_4x6));
        }
        catch (Exception e)
        {
            Log.e(TAG, "Caught exception: " + e.getMessage());
        }

        setContentView(R.layout.activity_fullscreen);
        mGridLayout = (DotMatrixLayout) findViewById(R.id.grid_layout);

        mGridLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restoreFullscreen();

                if (tinyFont == null)
                {
                    return;
                }

                try
                {
                    mGridLayout.putString(0, 0, smallFont, "0123.45");
                    mGridLayout.putString(0, 7, smallFont, "67:89");
                    mGridLayout.putString(7, 16, tinyFont, "21. APR");
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        restoreFullscreen();
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        Log.i(TAG, "onStart()");
        restoreFullscreen();
    }

    protected void restoreFullscreen()
    {
        Log.i(TAG, "restoreFullscreen");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.hide();
        }

        mGridLayout.setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_LOW_PROFILE |
            View.SYSTEM_UI_FLAG_FULLSCREEN |
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }
}
