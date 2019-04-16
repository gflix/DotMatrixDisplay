package de.benfm.dotmatrixdisplay;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import android.opengl.GLSurfaceView;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dotMatrix = new DotMatrix();
        dotMatrixRenderer = new DotMatrixRenderer(getResources(), dotMatrix);

        setContentView(R.layout.main_activity);

        glSurfaceView = (GLSurfaceView) findViewById(R.id.glSurfaceView);
        glSurfaceView.setRenderer(new DotMatrixRenderer(getResources(), dotMatrix));
        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        handler = new Handler();
        dotMatrixHandler = new DotMatrixHandler(this.getResources(), handler, dotMatrix, glSurfaceView);

        glSurfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restoreFullscreen(false);
            }
        });

        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
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
            toolbar.setVisibility(View.GONE);
            systemUiVisibility |= View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
        }
        else
        {
            toolbar.setVisibility(View.VISIBLE);
        }
        glSurfaceView.setSystemUiVisibility(systemUiVisibility);
        toogleFullscreen = !toogleFullscreen;
    }

    private static final String TAG = "MainActivity";
    private GLSurfaceView glSurfaceView;
    private DotMatrix dotMatrix;
    private DotMatrixRenderer dotMatrixRenderer;
    private DotMatrixHandler dotMatrixHandler;
    private Handler handler;

    private Toolbar toolbar;

    private boolean toogleFullscreen = false;
}
