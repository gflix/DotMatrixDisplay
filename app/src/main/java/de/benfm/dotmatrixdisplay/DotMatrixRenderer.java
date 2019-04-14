package de.benfm.dotmatrixdisplay;

import android.content.res.Resources;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

class DotMatrixRenderer implements GLSurfaceView.Renderer {

    public DotMatrixRenderer(Resources resources, DotMatrix dotMatrix) {
        this.dotMatrix = dotMatrix;
        this.ledOn = new Led(resources, R.mipmap.led_on);
        this.ledOff = new Led(resources, R.mipmap.led_off);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
//        Log.i(TAG, "onSurfaceCreated()");
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
//        Log.i(TAG, "onDrawFrame()");
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        synchronized (dotMatrix)
        {
            dotMatrix.draw(gl, ledOn, ledOff);
        }
    }

    private static final String TAG = "DotMatrixRenderer";
    private DotMatrix dotMatrix;
    private Led ledOn;
    private Led ledOff;
}
