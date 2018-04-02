package de.benfm.dotmatrixdisplay;

import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.util.Log;

import java.util.Random;

public class FullscreenActivity extends AppCompatActivity
{
    private static final String TAG = "FullscreenActivity";
    private DotMatrixLayout dotMatrixLayout;
    private DotMatrixHandler dotMatrixHandler;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");

        handler = new Handler();

        setContentView(R.layout.activity_fullscreen);
        dotMatrixLayout = (DotMatrixLayout) findViewById(R.id.grid_layout);
        dotMatrixHandler = new DotMatrixHandler(this, handler, dotMatrixLayout);

        dotMatrixLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restoreFullscreen();
            }
        });

//        restoreFullscreen();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        Log.i(TAG, "onStart()");

        restoreFullscreen();
        handler.postDelayed(dotMatrixHandler, 2000);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        Log.i(TAG, "onStop()");

        handler.removeCallbacks(dotMatrixHandler);
    }

    protected void restoreFullscreen()
    {
        Log.i(TAG, "restoreFullscreen");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.hide();
        }

        dotMatrixLayout.setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_LOW_PROFILE |
            View.SYSTEM_UI_FLAG_FULLSCREEN |
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }
}
