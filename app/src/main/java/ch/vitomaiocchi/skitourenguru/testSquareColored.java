package ch.vitomaiocchi.skitourenguru;

import android.annotation.SuppressLint;
import android.opengl.GLES20;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL;

public class testSquareColored {

    private FloatBuffer vertexBuffer;
    private ShortBuffer indexBuffer;

    private int[] vertexID = new int[1];
    private int[] indexID = new int[1];

    private int PositionAttribute;
    private int ColorAttribute;

    static float vertexData[] = {
            -0.5f,  0.5f, 0.0f,
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f,
            0.5f,  0.5f, 0.0f,

            1.0f, 0.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.2f
    };

    private short indexData[] = { 0, 1, 2, 0, 2, 3 };

    private final int ShaderProgram;


    @SuppressLint("NewApi")
    public testSquareColored() {

        vertexBuffer = FloatBuffer.wrap(vertexData);
        indexBuffer = ShortBuffer.wrap(indexData);

        GLES20.glGenBuffers(1, vertexID, 0);
        GLES20.glGenBuffers(1, indexID, 0);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexID[0]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, (4 * 2 * 4 * 3), vertexBuffer, GLES20.GL_STATIC_DRAW);

        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, indexID[0]);
        GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, (6 * 2), indexBuffer, GLES20.GL_STATIC_DRAW);


        String fragment =
                //*"precision mediump float;\n" +
                "in vec3 color;\n" +
                "\n" +
                "void main() {\n" +
                "\tgl_FragColor = color;\n" +
                "}";
        String vertex =
                "attribute vec4 aPosition;\n" +
                "attribute vec3 aColor;\n" +
                "\n" +
                "out vec4 color;\n" +
                "\n" +
                "void main() {\n" +
                "\tgl_Position = vPosition;\n" +
                "\tcolor = vColor;\n" +
                "}";

        /*
        try {
            fragment = new String (Files.readAllBytes(Paths.get("ch/vitomaiocchi/skitourenguru/Shaders/fragment.txt")));
            vertex = new String (Files.readAllBytes(Paths.get("ch/vitomaiocchi/skitourenguru/Shaders/vertex.txt")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        */

        int vertexShader = sgGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER, vertex);
        int fragmentShader = sgGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, fragment);

        ShaderProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(ShaderProgram, vertexShader);
        GLES20.glAttachShader(ShaderProgram, fragmentShader);
        GLES20.glLinkProgram(ShaderProgram);

        PositionAttribute = GLES20.glGetAttribLocation(ShaderProgram, "aPosition");
        ColorAttribute = GLES20.glGetAttribLocation(ShaderProgram, "aColor");
    }
    /*
    private int positionHandle;
    private int colorHandle;

    private final int vertexCount = vertexData.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    float[] color = new float[]{0.0f,0.0f,0.0f,1.0f};
    */
    public void draw() {

        System.out.println(PositionAttribute);
        System.out.println(ColorAttribute);
        System.out.println(vertexID[0]);
        System.out.println(indexID[0]);


        GLES20.glUseProgram(ShaderProgram);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexID[0]);

        GLES20.glVertexAttribPointer(PositionAttribute, 3, GLES20.GL_FLOAT, false, 0, 0);
        GLES20.glEnableVertexAttribArray(PositionAttribute);

        GLES20.glVertexAttribPointer(ColorAttribute, 3, GLES20.GL_FLOAT, false, 0, 4 * 3 * 4);
        GLES20.glEnableVertexAttribArray(ColorAttribute);

        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, indexID[0]);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, 6, GLES20.GL_UNSIGNED_SHORT, 0);

        /*
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(ShaderProgram);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexBuffer);
        // get handle to vertex shader's vPosition member
        positionHandle = GLES20.glGetAttribLocation(ShaderProgram, "vPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(positionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);


        // get handle to fragment shader's vColor member
        colorHandle = GLES20.glGetAttribLocation(ShaderProgram, "vColor");
        GLES20.glEnableVertexAttribArray(colorHandle);
        GLES20.glVertexAttribPointer(colorHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, colorBuffer);

        // Draw the triangle
        //GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indexBuffer.capacity(), GLES20.GL_UNSIGNED_SHORT, indexBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(positionHandle);
        */

    }
}
