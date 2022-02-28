package ch.vitomaiocchi.skitourenguru.swisstopo;

import ch.vitomaiocchi.skitourenguru.util.vector;

public class TileSet {

    // CH COORDS PIXELKARTE FARBE
    //https://wmts.geo.admin.ch/1.0.0/ch.swisstopo.pixelkarte-farbe/default/current/21781/24/700/400.jpeg

    /*
    public static final String url = "https://wmts.geo.admin.ch/1.0.0/ch.swisstopo.pixelkarte-farbe/default/current/21781/";

    public static final int minLevel = 15;

    public static final float[] scale = {
            14285714.2857f,
            13392857.1429f,
            12500000.0000f,
            11607142.8571f,
            10714285.7143f,
            9821428.57143f,
            8928571.42857f,
            8035714.28571f,
            7142857.14286f,
            6250000.00000f,
            5357142.85714f,
            4464285.71429f,
            3571428.57143f,
            2678571.42857f,
            2321428.57143f,
            1785714.28571f, //15
            892857.142857f,
            357142.857143f,
            178571.428571f,
            71428.5714286f,
            35714.2857143f,
            17857.1428571f,
            8928.57142857f,
            7142.85714286f,
            5357.14285714f,
            3571.42857143f,
            1785.71428571f,
            892.857142857f};

    public static final vector TopLeftCorner = new vector(420000.0f, 350000.0f);

    public static final int[] MatrixWidth =     {1,1,1,1,1,1,1,1,1,2,2,2,2,3,3,4,8,19,38,94,188,375,750,938,1250,1875,3750,7500}; // min:15

    public static final int[] MatrixHeight =    {1,1,1,1,1,1,1,1,1,1,1,1,2,2,2,3,5,13,25,63,125,250,500,625,834,1250,2500,5000};

    public static final int TileDimensions = 256;
     */

    public static final String url = "https://wmts.geo.admin.ch/1.0.0/ch.swisstopo.pixelkarte-farbe/default/current/3857/";

    public static final int minLevel = 0;

    public static final float[] scale = {
            559082264.029f,
            279541132.014f,
            139770566.007f,
            69885283.0036f,
            34942641.5018f,
            17471320.7509f,
            8735660.37545f,
            4367830.18772f,
            2183915.09386f,
            1091957.54693f,
            545978.773466f,
            272989.386733f,
            136494.693366f,
            68247.3466832f,
            34123.6733416f,
            17061.8366708f,
            8530.91833540f,
            4265.45916770f,
            2132.72958385f,
            1066.36479192f};

    public static final vector TopLeftCorner = new vector(-20037508.3428f, 20037508.3428f);

    public static final int[] MatrixWidth =     {1,2,4,8,16,32,64,128,256,512,1024,2048,4096,8192,16384,32768,65536,131072,262144,524288};

    public static final int[] MatrixHeight =    {1,2,4,8,16,32,64,128,256,512,1024,2048,4096,8192,16384,32768,65536,131072,262144,524288};

    public static final int TileDimensions = 256;

}
