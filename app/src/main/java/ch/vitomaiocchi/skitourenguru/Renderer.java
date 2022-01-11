package ch.vitomaiocchi.skitourenguru;

import android.opengl.GLSurfaceView;

import java.net.MalformedURLException;
import java.net.URL;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class Renderer implements GLSurfaceView.Renderer {

    private float posX;
    private float posY;
    private float scale;
    private float width;
    private float height;

    private Image image;
    private Image image2;

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        Image.createShader();

        URL url = null;
        URL url2 = null;
        try {
            url = new URL("https://wmts.geo.admin.ch/1.0.0/ch.swisstopo.pixelkarte-farbe/default/current/21781/24/700/400.jpeg");
            url2 = new URL("https://cdn.discordapp.com/attachments/504344717698400258/930056904909156392/artworks-aXCAepjE3p3Dtsw6-i07GrQ-t500x500.jpg");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        image = new Image(url);
        image2 = new Image(url2);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void onDrawFrame(GL10 gl10) {

        float[] matrix = new float[]{
                1, 0, 0, 0,
                0, 1, 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1
        };

        float[] vector = new float[]{0,0};

        image.draw(matrix, vector);
        image2.draw(matrix, new float[]{0.5f,0});
    }

    public void move(float x, float y) {

    }

    public void scale(float scale) {

    }
}
