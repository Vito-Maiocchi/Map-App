package ch.vitomaiocchi.skitourenguru.swisstopo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import ch.vitomaiocchi.skitourenguru.util.intVector;
import ch.vitomaiocchi.skitourenguru.opengl.Image;
import ch.vitomaiocchi.skitourenguru.util.vector;

public class Tile {

    public LoadState loadState;
    public Image image;

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

        load();
    }

    public void draw(vector pos, float scale, float ratio) {
        if (loadState != LoadState.LOADED) return;

        float[] matrix = new float[]{
                2/scale*size, 0, 0, 0,
                0, 2/scale/ratio*size, 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1
        };

        image.draw(matrix, new float[]{(this.pos.x - pos.x) * 2/scale ,(pos.y - this.pos.y) *2/ratio/scale});
    }

    public void load() {
        loadState = LoadState.FETCHING_BITMAP;
        BitmapFetcher.queue(this);
    }

    public void unload() {
        loadState = LoadState.UNLOADED;
        image = null;
    }

    /*
    private class BitmapFetcher extends Thread {

        public BitmapFetcher() {
            this.start();
        }

        @Override
        public void run() {

            try {
                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inScaled = false;

                URL imageUrl = new URL(TileSet.url + layer + "/" + tilePos.y + "/" + tilePos.x + ".jpeg");
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

     */

    public int getLayer() {
        return layer;
    }

    public enum LoadState {
        UNLOADED, FETCHING_BITMAP, LOADED
    }

    public intVector getTilePos() {
        return tilePos;
    }
}