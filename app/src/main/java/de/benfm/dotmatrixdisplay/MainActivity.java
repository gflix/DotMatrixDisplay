package de.benfm.dotmatrixdisplay;

import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.util.Log;

import android.opengl.GLSurfaceView;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dotMatrix = new DotMatrix();

        glSurfaceView = new GLSurfaceView(this);
        glSurfaceView.setRenderer(new DotMatrixRenderer(getResources(), dotMatrix));
        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        setContentView(glSurfaceView);
        glSurfaceView.setKeepScreenOn(true);

        handler = new Handler();
        dotMatrixHandler = new DotMatrixHandler(this.getResources(), handler, dotMatrix, glSurfaceView);

        glSurfaceView.setOnClickListener(new View.OnClickListener() {
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
//        Log.i(TAG, "onStart()");

        restoreFullscreen();
        dotMatrixHandler.run();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
//        Log.i(TAG, "onStop()");

        handler.removeCallbacks(dotMatrixHandler);
    }

    @Override
    protected void onResume() {
        super.onResume();
        glSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        glSurfaceView.onPause();
    }

    protected void restoreFullscreen()
    {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.hide();
        }

        glSurfaceView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LOW_PROFILE |
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    private static final String TAG = "MainActivity";
    private GLSurfaceView glSurfaceView;
    private DotMatrix dotMatrix;
    private DotMatrixHandler dotMatrixHandler;
    private Handler handler;
}
