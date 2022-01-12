package ch.vitomaiocchi.skitourenguru;

public class Map {

    private SwissTopo swissTopo;

    public Map() {
        swissTopo = new SwissTopo();
    }

    public void draw(vector pos, float scale, float ratio) {
        swissTopo.draw(pos, scale, ratio);
    }

}
