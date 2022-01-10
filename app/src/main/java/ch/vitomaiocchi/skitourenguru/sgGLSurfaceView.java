package ch.vitomaiocchi.skitourenguru;

import android.content.Context;
import android.opengl.GLSurfaceView;

public class sgGLSurfaceView extends GLSurfaceView {

    private final sgGLRenderer renderer;

    public sgGLSurfaceView(Context context) {
        super(context);

        setEGLContextClientVersion(2);
        renderer = new sgGLRenderer(context);
        setRenderer(renderer);
    }

}
