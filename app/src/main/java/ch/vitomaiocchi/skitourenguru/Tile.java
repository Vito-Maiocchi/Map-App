package ch.vitomaiocchi.skitourenguru;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;

public class Tile {

    private LoadState loadState;

    private Bitmap bitmap;
    private Image image;

    private int layer;
    private float size;
    private vector pos;
    private intVector tilePos;

    public Tile(int layer, int x, int y) {
        this.layer = layer;
        tilePos = new intVector(x,y);

        size = TileSet.scale[layer];
        pos = new vector(
                TileSet.TopLeftCorner.x + (float) tilePos.x * size + size/2,
                TileSet.TopLeftCorner.y + (float) tilePos.y * size + size/2
        );

        image = null;
        bitmap = null;
        loadState = LoadState.UNLOADED;
        load();
    }

    public void draw(vector pos, float scale, float ratio) {
        if (!isLoaded()) return;

        float[] matrix = new float[]{
                2/scale*size, 0, 0, 0,
                0, 2/scale/ratio*size, 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1
        };

        image.draw(matrix, new float[]{(this.pos.x - pos.x) * 2/scale ,(pos.y - this.pos.y) *2/ratio/scale});
    }

    public void load() {
        switch (loadState) {
            case UNLOADED:
                loadState = LoadState.FETCHING_BITMAP;
                new BitmapFetcher();
                break;
            case BITMAP_FETCHED:
                image = new Image(bitmap);
                loadState = LoadState.LOADED;
                break;
        }
    }

    public boolean isLoaded() {
        return  (loadState == LoadState.LOADED);
    }

    public void unload() {
    }

    private class BitmapFetcher extends Thread {

        public BitmapFetcher() {
            this.start();
        }

        @Override
        public void run() {

            try {
                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inScaled = false;

                URL imageUrl = new URL("https://wmts.geo.admin.ch/1.0.0/ch.swisstopo.pixelkarte-farbe/default/current/21781/" + layer + "/" + tilePos.y + "/" + tilePos.x + ".jpeg");
                HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
                conn.setConnectTimeout(30000);
                conn.setReadTimeout(30000);
                BufferedInputStream bs = new BufferedInputStream(conn.getInputStream());
                bitmap = BitmapFactory.decodeStream(bs);
            } catch (IOException e) {
                loadState = LoadState.UNLOADED;
                return;
            }

            loadState = LoadState.BITMAP_FETCHED;
        }
    }

    private enum LoadState {
        UNLOADED, FETCHING_BITMAP, BITMAP_FETCHED, LOADED
    }
}