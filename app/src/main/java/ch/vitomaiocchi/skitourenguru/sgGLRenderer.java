package ch.vitomaiocchi.skitourenguru;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class sgGLRenderer implements GLSurfaceView.Renderer {

    private testSquare triangle;
    private float[] sMatrix;
    public float[] tVector;

    private long last;

    Context context;

    public sgGLRenderer(Context context) {
        super();
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        triangle = new testSquare(context);

        tVector = new float[]{0,0};
        //last = System.currentTimeMillis();
    }

    double x = 0;

    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        /*
        long delta = System.currentTimeMillis() - last;
        last = System.currentTimeMillis();

        x = x + ((double) delta) / 500;

        tVector = new float[]{ (float) Math.cos(x), (float) Math.sin(x) };
         */

        triangle.draw(sMatrix, tVector);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        float r = (float) width / (float) height;
        float scale = 1;

        sMatrix = new float[]{
                1 * scale , 0, 0, 0,
                0, r * scale, 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1
        };
    }

}
