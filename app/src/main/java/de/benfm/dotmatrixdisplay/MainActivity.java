package de.benfm.dotmatrixdisplay;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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
        dotMatrixHandler = new DotMatrixHandler(this, handler, dotMatrix, glSurfaceView);

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

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_refresh:
                // User chose the "Refresh" item
                dotMatrixHandler.refresh();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
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
