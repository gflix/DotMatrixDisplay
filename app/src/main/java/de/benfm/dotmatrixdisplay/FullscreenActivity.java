package de.benfm.dotmatrixdisplay;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity
{
    private static final String TAG = "FullscreenActivity";
    private GridLayout mGridLayout;
    private LinearLayout leds[][];

    private final int columnCount = 32;
    private final int rowCount = 18;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");

        setContentView(R.layout.activity_fullscreen);
        mGridLayout = (GridLayout) findViewById(R.id.grid_layout);

        mGridLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restoreFullscreen();
            }
        });

        restoreFullscreen();
    }

    @Override
    protected void onStart()
    {
        super.onStart();

//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        Log.i(TAG, "onStart()");
        restoreFullscreen();

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mGridLayout.removeAllViews();
        mGridLayout.setColumnCount(columnCount);
        mGridLayout.setRowCount(rowCount);

        leds = new LinearLayout[rowCount][columnCount];
        for (int y = 0; y < rowCount; ++y)
        {
            for (int x = 0; x < columnCount; ++x)
            {
                leds[y][x] = (LinearLayout) inflater.inflate(R.layout.led_off, null);
                mGridLayout.addView(
                    leds[y][x],
                    new GridLayout.LayoutParams(
                        GridLayout.spec(y, GridLayout.CENTER),
                        GridLayout.spec(x, GridLayout.CENTER)));
            }
        }
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
