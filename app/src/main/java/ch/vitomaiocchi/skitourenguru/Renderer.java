package ch.vitomaiocchi.skitourenguru;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class Renderer implements GLSurfaceView.Renderer {

    private float posX;
    private float posY;
    private float scale;
    private int width;
    private int height;

    private Map map;

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        map = new Map();
        posY = 0;
        posX = 0;
        scale = 1;
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        this.width = width;
        this.height = height;
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        float ratio = (float) height / (float) width;
        map.draw(posX, posY, 1/scale, ratio);
    }

    public void move(float x, float y) {
        float ratio = (float) height / (float) width;
        posX += x / (float) width / scale;
        posY += y / (float) height * -ratio / scale;
    }

    public void scale(float scale, float x, float y) {
        //TODO richtig ine zoome
        this.scale *= scale;
    }
}
