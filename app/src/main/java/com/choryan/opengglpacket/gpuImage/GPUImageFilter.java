package com.choryan.opengglpacket.gpuImage;

import android.graphics.PointF;
import android.opengl.GLES20;
import android.opengl.GLES30;

import com.choryan.opengglpacket.util.GlesUtil;

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

    private int[] frameBuffers;
    private int[] frameBufferTextures;
    private int selfFboId;
    private int selfFboTextureId;

    private int outputWidth;
    private int outputHeight;

    private boolean isInitialized;

    public GPUImageFilter() {
        this(NO_FILTER_VERTEX_SHADER, NO_FILTER_FRAGMENT_SHADER);
    }

    public GPUImageFilter(String vertexShader, String fragmentShader) {
        runOnDraw = new LinkedList<>();
        this.vertexShader = vertexShader;
        this.fragmentShader = fragmentShader;
    }

    public void ifNeedInit() {
        if (!isInitialized) {
            init();
        }
    }

    private void init() {
        onInit();
        onInitialized();
    }

    public void onInit() {
        int[] vao = new int[1];
        GLES30.glGenVertexArrays(1, vao, 0);
        curVaoId = vao[0];

        glProgId = GlesUtil.createProgram(vertexShader, fragmentShader);
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
        outputWidth = width;
        outputHeight = height;
        glGenFrameBuffer();
    }

    /**
     * @description 2.0没有VAO
     * @author ChoRyan Quan
     * @time 2021/7/27 2:44 下午
     */
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
        GLES30.glViewport(0, 0, outputWidth, outputHeight);
        GLES30.glUseProgram(glProgId);
        runPendingOnDrawTasks();
        if (!isInitialized) {
            return;
        }

        GLES30.glBindVertexArray(curVaoId);

        if (textureId != -1) {
            GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId);
            GLES30.glUniform1i(glUniformTexture, 0);
        } else {
            throw new RuntimeException("GPUImageFilter onDraw textId is -1");
        }
        onDrawArraysPre();
        GLES30.glDrawArrays(GLES30.GL_TRIANGLE_STRIP, 0, 4);

        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);
        GLES30.glBindVertexArray(0);
        onDrawArraysEnd();
    }

    public int getCurVaoId() {
        return curVaoId;
    }

    public int getSelfFboId() {
        return selfFboId;
    }

    public int getSelfFboTextureId() {
        return selfFboTextureId;
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

    public void setOutputWidth(int outputWidth) {
        this.outputWidth = outputWidth;
    }

    public void setOutputHeight(int outputHeight) {
        this.outputHeight = outputHeight;
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

    public void glGenFrameBuffer() {
        destroyFrameBuffers();
        frameBuffers = new int[1];
        frameBufferTextures = new int[1];

        GLES30.glGenFramebuffers(1, frameBuffers, 0);
        GLES20.glGenTextures(1, frameBufferTextures, 0);

        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, frameBufferTextures[0]);
        GLES30.glTexImage2D(GLES30.GL_TEXTURE_2D, 0, GLES30.GL_RGBA, getOutputWidth(), getOutputHeight(), 0, GLES30.GL_RGBA, GLES30.GL_UNSIGNED_BYTE, null);

        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, frameBuffers[0]);
        GLES30.glFramebufferTexture2D(GLES30.GL_FRAMEBUFFER, GLES30.GL_COLOR_ATTACHMENT0, GLES30.GL_TEXTURE_2D, frameBufferTextures[0], 0);

        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, 0);

        selfFboId = frameBuffers[0];
        selfFboTextureId = frameBufferTextures[0];
    }

    public void destroyFrameBuffers() {
        if (frameBufferTextures != null) {
            GLES30.glDeleteTextures(frameBufferTextures.length, frameBufferTextures, 0);
            frameBufferTextures = null;
        }
        if (frameBuffers != null) {
            GLES30.glDeleteFramebuffers(frameBuffers.length, frameBuffers, 0);
            frameBuffers = null;
        }
    }

}
