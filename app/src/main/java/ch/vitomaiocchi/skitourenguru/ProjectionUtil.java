package ch.vitomaiocchi.skitourenguru;

public class ProjectionUtil {

    public static vector wmToCh(vector vector) {
        return elToCh(wmToEl(vector));
    }

    //lambda ->

    //CH

    static float ch_a = 6377397.155f;
    static float eSQ = 0.006674372230614f;
    static float E = 0.08169683121f;
    static float phi0 = 0.8194740686761218f;
    static float lambda0 = 0.12984522414316146f;

    static float ch_R = 6378815.90365f;
    static float alpha = 1.00072913843038f;
    static float b0 = 0.8186943585816748f;
    static float K = 0.0030667323772751f;

    private static vector chToEl(vector vector) {
        return null;
    }

    public static vector elToCh(vector vector) {
        // x = phi & y = lambda
        // Math.log = logarithmus naturalis , Math.exp(x) e^x
        double S = (alpha * Math.log(Math.tan(Math.PI/4 + vector.x/2)) - alpha*E/2*Math.log((1+E*Math.sin(vector.x)) / (1-E*Math.sin(vector.x))) + K);
        double b = (2 * ( Math.atan(Math.exp(S)) - Math.PI/4 ));
        double l = alpha * (vector.y - lambda0);
        double bR = Math.asin(Math.cos(b0)*Math.sin(b) - Math.sin(b0)*Math.cos(b)*Math.cos(l));
        double lR = Math.atan(Math.sin(l)/( Math.sin(b0)*Math.tan(b) + Math.cos(b0)*Math.cos(l) ));
        float X = (float) (ch_R /2 * Math.log( (1+Math.sin(bR))/(1-Math.sin(bR)) ));
        float Y = (float) (ch_R * lR);

        //System.out.println("S: "+S+"\nb: "+b+"\nl: "+l+"\nbR: "+bR+"\nlR: "+lR);

        /*
        Y += 600000;
        X += 200000;
         */

        Y += 2600000;
        X += 1200000;


        return new vector(X,Y);
    }

    //WM

    static float R = 6371007.0f;
    static float a = 6378137.0f;

    private static vector wmToEl(vector vector) {  // GETESTET
        // x = phi & y = lambda
        float D = -vector.y / a;
        float Phi = (float) (Math.PI/2 - 2*Math.atan(Math.exp(D)));
        float Lambda = vector.x/a;
        return new vector(Phi, Lambda);
    }

    private static vector elToWm(vector vector) {
        // x = phi & y = lambda
        float X = a * vector.y;
        float Y = (float) (a * Math.log(Math.tan( Math.PI/4 + vector.x/2 )));
        return new vector(X, Y);
    }



}
