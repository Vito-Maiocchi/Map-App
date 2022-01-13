package ch.vitomaiocchi.skitourenguru;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Tile {

    private static LoaderScheduler loaderScheduler = new LoaderScheduler();
    private LoadState loadState;

    private Bitmap bitmap;
    private Image image;

    private int layer;
    private float size;
    private vector pos;
    private intVector tilePos;

    public Tile(int layer, int x, int y) {
        this.layer = layer;
        tilePos.x = x;
        tilePos.y = y;

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
                break;
            case BITMAP_FETCHED:
                break;
        }


        if (loading) return;
        loading = true;
        //new ImageLoader(this);
        loaderScheduler.schedule(this);
    }

    public boolean isLoaded() {
        return  (loadState == LoadState.LOADED);
    }

    public void unload() {
        //TODO: unload wenns für performace nötig isch
    }

    private static class LoaderScheduler extends Thread {

        static ArrayList<Tile> tiles = new ArrayList<>();

        public LoaderScheduler() {
            this.start();
        }

        public void schedule(Tile tile) {
            tiles.add(tile);
        }

        @Override
        public void run() {
            while (true) {
                if(tiles.size() > 0) {
                    System.out.println("LOAD TILE: "+tiles.get(0).layer+"/"+tiles.get(0).tile_y+"/"+tiles.get(0).tile_x);
                    URL url = null;
                    try {
                        url = new URL("https://wmts.geo.admin.ch/1.0.0/ch.swisstopo.pixelkarte-farbe/default/current/21781/"+tiles.get(0).layer+"/"+tiles.get(0).tile_y+"/"+tiles.get(0).tile_x+".jpeg");
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    tiles.get(0).image = new Image(url);
                    tiles.get(0).loaded = true;
                    tiles.get(0).loading = false;
                    tiles.remove(0);

                    final BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inScaled = false;

                    Bitmap bitmap = null;
                    try {
                        InputStream is = image.openStream();
                        bitmap = BitmapFactory.decodeStream(is);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private enum LoadState {
        UNLOADED, FETCHING_BITMAP, BITMAP_FETCHED, LOADED
    }
}