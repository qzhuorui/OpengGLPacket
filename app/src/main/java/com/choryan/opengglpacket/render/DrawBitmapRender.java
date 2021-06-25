package com.choryan.opengglpacket.render;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.choryan.opengglpacket.R;
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
public class DrawBitmapRender implements GLSurfaceView.Renderer {

    private final int BYTES_PER_FLOAT = 4;
    private final int CoordsPerVertexCount = 2;
    private final FloatBuffer vertexBuffer;
    private final FloatBuffer frameBuffer;
    private final FloatBuffer textureSizeBuffer;

    private int mProgram;
    private int avPosition;
    private int afPosition;
    private int afTextureSize;
    private int sTexture;

    private Bitmap mRenderBitmap;
    private int mBitmapTextureId;

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

    private float textureSizeBufferData[] = {
            0f, 0f
    };

    protected final int VertexCount = vertexData.length / CoordsPerVertexCount;

    public DrawBitmapRender(Context context) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        mRenderBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.wnmt110_4, options);
        textureSizeBufferData[0] = mRenderBitmap.getWidth();
        textureSizeBufferData[1] = mRenderBitmap.getHeight();

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

        textureSizeBuffer = ByteBuffer.allocateDirect(textureSizeBufferData.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(textureSizeBufferData);
        textureSizeBuffer.position(0);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        String vertexShaderStr = AssetsUtils.getVertexStrFromAssert(BaseApplication.instance, "vertex_draw_bitmap_shader");
        String fragmentShaderStr = AssetsUtils.getFragmentStrFromAssert(BaseApplication.instance, "fragment_draw_bitmap_shader");

        mProgram = GlesUtil.createProgram(vertexShaderStr, fragmentShaderStr);

        mBitmapTextureId = GlesUtil.loadBitmapTexture(mRenderBitmap);

        avPosition = GLES30.glGetAttribLocation(mProgram, "av_Position");
        afPosition = GLES30.glGetAttribLocation(mProgram, "af_Position");
        afTextureSize = GLES30.glGetAttribLocation(mProgram, "af_textSize");
        sTexture = GLES30.glGetUniformLocation(mProgram, "textureId");
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES30.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES30.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);
        GLES30.glViewport(0, 0, mRenderBitmap.getWidth(), mRenderBitmap.getHeight());

        GLES30.glUseProgram(mProgram);

        GLES30.glEnableVertexAttribArray(avPosition);
        GLES30.glEnableVertexAttribArray(afPosition);
        GLES30.glEnableVertexAttribArray(afTextureSize);

        GLES30.glVertexAttribPointer(avPosition, CoordsPerVertexCount, GLES30.GL_FLOAT, false, 0, vertexBuffer);
        GLES30.glVertexAttribPointer(afPosition, CoordsPerVertexCount, GLES30.GL_FLOAT, false, 0, frameBuffer);
        GLES30.glVertexAttribPointer(afTextureSize, CoordsPerVertexCount, GLES30.GL_FLOAT, false, 0, textureSizeBuffer);

        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, mBitmapTextureId);
        GLES30.glUniform1i(sTexture, 0);

        GLES30.glDrawArrays(GLES30.GL_TRIANGLE_STRIP, 0, VertexCount);

        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);
        GLES30.glDisableVertexAttribArray(avPosition);
        GLES30.glDisableVertexAttribArray(afPosition);
        GLES30.glDisableVertexAttribArray(afTextureSize);
    }

}