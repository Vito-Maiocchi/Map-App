package ch.vitomaiocchi.skitourenguru.opengl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Image {

    private static int vertexShaderHandle;
    private static int fragmentShaderHandle;
    private static FloatBuffer verticesBuffer;

    public static void createShader() {
        final float[] verticesData = {

                -0.5f, -0.5f, 0.0f,
                0.0f, 1.0f,

                0.5f, -0.5f, 0.0f,
                1.0f, 1.0f,

                -0.5f, 0.5f, 0.0f,
                0.0f, 0.0f,


                0.5f, -0.5f, 0.0f,
                1.0f, 1.0f,

                0.5f, 0.5f, 0.0f,
                1.0f, 0.0f,

                -0.5f, 0.5f, 0.0f,
                0.0f, 0.0f
        };

        verticesBuffer = ByteBuffer.allocateDirect(verticesData.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        verticesBuffer.put(verticesData).position(0);

        final String vertexShader =
                        "attribute vec4 a_Position;" +
                        "attribute vec2 a_TexCoordinate;" +

                        "uniform mat4 u_Matrix;" +
                        "uniform vec4 u_Vector;" +

                        "varying vec2 v_TexCoordinate;" +

                        "void main() {" +
                        "    v_TexCoordinate = a_TexCoordinate;" +
                        "    gl_Position = u_Matrix * a_Position + u_Vector;" +
                        "}";

        final String fragmentShader =
                        "precision mediump float;" +

                        "uniform sampler2D u_Texture;" +

                        "varying vec2 v_TexCoordinate;" +

                        "void main() {" +
                        "    gl_FragColor = texture2D(u_Texture, v_TexCoordinate);" +
                        "}";

        vertexShaderHandle = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        GLES20.glShaderSource(vertexShaderHandle, vertexShader);
        GLES20.glCompileShader(vertexShaderHandle);

        fragmentShaderHandle = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        GLES20.glShaderSource(fragmentShaderHandle, fragmentShader);
        GLES20.glCompileShader(fragmentShaderHandle);
    }

    private int programHandle;
    private int positionHandle;
    private int textureCoordinateHandle;
    private int textureDataHandle;
    private int textureUniformHandle;

    public Image(Bitmap bitmap) {

        programHandle = GLES20.glCreateProgram();

        GLES20.glAttachShader(programHandle, vertexShaderHandle);
        GLES20.glAttachShader(programHandle, fragmentShaderHandle);

        GLES20.glBindAttribLocation(programHandle, 0, "a_Position");
        GLES20.glBindAttribLocation(programHandle, 1, "a_TexCoordinate");

        GLES20.glLinkProgram(programHandle);

        positionHandle = GLES20.glGetAttribLocation(programHandle, "a_Position");
        textureCoordinateHandle = GLES20.glGetAttribLocation(programHandle, "a_TexCoordinate");

        final int[] textureArray = new int[1];
        GLES20.glGenTextures(1, textureArray, 0);
        textureDataHandle = textureArray[0];

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureDataHandle);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();

        GLES20.glUseProgram(programHandle);

        verticesBuffer.position(0);
        GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false, 20, verticesBuffer);
        GLES20.glEnableVertexAttribArray(positionHandle);

        verticesBuffer.position(3);
        GLES20.glVertexAttribPointer(textureCoordinateHandle, 2, GLES20.GL_FLOAT, false, 20, verticesBuffer);
        GLES20.glEnableVertexAttribArray(textureCoordinateHandle);

    }

    private int matrixHandle;
    private int vectorHandle;

    public void draw(float[] matrix, float[] vector) {

        matrixHandle = GLES20.glGetUniformLocation(programHandle, "u_Matrix");
        GLES20.glUniformMatrix4fv(matrixHandle, 1, false, matrix, 0);

        vectorHandle = GLES20.glGetUniformLocation(programHandle, "u_Vector");
        GLES20.glUniform4f(vectorHandle, vector[0], vector[1], 0, 0);

        textureUniformHandle = GLES20.glGetUniformLocation(programHandle, "u_Texture");
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureDataHandle);
        GLES20.glUniform1i(textureUniformHandle, 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 6);

    }
}
