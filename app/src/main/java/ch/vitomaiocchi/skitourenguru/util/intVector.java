package ch.vitomaiocchi.skitourenguru.util;

import java.util.ArrayList;

public class intVector {

    public int x;
    public int y;

    public intVector(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean identical(intVector vector) {
        return x == vector.x && y == vector.y;
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
