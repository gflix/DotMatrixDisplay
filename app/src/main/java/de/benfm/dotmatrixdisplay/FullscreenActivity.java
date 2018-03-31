package de.benfm.dotmatrixdisplay;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.util.Log;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity
{
    private static final String TAG = "FullscreenActivity";
    private View mContentView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");

        setContentView(R.layout.activity_fullscreen);

        mContentView = findViewById(R.id.fullscreen_content);

        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restoreFullscreen();
            }
        });

        mContentView.post(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "post()");
                Log.i(TAG, Integer.toString(mContentView.getWidth()));
                Log.i(TAG, Integer.toString(mContentView.getHeight()));
            }
        });

        restoreFullscreen();
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        Log.i(TAG, "onStart");
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

        mContentView.setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_LOW_PROFILE |
            View.SYSTEM_UI_FLAG_FULLSCREEN |
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }
}
