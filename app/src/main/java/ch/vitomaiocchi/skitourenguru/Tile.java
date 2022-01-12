package ch.vitomaiocchi.skitourenguru;

import java.net.MalformedURLException;
import java.net.URL;

public class Tile {

    private boolean loaded;
    private boolean loading;

    private Image image;
    private int layer;
    private int tile_x;
    private int tile_y;

    private float size;
    private vector pos;

    public Tile(int layer, int x, int y) {
        loaded = false;
        image = null;
        this.layer = layer;
        tile_x = x;
        tile_y = y;

        size = TileSet.scale[layer];
        pos = new vector(
                TileSet.TopLeftCorner.x + (float) tile_x * size + size/2,
                TileSet.TopLeftCorner.y + (float) tile_y * size + size/2
        );

        load();

    }

    public void draw(vector pos, float scale, float ratio) {
        if (loaded == false) return;

        float[] matrix = new float[]{
                2/scale*size, 0, 0, 0,
                0, 2/scale/ratio*size, 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1
        };

        image.draw(matrix, new float[]{(this.pos.x - pos.x) * 2/scale ,(pos.y - this.pos.y) *2/ratio/scale});
    }

    public void load() {
        if (loading) return;
        loading = true;
        new ImageLoader(this);
    }

    public boolean isLoaded() {
        return  loaded;
    }

    public void unload() {
        //TODO: unload wenns für performace nötig isch
    }

    private class ImageLoader extends Thread {

        Tile tile;

        public ImageLoader(Tile tile) {
            this.tile = tile;
            this.run();
        }

        @Override
        public void run() {
            URL url = null;
            try {
                url = new URL("https://wmts.geo.admin.ch/1.0.0/ch.swisstopo.pixelkarte-farbe/default/current/21781/"+tile.layer+"/"+tile.tile_y+"/"+tile.tile_x+".jpeg");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            tile.image = new Image(url);
            tile.loaded = true;
            tile.loading = false;
        }
    }

}
