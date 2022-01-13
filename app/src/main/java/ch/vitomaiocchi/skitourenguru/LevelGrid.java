package ch.vitomaiocchi.skitourenguru;

import java.util.ArrayList;

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
        ArrayList<intVector> unloaded = new ArrayList<>(); //clusters that contain unloaded
        ArrayList<intVector> loaded = new ArrayList<>();   //loaded Tiles

        for (int i = 0; i < tiles.length; i++) {
            if( 0 <= tiles[i].x && tiles[i].x < this.tiles.length &&  0 <= tiles[i].y && tiles[i].y < this.tiles[0].length) {
                Tile tile = this.tiles[tiles[i].x][tiles[i].y];
                if (tile == null) {
                    intVector cluster = getCluster(tiles[i]);
                    if (!cluster.containedIn(unloaded)) unloaded.add(cluster);
                    System.out.println("NEW TILE");
                    this.tiles[tiles[i].x][tiles[i].y] = new Tile(level, tiles[i].x, tiles[i].y);
                } else if (!tile.isLoaded()) {
                    intVector cluster = getCluster(tiles[i]);
                    if (!cluster.containedIn(unloaded)) unloaded.add(cluster);
                    tile.load();
                } else {
                    loaded.add(tiles[i]);
                }
            }
        }

        if (unloaded.size() > 0) {

            for (int i = 0; i < loaded.size(); i++) {
                intVector cluster = getCluster(loaded.get(i));
                if (cluster.containedIn(unloaded)) {
                    loaded.remove(i);
                    i--;
                }
            }

            if (level < 15) {
                intVector[] vectors = new intVector[unloaded.size()];
                for (int i = 0; i < vectors.length; i++) vectors[i] = unloaded.get(i);

                swissTopo.getLevelGrid(level + 1).drawTiles(pos, scale, ratio, vectors);
            }
        }

        for (intVector v : loaded) {
            this.tiles[v.x][v.y].draw(pos, scale, ratio);
        }
    }

    intVector getCluster(intVector tile) {
        int x = (int) Math.floor( (float)tile.x / 2f );
        int y = (int) Math.floor( (float)tile.y / 2f );
        return new intVector(x,y);
    }
}
