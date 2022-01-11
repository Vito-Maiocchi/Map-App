package ch.vitomaiocchi.skitourenguru;

import static android.view.MotionEvent.INVALID_POINTER_ID;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import androidx.core.view.MotionEventCompat;

public class SurfaceView extends GLSurfaceView {

    private final ch.vitomaiocchi.skitourenguru.Renderer renderer;

    public SurfaceView(Context context) {
        super(context);
        setEGLContextClientVersion(2);
        renderer = new ch.vitomaiocchi.skitourenguru.Renderer();
        setRenderer(renderer);

        ScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
    }

    private ScaleGestureDetector ScaleDetector;
    private int mActivePointerId =  INVALID_POINTER_ID;
    private float mLastTouchX;
    private float mLastTouchY;

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        ScaleDetector.onTouchEvent(e);
        final int action = e.getActionMasked();

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                final int pointerIndex = e.getActionIndex();
                final float x = e.getX(pointerIndex);
                final float y = e.getY(pointerIndex);

                mLastTouchX = x;
                mLastTouchY = y;
                mActivePointerId = e.getPointerId(0);
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                final int pointerIndex = e.findPointerIndex(mActivePointerId);

                final float x = e.getX(pointerIndex);
                final float y = e.getY(pointerIndex);

                final float dx = x - mLastTouchX;
                final float dy = y - mLastTouchY;

                renderer.move(dx, dy);
                invalidate();

                mLastTouchX = x;
                mLastTouchY = y;
                break;
            }

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_POINTER_UP: {

                final int pointerIndex = e.getActionIndex();
                final int pointerId = e.getPointerId(pointerIndex);

                if (pointerId == mActivePointerId) {
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mLastTouchX = e.getX(newPointerIndex);
                    mLastTouchY = e.getY(newPointerIndex);
                    mActivePointerId = e.getPointerId(newPointerIndex);
                }
                break;
            }
        }
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            renderer.scale(detector.getScaleFactor());
            invalidate();
            return true;
        }
    }
}
