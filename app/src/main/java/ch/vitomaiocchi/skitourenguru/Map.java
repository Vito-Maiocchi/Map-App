package ch.vitomaiocchi.skitourenguru;

import ch.vitomaiocchi.skitourenguru.swisstopo.SwissTopo;
import ch.vitomaiocchi.skitourenguru.util.vector;

public class Map {

    private SwissTopo swissTopo;

    public Map() {
        swissTopo = new SwissTopo();
    }

    public void draw() {
        swissTopo.draw();
    }

    public void updateView(vector pos, float scale, float ratio) {
        swissTopo.updateView(pos, scale, ratio);
    }

    public void scale_start() {

    }

    public void scale_end() {
        swissTopo.updateLOD();
    }

}
