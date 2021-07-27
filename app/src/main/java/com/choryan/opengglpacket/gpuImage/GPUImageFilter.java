package com.choryan.opengglpacket.gpuImage;

import android.graphics.PointF;
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
    private int curVaoId;
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

        int[] vao = new int[1];
        GLES30.glGenVertexArrays(1, vao, 0);
        curVaoId = vao[0];

        glProgId = OpenGlUtils.loadProgram(vertexShader, fragmentShader);
        glAttribPosition = GLES30.glGetAttribLocation(glProgId, "position");
        glUniformTexture = GLES30.glGetUniformLocation(glProgId, "inputImageTexture");
        glAttribTextureCoordinate = GLES30.glGetAttribLocation(glProgId, "inputTextureCoordinate");
        isInitialized = true;
    }

    public void onInitialized() {
    }

    public final void destroy() {
        isInitialized = false;
        GLES30.glDeleteProgram(glProgId);
        onDestroy();
    }

    public void onDestroy() {
    }

    public void onOutputSizeChanged(final int width, final int height) {
        LogUtil.print("GPUImageFilter-onOutputSizeChanged curClass: " + curClassName);

        outputWidth = width;
        outputHeight = height;
    }

    public void bindVAOData(int vertexBufferId, int frameTextureBufferId, int frameFlipTextureBufferId) {
        GLES30.glBindVertexArray(curVaoId);

        GLES30.glEnableVertexAttribArray(glAttribPosition);
        GLES30.glEnableVertexAttribArray(glAttribTextureCoordinate);

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vertexBufferId);
        GLES30.glVertexAttribPointer(glAttribPosition, 2, GLES30.GL_FLOAT, false, 0, 0);
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, frameTextureBufferId);
        GLES30.glVertexAttribPointer(glAttribTextureCoordinate, 2, GLES30.GL_FLOAT, false, 0, 0);

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0);
        GLES30.glBindVertexArray(0);
        GLES30.glDisableVertexAttribArray(glAttribPosition);
        GLES30.glDisableVertexAttribArray(glAttribTextureCoordinate);
    }

    public void onDraw(int textureId) {
        LogUtil.print("GPUImageFilter-onDraw curClass: " + curClassName);

        GLES30.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);

        GLES30.glUseProgram(glProgId);
        runPendingOnDrawTasks();
        if (!isInitialized) {
            return;
        }

        GLES30.glBindVertexArray(curVaoId);

        if (textureId != OpenGlUtils.NO_TEXTURE) {
            GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId);
            GLES30.glUniform1i(glUniformTexture, 0);
        }
        onDrawArraysPre();
        GLES30.glDrawArrays(GLES30.GL_TRIANGLE_STRIP, 0, 4);

        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);
        GLES30.glBindVertexArray(0);
        onDrawArraysEnd();
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

    protected void onDrawArraysEnd() {
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
                GLES30.glUniform1i(location, intValue);
            }
        });
    }

    protected void setFloat(final int location, final float floatValue) {
        runOnDraw(new Runnable() {
            @Override
            public void run() {
                ifNeedInit();
                GLES30.glUniform1f(location, floatValue);
            }
        });
    }

    protected void setFloatVec2(final int location, final float[] arrayValue) {
        runOnDraw(new Runnable() {
            @Override
            public void run() {
                ifNeedInit();
                GLES30.glUniform2fv(location, 1, FloatBuffer.wrap(arrayValue));
            }
        });
    }

    protected void setFloatVec3(final int location, final float[] arrayValue) {
        runOnDraw(new Runnable() {
            @Override
            public void run() {
                ifNeedInit();
                GLES30.glUniform3fv(location, 1, FloatBuffer.wrap(arrayValue));
            }
        });
    }

    protected void setFloatVec4(final int location, final float[] arrayValue) {
        runOnDraw(new Runnable() {
            @Override
            public void run() {
                ifNeedInit();
                GLES30.glUniform4fv(location, 1, FloatBuffer.wrap(arrayValue));
            }
        });
    }

    protected void setFloatArray(final int location, final float[] arrayValue) {
        runOnDraw(new Runnable() {
            @Override
            public void run() {
                ifNeedInit();
                GLES30.glUniform1fv(location, arrayValue.length, FloatBuffer.wrap(arrayValue));
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
                GLES30.glUniform2fv(location, 1, vec2, 0);
            }
        });
    }

    protected void setUniformMatrix3f(final int location, final float[] matrix) {
        runOnDraw(new Runnable() {
            @Override
            public void run() {
                ifNeedInit();
                GLES30.glUniformMatrix3fv(location, 1, false, matrix, 0);
            }
        });
    }

    protected void setUniformMatrix4f(final int location, final float[] matrix) {
        runOnDraw(new Runnable() {
            @Override
            public void run() {
                ifNeedInit();
                GLES30.glUniformMatrix4fv(location, 1, false, matrix, 0);
            }
        });
    }

}
