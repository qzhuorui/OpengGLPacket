package com.choryan.opengglpacket.gpuImage;

import android.graphics.PointF;
import android.opengl.GLES20;
import android.opengl.GLES30;

import com.choryan.opengglpacket.util.LogUtil;
import com.choryan.opengglpacket.util.OpenGlUtils;

import java.nio.FloatBuffer;
import java.util.LinkedList;

/**
 * @author: ChoRyan Quan
 * @date: 2021/7/5
 */
public class GPUImageFilter {

    private final String curClassName = this.getClass().getSimpleName();

    public static final String NO_FILTER_VERTEX_SHADER = "" +
            "attribute vec4 position;\n" +
            "attribute vec4 inputTextureCoordinate;\n" +
            " \n" +
            "varying vec2 textureCoordinate;\n" +
            " \n" +
            "void main()\n" +
            "{\n" +
            "    gl_Position = position;\n" +
            "    textureCoordinate = inputTextureCoordinate.xy;\n" +
            "}";
    public static final String NO_FILTER_FRAGMENT_SHADER = "" +
            "varying highp vec2 textureCoordinate;\n" +
            " \n" +
            "uniform sampler2D inputImageTexture;\n" +
            " \n" +
            "void main()\n" +
            "{\n" +
            "     gl_FragColor = texture2D(inputImageTexture, textureCoordinate);\n" +
            "}";


    protected final LinkedList<Runnable> runOnDraw;
    private final String vertexShader;
    private final String fragmentShader;
    private int glProgId;
    private int glAttribPosition;//vertex Coordinate
    private int glUniformTexture;//texture id
    private int glAttribTextureCoordinate;//texture Coordinate

    private int outputWidth;
    private int outputHeight;

    private boolean isInitialized;

    public GPUImageFilter() {
        this(NO_FILTER_VERTEX_SHADER, NO_FILTER_FRAGMENT_SHADER);
        LogUtil.print("GPUImageFilter curClass: " + curClassName);
    }

    public GPUImageFilter(String vertexShader, String fragmentShader) {
        runOnDraw = new LinkedList<>();
        this.vertexShader = vertexShader;
        this.fragmentShader = fragmentShader;
        LogUtil.print("GPUImageFilter2 curClass: " + curClassName);
    }

    public void ifNeedInit() {
        if (!isInitialized) {
            LogUtil.print("GPUImageFilter-ifNeedInit curClass: " + curClassName);
            init();
        }
    }

    private void init() {
        LogUtil.print("GPUImageFilter-init curClass: " + curClassName);

        onInit();
        onInitialized();
    }

    public void onInit() {
        LogUtil.print("GPUImageFilter-onInit  curClass: " + curClassName);

        glProgId = OpenGlUtils.loadProgram(vertexShader, fragmentShader);
        glAttribPosition = GLES20.glGetAttribLocation(glProgId, "position");
        glUniformTexture = GLES20.glGetUniformLocation(glProgId, "inputImageTexture");
        glAttribTextureCoordinate = GLES20.glGetAttribLocation(glProgId, "inputTextureCoordinate");
        isInitialized = true;
    }

    public void onInitialized() {
    }

    public final void destroy() {
        isInitialized = false;
        GLES20.glDeleteProgram(glProgId);
        onDestroy();
    }

    public void onDestroy() {
    }

    public void onOutputSizeChanged(final int width, final int height) {
        LogUtil.print("GPUImageFilter-onOutputSizeChanged curClass: " + curClassName);

        outputWidth = width;
        outputHeight = height;
    }

