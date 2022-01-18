package ch.vitomaiocchi.skitourenguru;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class Map {

    private SwissTopo swissTopo;

    private Image debug_image;

    public Map() {
        swissTopo = new SwissTopo();

        Bitmap bitmap = null;
        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;

            URL imageUrl = new URL("https://obj.shine.cn/files/2020/07/10/93c850ab-8ace-4b76-a0c5-834df5e9bf6a_0.jpg");
            HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            BufferedInputStream bs = new BufferedInputStream(conn.getInputStream());
            bitmap = BitmapFactory.decodeStream(bs);
        } catch (IOException e) {
        }
        debug_image = new Image(bitmap);
    }

    public void draw(vector pos, float scale, float ratio) {
        //System.out.println(ProjectionUtil.wmToCh(new vector(958207f, 5847040f)));
        //System.out.println(ProjectionUtil.elToCh(new vector(0.820305f, 0.139626f)));
        // Sött öppe das si : 5643382.69, -2976576.05;
        //System.out.println(ProjectionUtil.elToCh(new vector(0.821317799f , 0.148115967f)));
        swissTopo.draw(pos, scale, ratio);

        float[] matrix = new float[]{
                2f/10f, 0, 0, 0,
                0, 2f/ratio/10f, 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1
        };

        vector vector = ProjectionUtil.wmToCh(new vector(756127.0f, 5757769.0f));
        vector = new vector(958207f, 5847040f);

        System.out.println(vector);
        debug_image.draw(matrix, new float[]{(vector.x - pos.x)*2/scale ,(pos.y - vector.y)/ratio*2/scale});
    }

}
