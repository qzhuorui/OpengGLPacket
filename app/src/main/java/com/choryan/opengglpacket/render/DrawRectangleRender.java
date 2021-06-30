package com.choryan.opengglpacket.render;

import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.choryan.opengglpacket.base.BaseApplication;
import com.choryan.opengglpacket.util.AssetsUtils;
import com.choryan.opengglpacket.util.GlesUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author: ChoRyan Quan
 * @date: 2021/6/23
 */
public class DrawRectangleRender implements GLSurfaceView.Renderer {

    private final int BYTES_PER_FLOAT = 4;

    private final FloatBuffer vertexBuffer;
    private final FloatBuffer colorBuffer;
    private final ShortBuffer indicesBuffer;

    //相机矩阵
    private final float[] mViewMatrix = new float[16];
    //投影矩阵
    private final float[] mProjectMatrix = new float[16];
    //最终变换矩阵
    private final float[] mMVPMatrix = new float[16];

    private int mProgram;
    private int avPosition;
    private int afPosition;
    private int uMatrixLocation;

    private float vertexData[] = {
            -0.5f, 0.5f, 0.0f,//top left
            -0.5f, -0.5f, 0.0f, // bottom left
            0.5f, -0.5f, 0.0f, // bottom right
            0.5f, 0.5f, 0.0f // top right
    };

    private float colorData[] = {
            0.0f, 0.0f, 1.0f, 1.0f,//top left
            0.0f, 1.0f, 0.0f, 1.0f,// bottom left
            0.0f, 0.0f, 1.0f, 1.0f,// bottom right
            1.0f, 0.0f, 0.0f, 1.0f// top right
    };

    private short[] indices = {
            0, 1, 2, 0, 2, 3
    };

    public DrawRectangleRender() {
        vertexBuffer = ByteBuffer.allocateDirect(vertexData.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertexData);
        vertexBuffer.position(0);

        colorBuffer = ByteBuffer.allocateDirect(colorData.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(colorData);
        colorBuffer.position(0);

        indicesBuffer = ByteBuffer.allocateDirect(indices.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asShortBuffer()
                .put(indices);
        indicesBuffer.position(0);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        String vertexShaderStr = AssetsUtils.getVertexStrFromAssert(BaseApplication.instance, "vertex_draw_rectangle_shader");
        String fragmentShaderStr = AssetsUtils.getFragmentStrFromAssert(BaseApplication.instance, "fragment_draw_rectangle_shader");
        mProgram = GlesUtil.createProgram(vertexShaderStr, fragmentShaderStr);

        avPosition = GLES30.glGetAttribLocation(mProgram, "av_Position");
        afPosition = GLES30.glGetAttribLocation(mProgram, "af_Position");
        uMatrixLocation = GLES30.glGetUniformLocation(mProgram, "u_Matrix");
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES30.glViewport(0, 0, width, height);

//        float ratio = (float) width / height;
//        Matrix.frustumM(mProjectMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
//        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 7.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
//        Matrix.multiplyMM(mMVPMatrix, 0, mProjectMatrix, 0, mViewMatrix, 0);
//
        float aspectRatio = (float) height / (float) width;
        Matrix.orthoM(mMVPMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, -1f, 1f);

//        Matrix.setIdentityM(mMVPMatrix, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES30.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);

        GLES30.glUseProgram(mProgram);

        GLES30.glEnableVertexAttribArray(avPosition);
        GLES30.glEnableVertexAttribArray(afPosition);

        GLES30.glVertexAttribPointer(avPosition, 3, GLES30.GL_FLOAT, false, 0, vertexBuffer);
        GLES30.glVertexAttribPointer(afPosition, 4, GLES30.GL_FLOAT, false, 0, colorBuffer);

        GLES30.glUniformMatrix4fv(uMatrixLocation, 1, false, mMVPMatrix, 0);

        GLES30.glDrawElements(GLES30.GL_TRIANGLES, indices.length, GL10.GL_UNSIGNED_SHORT, indicesBuffer);

        GLES30.glDisableVertexAttribArray(avPosition);
        GLES30.glDisableVertexAttribArray(afPosition);
    }
}