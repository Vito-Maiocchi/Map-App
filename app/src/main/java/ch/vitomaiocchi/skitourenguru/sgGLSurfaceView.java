package ch.vitomaiocchi.skitourenguru;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class sgGLSurfaceView extends GLSurfaceView {

    private final sgGLRenderer renderer;

    public sgGLSurfaceView(Context context) {
        super(context);

        setEGLContextClientVersion(2);
        renderer = new sgGLRenderer(context);
        setRenderer(renderer);
    }

    private float previousX;
    private float previousY;

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:

                System.out.println("("+x+"/"+getWidth()+", "+y+"/"+getHeight()+")");

                float dx = x - previousX;
                float dy = y - previousY;

                float r = (float) getHeight() / (float) getWidth();

                renderer.tVector[0] = renderer.tVector[0] + dx / getWidth() * 2;
                renderer.tVector[1] = renderer.tVector[1] - dy / getHeight() * 2 * r;
                break;
        }

        previousX = x;
        previousY = y;
        return true;
    }

}
