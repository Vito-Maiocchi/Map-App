package ch.vitomaiocchi.skitourenguru.util;

public class vector {

    public float x;
    public float y;

    public vector(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public vector add(vector vector) {
        x = x + vector.x;
        y = y + vector.y;
        return this;
    }

    public static vector subtract (vector vector1, vector vector2) {
        return new vector(vector1.x - vector2.x, vector1.y - vector2.y);
    }

    public vector subtract(vector vector) {
        x = x - vector.x;
        y = y - vector.y;
        return this;
    }

    public vector scaleToDimensions(vector dimensions, float scale) {
        float ratio = dimensions.y / dimensions.x;

        x = x / dimensions.x * scale;
        y = y / dimensions.y * scale * ratio;

        return this;
    }

    public vector transformToDimensions(vector pos, vector dimensions, float scale) {
        float ratio = dimensions.y / dimensions.x;

        x = x / dimensions.x * scale;
        y = y / dimensions.y * scale * ratio;

        x = x + pos.x - scale / 2;
        y = y + pos.y - (scale * ratio) / 2;

        return this;
    }

    @Override
    public String toString() {
        return "X: "+x+", Y: "+y;
    }
}
