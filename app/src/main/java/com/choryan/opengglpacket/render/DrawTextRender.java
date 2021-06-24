package com.choryan.opengglpacket.render;

import android.graphics.Bitmap;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.choryan.opengglpacket.base.BaseApplication;
import com.choryan.opengglpacket.util.AssetsUtils;
import com.choryan.opengglpacket.util.GlesUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author: ChoRyan Quan
 * @date: 2021/6/23
 */
public class DrawTextRender implements GLSurfaceView.Renderer {

    private static final String TAG = "DrawTextRender";

    private final int BYTES_PER_FLOAT = 4;
    private final int CoordsPerVertexCount = 2;
    private final FloatBuffer vertexBuffer;
    private final FloatBuffer frameBuffer;

    private final float[] transformMatrixArray = new float[16];
    private final float[] rotateMatrixArray = new float[16];
    private final float[] orthographicMatrix = new float[16];
    private int mProgram;
    private int avPosition;
    private int afPosition;
    private int uniformRotateMatrixLocation;
    private int sTexture;

    private Bitmap mRenderBitmap;
    private int mBitmapTextureId = -1;

    private boolean needReCreateTextureId;


    private float vertexData[] = {
            -1f, -1f,
            1f, -1f,
            -1f, 1f,
            1f, 1f
    };

    private float frameBufferData[] = {
            0f, 0f,
            1f, 0f,
            0f, 1f,
            1f, 1f
    };

    protected final int VertexCount = vertexData.length / CoordsPerVertexCount;

    public DrawTextRender() {
        vertexBuffer = ByteBuffer.allocateDirect(vertexData.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertexData);
        vertexBuffer.position(0);

        frameBuffer = ByteBuffer.allocateDirect(frameBufferData.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(frameBufferData);
        frameBuffer.position(0);

        Matrix.setIdentityM(rotateMatrixArray, 0);
        Matrix.setRotateM(rotateMatrixArray, 0, 180, 1, 0, 0);
    }

    public void setRenderBitmap(Bitmap renderBitmap, GLSurfaceRenderCallback callback) {
        mRenderBitmap = renderBitmap;
        needReCreateTextureId = true;
        callback.onRequestRender();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        String vertexShaderStr = AssetsUtils.getVertexStrFromAssert(BaseApplication.instance, "vertex_draw_text_shader");
        String fragmentShaderStr = AssetsUtils.getFragmentStrFromAssert(BaseApplication.instance, "fragment_draw_text_shader");

        mProgram = GlesUtil.createProgram(vertexShaderStr, fragmentShaderStr);

        avPosition = GLES30.glGetAttribLocation(mProgram, "av_Position");
        afPosition = GLES30.glGetAttribLocation(mProgram, "af_Position");
        uniformRotateMatrixLocation = GLES30.glGetUniformLocation(mProgram, "rotateMatrix");
        sTexture = GLES30.glGetUniformLocation(mProgram, "textureId");
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES30.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        if (mRenderBitmap == null) {
            GLES30.glClearColor(1.0f, 0.0f, 0.0f, 0.5f);
            GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);
            return;
        } else {
            GLES30.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
            GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);
            if (mBitmapTextureId == -1 || needReCreateTextureId) {
                needReCreateTextureId = false;
                mBitmapTextureId = GlesUtil.loadBitmapTexture(mRenderBitmap);
            }
        }
        GLES30.glViewport(0, 0, mRenderBitmap.getWidth(), mRenderBitmap.getHeight());

        GLES30.glUseProgram(mProgram);

        GLES30.glEnable(GLES30.GL_BLEND);
        GLES30.glBlendEquation(GLES30.GL_FUNC_ADD);
        GLES30.glBlendFuncSeparate(GLES30.GL_SRC_ALPHA, GLES30.GL_ONE_MINUS_SRC_ALPHA, GLES30.GL_ONE, GLES30.GL_ONE);

        GLES30.glEnableVertexAttribArray(avPosition);
        GLES30.glEnableVertexAttribArray(afPosition);

        GLES30.glVertexAttribPointer(avPosition, CoordsPerVertexCount, GLES30.GL_FLOAT, false, 0, vertexBuffer);
        GLES30.glVertexAttribPointer(afPosition, CoordsPerVertexCount, GLES30.GL_FLOAT, false, 0, frameBuffer);

        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, mBitmapTextureId);
        GLES30.glUniform1i(sTexture, 0);

        GLES30.glUniformMatrix4fv(uniformRotateMatrixLocation, 1, false, rotateMatrixArray, 0);

        GLES30.glDrawArrays(GLES30.GL_TRIANGLE_STRIP, 0, VertexCount);

        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);
        GLES30.glDisableVertexAttribArray(avPosition);
        GLES30.glDisableVertexAttribArray(afPosition);
        GLES30.glDisable(GLES30.GL_BLEND);
    }

    public interface GLSurfaceRenderCallback {
        void onRequestRender();
    }
}