    public void onDraw(int textureId, int vertexBufferId, int frameTextureBufferId, int frameFlipTextureBufferId) {

        LogUtil.print("GPUImageFilter-onDraw curClass: " + curClassName);

        GLES20.glUseProgram(glProgId);
        runPendingOnDrawTasks();
        if (!isInitialized) {
            return;
        }

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vertexBufferId);
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, frameTextureBufferId);
        GLES20.glEnableVertexAttribArray(glAttribPosition);
        GLES20.glEnableVertexAttribArray(glAttribTextureCoordinate);

        GLES20.glVertexAttribPointer(glAttribPosition, 2, GLES20.GL_FLOAT, false, 0, 0);
        GLES20.glVertexAttribPointer(glAttribTextureCoordinate, 2, GLES20.GL_FLOAT, false, 0, 0);

        if (textureId != OpenGlUtils.NO_TEXTURE) {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
            GLES20.glUniform1i(glUniformTexture, 0);
        }
        onDrawArraysPre();
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
        GLES20.glDisableVertexAttribArray(glAttribPosition);
        GLES20.glDisableVertexAttribArray(glAttribTextureCoordinate);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
    }

    protected void runPendingOnDrawTasks() {
        synchronized (runOnDraw) {
            while (!runOnDraw.isEmpty()) {
                runOnDraw.removeFirst().run();
            }
        }
    }

    protected void onDrawArraysPre() {
    }

    public boolean isInitialized() {
        return isInitialized;
    }

    public int getOutputWidth() {
        return outputWidth;
    }

    public int getOutputHeight() {
        return outputHeight;
    }

    public int getProgram() {
        return glProgId;
    }

    protected void runOnDraw(final Runnable runnable) {
        synchronized (runOnDraw) {
            runOnDraw.addLast(runnable);
        }
    }

    protected void setInteger(final int location, final int intValue) {
        runOnDraw(new Runnable() {
            @Override
            public void run() {
                ifNeedInit();
                GLES20.glUniform1i(location, intValue);
            }
        });
    }

    protected void setFloat(final int location, final float floatValue) {
        runOnDraw(new Runnable() {
            @Override
            public void run() {
                ifNeedInit();
                GLES20.glUniform1f(location, floatValue);
            }
        });
    }

    protected void setFloatVec2(final int location, final float[] arrayValue) {
        runOnDraw(new Runnable() {
            @Override
            public void run() {
                ifNeedInit();
                GLES20.glUniform2fv(location, 1, FloatBuffer.wrap(arrayValue));
            }
        });
    }

    protected void setFloatVec3(final int location, final float[] arrayValue) {
        runOnDraw(new Runnable() {
            @Override
            public void run() {
                ifNeedInit();
                GLES20.glUniform3fv(location, 1, FloatBuffer.wrap(arrayValue));
            }
        });
    }

    protected void setFloatVec4(final int location, final float[] arrayValue) {
        runOnDraw(new Runnable() {
            @Override
            public void run() {
                ifNeedInit();
                GLES20.glUniform4fv(location, 1, FloatBuffer.wrap(arrayValue));
            }
        });
    }

    protected void setFloatArray(final int location, final float[] arrayValue) {
        runOnDraw(new Runnable() {
            @Override
            public void run() {
                ifNeedInit();
                GLES20.glUniform1fv(location, arrayValue.length, FloatBuffer.wrap(arrayValue));
            }
        });
    }

    protected void setPoint(final int location, final PointF point) {
        runOnDraw(new Runnable() {
            @Override
            public void run() {
                ifNeedInit();
                float[] vec2 = new float[2];
                vec2[0] = point.x;
                vec2[1] = point.y;
                GLES20.glUniform2fv(location, 1, vec2, 0);
            }
        });
    }

    protected void setUniformMatrix3f(final int location, final float[] matrix) {
        runOnDraw(new Runnable() {
            @Override
            public void run() {
                ifNeedInit();
                GLES20.glUniformMatrix3fv(location, 1, false, matrix, 0);
            }
        });
    }

    protected void setUniformMatrix4f(final int location, final float[] matrix) {
        runOnDraw(new Runnable() {
            @Override
            public void run() {
                ifNeedInit();
                GLES20.glUniformMatrix4fv(location, 1, false, matrix, 0);
            }
        });
    }

}
