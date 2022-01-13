package ch.vitomaiocchi.skitourenguru;

import java.util.ArrayList;

public class intVector {

    public int x;
    public int y;

    public intVector(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean identical(intVector vector) {
        if (x == vector.x && y == vector.y) return true;
        return false;
    }

    public boolean containedIn(ArrayList<intVector> arrayList) {
        for (intVector vector : arrayList) {
            if(identical(vector)) return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "X: "+x+", Y: "+y;
    }

}
