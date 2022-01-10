package ch.vitomaiocchi.skitourenguru;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class testSquare {

    private FloatBuffer vertexBuffer;
    private ShortBuffer indexBuffer;

    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;

    static float squareCoords[] = {
    //      Cords                   Colors
            -0.5f,  0.5f, 0.0f,    // 1.0f, 0.0f, 0.0f,   // top left
            -0.5f, -0.5f, 0.0f,    // 0.0f, 1.0f, 0.0f,   // bottom left
            0.5f, -0.5f, 0.0f,     // 0.0f, 0.0f, 1.0f,   // bottom right
            0.5f,  0.5f, 0.0f,     // 1.0f, 1.0f, 0.2f    // top right
    };

    private short drawOrder[] = { 0, 1, 2, 0, 2, 3 }; // order to draw vertices

    private final int mProgram;


    public testSquare() {
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 4 bytes per float)
                squareCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(squareCoords);
        vertexBuffer.position(0);

        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 2 bytes per short)
                drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        indexBuffer = dlb.asShortBuffer();
        indexBuffer.put(drawOrder);
        indexBuffer.position(0);

        String fragment =
                "precision mediump float;" +
                "uniform vec4 vColor;" +
                "void main() {" +
                "  gl_FragColor = vColor;" +
                "}";
        String vertex =
                "attribute vec4 vPosition;" +
                "void main() {" +
                "  gl_Position = vPosition;" +
                "}";
        /*
        try {
            fragment = new String (Files.readAllBytes(Paths.get("ch/vitomaiocchi/skitourenguru/Shaders/fragment.txt")));
            vertex = new String (Files.readAllBytes(Paths.get("ch/vitomaiocchi/skitourenguru/Shaders/vertex.txt")));
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        int vertexShader = sgGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER, vertex);
        int fragmentShader = sgGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, fragment);

        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragmentShader);
        GLES20.glLinkProgram(mProgram);
    }

    private int positionHandle;
    private int colorHandle;

    private final int vertexCount = squareCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    float[] color = new float[]{0.0f,0.0f,0.0f,1.0f};

    public void draw() {
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(positionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);
        // get handle to fragment shader's vColor member
        colorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

        // Set color for drawing the triangle
        GLES20.glUniform4fv(colorHandle, 1, color, 0);

        // Draw the triangle
        //GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indexBuffer.capacity(), GLES20.GL_UNSIGNED_SHORT, indexBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(positionHandle);
    }
}
