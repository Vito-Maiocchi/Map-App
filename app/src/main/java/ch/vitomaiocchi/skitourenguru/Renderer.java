package ch.vitomaiocchi.skitourenguru;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import java.util.Vector;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class Renderer implements GLSurfaceView.Renderer {

    private vector pos;
    private vector dimensions;

    private float scale; //     scale of the width of the screen
    private float ratio;//      height of the screen relative to width

    private Map map;

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        GLES20.glClearColor(0.95f, 0.9f,1.0f, 1);

        map = new Map();
        pos = new vector(TileSet.TopLeftCorner.x, TileSet.TopLeftCorner.y);
        scale = 6250000;
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        dimensions = new vector(width, height);
        ratio = (float) height / (float) width;
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
       map.draw(new vector(pos.x, pos.y), scale, ratio);
    }

    public void move(vector pos) {
        this.pos.add( pos.scaleToDimensions(dimensions, scale) );
    }


    vector focus;

    public void scale(float scale, vector focus) {
        this.scale = this.scale / scale;
        pos.add(vector.subtract(this.focus, focus.transformToDimensions(pos, dimensions, this.scale)));
    }

    public void scale_begin(vector focus) {
        this.focus = focus.transformToDimensions(pos, dimensions, scale);
    }

}
