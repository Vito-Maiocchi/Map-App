package ch.vitomaiocchi.skitourenguru;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class testSquare {


    private final FloatBuffer mTriangleVertices;
    private int mPositionHandle;
    private int mTextureCoordinateHandle;
    private final int mBytesPerFloat = 4;
    private final int mStrideBytes = 5 * mBytesPerFloat;
    private final int mPositionOffset = 0;
    private final int mPositionDataSize = 3;
    private final int mTextureCoordinateOffset = 3;
    private final int mTextureCoordinateDataSize = 2;
    private int mTextureUniformHandle;
    private int mTextureDataHandle;

    private int matrixHandle;
    private int vectorHandle;

    private int programHandle;

    public testSquare(Context context) {

        final float[] triangle1VerticesData = {

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

        mTriangleVertices = ByteBuffer.allocateDirect(triangle1VerticesData.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();

        mTriangleVertices.put(triangle1VerticesData).position(0);


        final String vertexShader =
            "attribute vec4 a_Position;\n" +
                    "attribute vec2 a_TexCoordinate;\n" +
                    "\n" +
                    "uniform mat4 u_Matrix;\n" +
                    "uniform vec4 u_Vector;\n" +
                    "\n" +
                    "varying vec2 v_TexCoordinate;\n" +
                    "\n" +
                    "void main() {\n" +
                    "    v_TexCoordinate = a_TexCoordinate;\n" +
                    "    gl_Position = u_Matrix * ( a_Position + u_Vector );\n" +
                    "}";

        final String fragmentShader =
                        "precision mediump float;\n" +
                        "\n" +
                        "uniform sampler2D u_Texture;\n" +
                        "\n" +
                        "varying vec2 v_TexCoordinate;\n" +
                        "\n" +
                        "void main() {\n" +
                        "    gl_FragColor = texture2D(u_Texture, v_TexCoordinate);\n" +
                        "}";

        int vertexShaderHandle = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);

        if (vertexShaderHandle != 0)
        {
            GLES20.glShaderSource(vertexShaderHandle, vertexShader);
            GLES20.glCompileShader(vertexShaderHandle);
            final int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(vertexShaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

            if (compileStatus[0] == 0)
            {
                GLES20.glDeleteShader(vertexShaderHandle);
                vertexShaderHandle = 0;
            }
        }

        if (vertexShaderHandle == 0)
        {
            throw new RuntimeException("Error creating vertex shader.");
        }

        int fragmentShaderHandle = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);

        if (fragmentShaderHandle != 0)
        {
            GLES20.glShaderSource(fragmentShaderHandle, fragmentShader);

            GLES20.glCompileShader(fragmentShaderHandle);

            final int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(fragmentShaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

            if (compileStatus[0] == 0)
            {
                GLES20.glDeleteShader(fragmentShaderHandle);
                fragmentShaderHandle = 0;
            }
        }

        if (fragmentShaderHandle == 0)
        {
            throw new RuntimeException("Error creating fragment shader.");
        }

        programHandle = GLES20.glCreateProgram();

        if (programHandle != 0)
        {
            GLES20.glAttachShader(programHandle, vertexShaderHandle);

            GLES20.glAttachShader(programHandle, fragmentShaderHandle);

            GLES20.glBindAttribLocation(programHandle, 0, "a_Position");
            GLES20.glBindAttribLocation(programHandle, 1, "a_TexCoordinate");

            GLES20.glLinkProgram(programHandle);

            final int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS, linkStatus, 0);

            if (linkStatus[0] == 0)
            {
                GLES20.glDeleteProgram(programHandle);
                programHandle = 0;
            }
        }

        if (programHandle == 0)
        {
            throw new RuntimeException("Error creating program.");
        }

        mPositionHandle = GLES20.glGetAttribLocation(programHandle, "a_Position");
        mTextureCoordinateHandle = GLES20.glGetAttribLocation(programHandle, "a_TexCoordinate");

        mTextureDataHandle = loadTexture(context, R.drawable.pop_cat);

        GLES20.glUseProgram(programHandle);
    }

    public void draw(float[] matrix, float[] vector) {

        matrixHandle = GLES20.glGetUniformLocation(programHandle, "u_Matrix");
        GLES20.glUniformMatrix4fv(matrixHandle, 1, false, matrix, 0);

        vectorHandle = GLES20.glGetUniformLocation(programHandle, "u_Vector");
        GLES20.glUniform4f(vectorHandle, vector[0], vector[1], 0, 0);

        drawTriangle(mTriangleVertices);

    }

    private void drawTriangle(final FloatBuffer aTriangleBuffer)
    {

        mTextureUniformHandle = GLES20.glGetUniformLocation(programHandle, "u_Texture");

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureDataHandle);

        GLES20.glUniform1i(mTextureUniformHandle, 0);

        aTriangleBuffer.position(mPositionOffset);
        GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT, false,
                mStrideBytes, aTriangleBuffer);

        GLES20.glEnableVertexAttribArray(mPositionHandle);

        aTriangleBuffer.position(mTextureCoordinateOffset);
        GLES20.glVertexAttribPointer(mTextureCoordinateHandle, mTextureCoordinateDataSize, GLES20.GL_FLOAT, false,
                mStrideBytes, aTriangleBuffer);

        GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 6);
    }

    public static int loadTexture(final Context context, final int resourceId)
    {
        final int[] textureHandle = new int[1];

        GLES20.glGenTextures(1, textureHandle, 0);

        if (textureHandle[0] != 0)
        {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;   // No pre-scaling

            // Read in the resource
            final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);

            // Bind to the texture in OpenGL
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);

            // Set filtering
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

            // Load the bitmap into the bound texture.
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

            // Recycle the bitmap, since its data has been loaded into OpenGL.
            bitmap.recycle();
        }

        if (textureHandle[0] == 0)
        {
            throw new RuntimeException("Error loading texture.");
        }

        return textureHandle[0];
    }
}
