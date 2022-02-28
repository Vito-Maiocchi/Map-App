package ch.vitomaiocchi.skitourenguru.swisstopo;

import java.util.ArrayList;

import ch.vitomaiocchi.skitourenguru.util.intVector;
import ch.vitomaiocchi.skitourenguru.util.vector;

public class LevelGrid {

    SwissTopo swissTopo;
    int level;

    Tile[][] tiles;
    float size;

    public LevelGrid(int level, SwissTopo swissTopo) {
        this.level = level;
        this.swissTopo = swissTopo;

        tiles = new Tile[TileSet.MatrixWidth[level]][TileSet.MatrixHeight[level]];
        size = TileSet.scale[level];
    }

    public void drawScreen(vector pos, float scale, float ratio) {
        int min_x = (int) Math.floor((pos.x - scale/2 - TileSet.TopLeftCorner.x) / size);
        int max_x = (int) Math.ceil((pos.x + scale/2 - TileSet.TopLeftCorner.x) / size);
        int min_y = (int) Math.floor((pos.y - scale*ratio/2 - TileSet.TopLeftCorner.y) / size);
        int max_y = (int) Math.ceil((pos.y + scale*ratio/2 - TileSet.TopLeftCorner.y) / size);

        ArrayList<intVector> screen = new ArrayList<>();

        for (int x = min_x; x <= max_x; x++)
            for (int y = min_y; y <= max_y; y++) {
                screen.add(new intVector(x, y));
            }

        intVector[] vectors = screen.toArray(new intVector[screen.size()]);
        drawTiles(pos, scale, ratio, vectors);
    }

    public void drawTiles(vector pos, float scale, float ratio, intVector[] tiles) {
        /*
        ArrayList<intVector> unloaded = new ArrayList<>(); //clusters that contain unloaded
        ArrayList<intVector> loaded = new ArrayList<>();   //loaded Tiles

        for (int i = 0; i < tiles.length; i++) {
            if( 0 <= tiles[i].x && tiles[i].x < this.tiles.length &&  0 <= tiles[i].y && tiles[i].y < this.tiles[0].length) {
                Tile tile = this.tiles[tiles[i].x][tiles[i].y];
                if (tile == null) {
                    intVector[] cluster = getCluster(tiles[i]);
                    for (int c = 0; c < cluster.length; c++) if (!cluster[c].containedIn(unloaded)) unloaded.add(cluster[c]);
                    System.out.println("NEW TILE");
                    this.tiles[tiles[i].x][tiles[i].y] = new Tile(level, tiles[i].x, tiles[i].y);
                } else if (!tile.isLoaded()) {
                    intVector[] cluster = getCluster(tiles[i]);
                    for (int c = 0; c < cluster.length; c++) if (!cluster[c].containedIn(unloaded)) unloaded.add(cluster[c]);
                    tile.load();
                } else {
                    loaded.add(tiles[i]);
                }
            }
        }


        if (unloaded.size() > 0) {

            /*
            for (int i = 0; i < loaded.size(); i++) {
                intVector[] cluster = getCluster(loaded.get(i));
                for (int c = 0; c < cluster.length; c++) if (cluster[c].containedIn(unloaded)) {
                    loaded.remove(i);
                    i--;
                    break;
                }
            }
             *

            if (level > TileSet.minLevel) {
                //draw upper level
                swissTopo.getLevelGrid(level - 1).drawTiles(pos, scale, ratio, unloaded.toArray(new intVector[0]));
            }
        }


        if (loaded.size() > 0) {
            for (intVector v : loaded) {
                this.tiles[v.x][v.y].draw(pos, scale, ratio);
            }
        }


         */

    }

    intVector[] getCluster(intVector tile) {

        if(level <= TileSet.minLevel) return new intVector[0];

        float size_new = TileSet.scale[level - 1];

        int min_x = (int) Math.floor(tile.x * size / size_new);
        int max_x = (int) Math.ceil((tile.x + 1)* size / size_new);
        int min_y = (int) Math.floor(tile.y * size / size_new);
        int max_y = (int) Math.ceil((tile.y + 1)* size / size_new);

        ArrayList<intVector> cluster = new ArrayList<>();

        for (int x = min_x; x <= max_x; x++)
            for (int y = min_y; y <= max_y; y++) {
                cluster.add(new intVector(x, y));
            }

        intVector[] vectors = cluster.toArray(new intVector[cluster.size()]);
        return vectors;
    }
}
