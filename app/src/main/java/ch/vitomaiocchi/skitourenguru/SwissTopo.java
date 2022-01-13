package ch.vitomaiocchi.skitourenguru;


public class SwissTopo {

    int targetLevel;
    private LevelGrid[] levelGrids;

    public SwissTopo() {
        Image.createShader();
        levelGrids = new LevelGrid[13];

        last = System.currentTimeMillis();
    }

    long last;

    public void draw(vector pos, float scale, float ratio) {
        for (int i = 15; i < TileSet.scale.length; i++) {
            if (scale > 2 * TileSet.scale[i]) {
                targetLevel = i;
                break;
            }
        }
        getLevelGrid(targetLevel).drawScreen(pos, scale, ratio);

        //System.out.println("DELTA DRAW: "+ (System.currentTimeMillis() - last));
        last = System.currentTimeMillis();

    }

    public LevelGrid getLevelGrid(int level) {
        if(levelGrids[level - 15] == null) levelGrids[level - 15] = new LevelGrid(level, this);
        return levelGrids[level - 15];
    }

}
