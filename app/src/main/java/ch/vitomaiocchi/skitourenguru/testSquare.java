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

    /** This will be used to pass in model position information. */
    private int mPositionHandle;

    /** This will be used to pass in model color information. */
    private int mTextureCoordinateHandle;

    /** How many bytes per float. */
    private final int mBytesPerFloat = 4;

    /** How many elements per vertex. */
    private final int mStrideBytes = 5 * mBytesPerFloat;

    /** Offset of the position data. */
    private final int mPositionOffset = 0;

    /** Size of the position data in elements. */
    private final int mPositionDataSize = 3;

    /** Offset of the color data. */
    private final int mTextureCoordinateOffset = 3;

    /** Size of the color data in elements. */
    private final int mTextureCoordinateDataSize = 2;

    /** This will be used to pass in the texture. */
    private int mTextureUniformHandle;
    private int mTextureDataHandle;

    private int matrixHandle;

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

        // This triangle is yellow, cyan, and magenta.

        // Initialize the buffers.
        mTriangleVertices = ByteBuffer.allocateDirect(triangle1VerticesData.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();

        mTriangleVertices.put(triangle1VerticesData).position(0);


        final String vertexShader =
            "attribute vec4 a_Position;\n" +
                    "attribute vec2 a_TexCoordinate;\n" +
                    "\n" +
                    "uniform mat4 u_Matrix;\n" +
                    "\n" +
                    "varying vec2 v_TexCoordinate;\n" +
                    "\n" +
                    "void main() {\n" +
                    "    v_TexCoordinate = a_TexCoordinate;\n" +
                    "    gl_Position = u_Matrix * a_Position;\n" +
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

        // Load in the vertex shader.
        int vertexShaderHandle = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);

        if (vertexShaderHandle != 0)
        {
            // Pass in the shader source.
            GLES20.glShaderSource(vertexShaderHandle, vertexShader);

            // Compile the shader.
            GLES20.glCompileShader(vertexShaderHandle);

            // Get the compilation status.
            final int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(vertexShaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

            // If the compilation failed, delete the shader.
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

        // Load in the fragment shader shader.
        int fragmentShaderHandle = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);

        if (fragmentShaderHandle != 0)
        {
            // Pass in the shader source.
            GLES20.glShaderSource(fragmentShaderHandle, fragmentShader);

            // Compile the shader.
            GLES20.glCompileShader(fragmentShaderHandle);

            // Get the compilation status.
            final int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(fragmentShaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

            // If the compilation failed, delete the shader.
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

        // Create a program object and store the handle to it.
        programHandle = GLES20.glCreateProgram();

        if (programHandle != 0)
        {
            // Bind the vertex shader to the program.
            GLES20.glAttachShader(programHandle, vertexShaderHandle);

            // Bind the fragment shader to the program.
            GLES20.glAttachShader(programHandle, fragmentShaderHandle);

            // Bind attributes
            GLES20.glBindAttribLocation(programHandle, 0, "a_Position");
            GLES20.glBindAttribLocation(programHandle, 1, "a_TexCoordinate");

            // Link the two shaders together into a program.
            GLES20.glLinkProgram(programHandle);

            // Get the link status.
            final int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS, linkStatus, 0);

            // If the link failed, delete the program.
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

        // Set program handles. These will later be used to pass in values to the program.
        mPositionHandle = GLES20.glGetAttribLocation(programHandle, "a_Position");
        mTextureCoordinateHandle = GLES20.glGetAttribLocation(programHandle, "a_TexCoordinate");

        mTextureDataHandle = loadTexture(context, R.drawable.pop_cat);

        // Tell OpenGL to use this program when rendering.
        GLES20.glUseProgram(programHandle);
    }

    public void draw(float[] matrix) {

        matrixHandle = GLES20.glGetUniformLocation(programHandle, "u_Matrix");

        GLES20.glUniformMatrix4fv(matrixHandle, 1, false, matrix, 0);

        drawTriangle(mTriangleVertices);

    }

    private void drawTriangle(final FloatBuffer aTriangleBuffer)
    {

        mTextureUniformHandle = GLES20.glGetUniformLocation(programHandle, "u_Texture");

        // Set the active texture unit to texture unit 0.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);

        // Bind the texture to this unit.
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureDataHandle);

        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
        GLES20.glUniform1i(mTextureUniformHandle, 0);

        // Pass in the position information
        aTriangleBuffer.position(mPositionOffset);
        GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT, false,
                mStrideBytes, aTriangleBuffer);

        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Pass in the color information
        aTriangleBuffer.position(mTextureCoordinateOffset);
        GLES20.glVertexAttribPointer(mTextureCoordinateHandle, mTextureCoordinateDataSize, GLES20.GL_FLOAT, false,
                mStrideBytes, aTriangleBuffer);

        GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);

        //GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
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
