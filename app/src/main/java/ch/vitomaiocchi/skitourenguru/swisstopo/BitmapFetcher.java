package ch.vitomaiocchi.skitourenguru.swisstopo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import ch.vitomaiocchi.skitourenguru.opengl.Image;

public class BitmapFetcher {
    private static ArrayList<BitmapFetcherThread> fetcherThreads = new ArrayList<>();
    private static ArrayList<Tile>[] layers = new ArrayList[(TileSet.scale.length - TileSet.minLevel)];
    private static final int maxFetcherThreads = 10;
    private static SwissTopo swissTopo;

    public static void initialize(SwissTopo topo) {
        int i = 0;
        while (true) {
            if (i < layers.length) {
                layers[i] = new ArrayList<>();
                i++;
            } else {
                swissTopo = topo;
                return;
            }
        }
    }

    public static void queue(Tile tile) {
        layers[tile.getLayer() - TileSet.minLevel].add(tile);
    }

    public static void update() {
        for (int i = 0; i < fetcherThreads.size(); i++) {
            if (fetcherThreads.get(i).finished) {
                BitmapFetcherThread thread = fetcherThreads.get(i);
                if (thread.bitmap == null) {
                    thread.tile.loadState = Tile.LoadState.UNLOADED;
                } else {
                    thread.tile.image = new Image(thread.bitmap);
                    thread.tile.loadState = Tile.LoadState.LOADED;
                    thread.bitmap.recycle();
                }
                fetcherThreads.remove(i);
            }
        }
        for (int i2 = swissTopo.targetLevel; i2 > TileSet.minLevel; i2--) {
            while (layers[i2 - TileSet.minLevel].size() > 0 && fetcherThreads.size() < 10) {
                fetcherThreads.add(new BitmapFetcherThread(layers[i2 - TileSet.minLevel].get(0)));
                layers[i2 - TileSet.minLevel].remove(0);
            }
        }
    }

    private static class BitmapFetcherThread extends Thread {
        public Bitmap bitmap = null;
        public boolean finished = false;
        public Tile tile;

        public BitmapFetcherThread(Tile tile) {
            this.tile = tile;
            start();
        }

        public void run() {
            try {
                new BitmapFactory.Options().inScaled = false;
                HttpURLConnection conn = (HttpURLConnection) new URL(TileSet.url + this.tile.getLayer() + "/" + this.tile.getTilePos().y + "/" + this.tile.getTilePos().x + ".jpeg").openConnection();
                conn.setConnectTimeout(30000);
                conn.setReadTimeout(30000);
                this.bitmap = BitmapFactory.decodeStream(new BufferedInputStream(conn.getInputStream()));
            } catch (IOException e) {
                this.bitmap = null;
            }
            this.finished = true;
        }
    }
}
