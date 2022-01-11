package ch.vitomaiocchi.skitourenguru;

import java.net.MalformedURLException;
import java.net.URL;

public class Map {

    private Image image;
    private Image image2;

    public Map() {
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

    public void draw(float posX, float posY, float scale, float ratio) {
        //scale = width of the screen
        //ratio = height relative to the width

        scale = scale / 2;

        float[] matrix = new float[]{
                1/scale, 0, 0, 0,
                0, 1/(scale*ratio), 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1
        };

        image.draw(matrix, new float[]{posX/scale,posY/(scale*ratio)});
        image2.draw(matrix, new float[]{(1.5f + posX)/scale, (1 + posY)/(scale*ratio)});
    }

}
