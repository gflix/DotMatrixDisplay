package de.benfm.dotmatrixdisplay;

import android.opengl.GLU;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView;
import android.os.Bundle;

import android.content.res.Resources;

class DotMatrixRenderer implements GLSurfaceView.Renderer {
    public DotMatrixRenderer(Resources resources) {
        this.ledOn = new Led(resources, R.mipmap.led_on);
        this.ledOff = new Led(resources, R.mipmap.led_off);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        Log.i(TAG, "onSurfaceCreated()");
        gl.glEnable(GL10.GL_TEXTURE_2D);
        ledOn.reloadAssets(gl);
        ledOff.reloadAssets(gl);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        Log.i(TAG, "onSurfaceChanged(" + width + ", " + height + ")");
        if (height == 0)
        {
            height = 1;
        }

        gl.glViewport(0, 0, width, height);

        float ratio = (float) width / height;
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
//        gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);
        GLU.gluPerspective(gl, 45.0f, ratio, 0.1f, 100.0f);

        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        Log.i(TAG, "onDrawFrame()");
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        for (int i = -18; i <= 18; ++i)
        {
            ledOn.draw(gl, i, 0);
        }

        for (int i = 1; i <= 10; ++i)
        {
            ledOff.draw(gl, -5, -i);
            ledOff.draw(gl, 5, i);
        }
    }

    private static final String TAG = "DotMatrixRenderer";
    private Led ledOn;
    private Led ledOff;
}

public class FullscreenActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create our Preview view and set it as the content of our
        // Activity
        mGLSurfaceView = new GLSurfaceView(this);
        mGLSurfaceView.setRenderer(new DotMatrixRenderer(getResources()));
        mGLSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        setContentView(mGLSurfaceView);

        mGLSurfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restoreFullscreen();
                mGLSurfaceView.requestRender();
            }
        });

        restoreFullscreen();
    }

    @Override
    protected void onResume() {
        // Ideally a game should implement onResume() and onPause()
        // to take appropriate action when the activity looses focus
        super.onResume();
        mGLSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        // Ideally a game should implement onResume() and onPause()
        // to take appropriate action when the activity looses focus
        super.onPause();
        mGLSurfaceView.onPause();
    }

    protected void restoreFullscreen()
    {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.hide();
        }

        mGLSurfaceView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LOW_PROFILE |
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    private static final String TAG = "FullscreenActivity";
    private GLSurfaceView mGLSurfaceView;
}

/*
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
*/
