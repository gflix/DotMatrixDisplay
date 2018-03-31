package de.benfm.dotmatrixdisplay;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.util.Log;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity implements SurfaceHolder.Callback
{
    private static final String TAG = "FullscreenActivity";
    private View mContentView;
    private SurfaceView mDotMatrixView;
    private SurfaceHolder mDotMatrixHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");

        setContentView(R.layout.activity_fullscreen);

        mContentView = findViewById(R.id.fullscreen_content);
        mDotMatrixView = findViewById(R.id.dot_matrix_view);
        mDotMatrixHolder = mDotMatrixView.getHolder();
        mDotMatrixHolder.addCallback(this);

        mContentView.setOnClickListener(new View.OnClickListener() {
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

    public void surfaceCreated(SurfaceHolder surfaceHolder)
    {
        Log.i(TAG, "surfaceCreated");
    }

    public void surfaceDestroyed(SurfaceHolder surfaceHolder)
    {
        Log.i(TAG, "surfaceDestroyed");
    }

    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height)
    {
        Log.i(TAG, "surfaceChanged(width=" + Integer.toString(width) +
            ", height=" + Integer.toString(height) + ")");
        redrawOnSurface(mDotMatrixHolder);
    }

    protected void redrawOnSurface(SurfaceHolder surfaceHolder)
    {
        Canvas canvas = surfaceHolder.lockCanvas();
        if (canvas != null)
        {
            redrawOnCanvas(canvas);
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    protected void redrawOnCanvas(Canvas canvas)
    {
        Log.i(TAG, "redrawOnCanvas");
        canvas.drawColor(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_OVER);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(3);
        paint.setColor(0xdfdd1010);
        canvas.drawCircle(
                canvas.getWidth() / 2,
                canvas.getHeight() / 2,
                canvas.getHeight() / 2,
                paint);
    }
}
