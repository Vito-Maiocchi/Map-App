package ch.vitomaiocchi.skitourenguru.swisstopo;


import ch.vitomaiocchi.skitourenguru.opengl.Image;
import ch.vitomaiocchi.skitourenguru.util.vector;

public class SwissTopo {

    int targetLevel;
    //private LevelGrid[] levelGrids;

    vector pos;
    float scale;
    float ratio;

    private Tile[] tiles;

    public SwissTopo() {
        Image.createShader();
        BitmapFetcher.initialize(this);
        tiles = new Tile[]{new Tile(1, 0, 0), new Tile(1, 0, 1), new Tile(1, 1, 0), new Tile(1, 1, 1)};
        //levelGrids = new LevelGrid[TileSet.scale.length - TileSet.minLevel];
    }


    public void draw() {
        BitmapFetcher.update();

        for (int i = 0; i < tiles.length; i++) {
            System.out.println(tiles[i].loadState);
            tiles[i].draw(pos, scale, ratio);
        }

        for (int i = TileSet.minLevel; i < TileSet.scale.length; i++) {
            if (scale > 2 * TileSet.scale[i]) {
                targetLevel = i;
                break;
            }
        }

    }

    public void updateView(vector pos, float scale, float ratio) {
        this.pos = pos;
        this.scale = scale;
        this.ratio = ratio;
    }

    public void updateLOD() {

    }

    /*
    public LevelGrid getLevelGrid(int level) {
        if(levelGrids[level - TileSet.minLevel] == null) levelGrids[level - TileSet.minLevel] = new LevelGrid(level, this);
        return levelGrids[level - TileSet.minLevel];
    }

     */
}
