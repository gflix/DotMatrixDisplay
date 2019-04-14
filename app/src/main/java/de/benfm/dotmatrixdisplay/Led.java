package de.benfm.dotmatrixdisplay;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Represents a LED which is loaded from a given resource and drawn to an explicit position
 */
public class Led {

    public Led(Resources resources, int resourceId)
    {
        this.resources = resources;
        this.resourceId = resourceId;

        /*
        Convert vertices and texture coordinates to appropriate formats
         */
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vertices.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        vertexBuffer = byteBuffer.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        byteBuffer = ByteBuffer.allocateDirect(texture.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        textureBuffer = byteBuffer.asFloatBuffer();
        textureBuffer.put(texture);
        textureBuffer.position(0);
    }

    public void draw(GL10 gl, float x, float y)
    {
        if (textureId <= 0)
        {
            reloadAssets(gl);
        }

        /*
        Move to a given position
         */
        gl.glLoadIdentity();
        gl.glTranslatef(x, y, z);

        /*
        Push the vertices and texture coordinates to the OpenGL engine
         */
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glFrontFace(GL10.GL_CW);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);

        /*
        Finally draw the rectangle with the given texture
         */
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices.length / 3);

        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
    }

    public void reloadAssets(GL10 gl)
    {
        /*
        Set-up a new texture
         */
        int[] textureIds = new int[1];
        gl.glGenTextures(1, textureIds, 0);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIds[0]);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);

        /*
        Initialize the texture with a bitmap loaded from a resource
         */
        Bitmap bitmap = BitmapFactory.decodeResource(resources, resourceId);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();

        textureId = textureIds[0];
    }

    private static final String TAG = "Led";

    private FloatBuffer vertexBuffer;
    private float vertices[] = {
            -0.5f, -0.5f, 0.0f,
            -0.5f,  0.5f, 0.0f,
            0.5f, -0.5f, 0.0f,
            0.5f,  0.5f, 0.0f
    };

    private FloatBuffer textureBuffer;
    private float texture[] = {
            0.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 1.0f,
            1.0f, 0.0f
    };

    /*
    Z depth of the LED plane
     */
    private float z = -28.0f;

    private Resources resources = null;
    private int resourceId = 0;
    private int textureId = -1;
}
