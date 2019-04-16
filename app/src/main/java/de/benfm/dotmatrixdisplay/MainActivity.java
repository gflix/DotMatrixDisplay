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
                restoreFullscreen(false);
            }
        });
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        restoreFullscreen(true);
        dotMatrixHandler.run();
    }

    @Override
    protected void onStop()
    {
        super.onStop();

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

    protected void restoreFullscreen(boolean forceHide)
    {
        ActionBar actionBar = getSupportActionBar();
        int systemUiVisibility =
            View.SYSTEM_UI_FLAG_LOW_PROFILE |
            View.SYSTEM_UI_FLAG_FULLSCREEN |
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

        if (forceHide)
        {
            toogleFullscreen = true;
        }

        if (toogleFullscreen)
        {
            if (actionBar != null)
            {
                actionBar.hide();
            }

            systemUiVisibility |= View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
        }
        else
        {
            if (actionBar != null)
            {
                actionBar.show();
            }
        }
        glSurfaceView.setSystemUiVisibility(systemUiVisibility);
        toogleFullscreen = !toogleFullscreen;
    }

    private static final String TAG = "MainActivity";
    private GLSurfaceView glSurfaceView;
    private DotMatrix dotMatrix;
    private DotMatrixHandler dotMatrixHandler;
    private Handler handler;

    private boolean toogleFullscreen = false;
}
