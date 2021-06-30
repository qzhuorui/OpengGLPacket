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
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author: ChoRyan Quan
 * @date: 2021/6/23
 */
public class DrawCircularConeRender implements GLSurfaceView.Renderer {

    private final int BYTES_PER_FLOAT = 4;

    private final FloatBuffer vertexBuffer;
    private final FloatBuffer vertexBuffer1;
    private final FloatBuffer colorBuffer;

    //相机矩阵
    private final float[] mViewMatrix = new float[16];
    //投影矩阵
    private final float[] mProjectMatrix = new float[16];
    //最终变换矩阵
    private final float[] mMVPMatrix = new float[16];

    private int mProgram;
    private int avPosition;
    private int afColor;
    private int uMatrixLocation;

    private float circularCoords[];
    //圆锥顶点位置（圆底面）
    private float coneCoords1[];
    private float color[];

    public DrawCircularConeRender() {
        createPositions(0.5f, 60);
        //圆锥的圆形底面数据
        createCircularPositions();

        vertexBuffer = ByteBuffer.allocateDirect(circularCoords.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(circularCoords);
        vertexBuffer.position(0);

        vertexBuffer1 = ByteBuffer.allocateDirect(coneCoords1.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(coneCoords1);
        vertexBuffer1.position(0);

        colorBuffer = ByteBuffer.allocateDirect(color.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(color);
        colorBuffer.position(0);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        String vertexShaderStr = AssetsUtils.getVertexStrFromAssert(BaseApplication.instance, "vertex_draw_circular_cone_shader");
        String fragmentShaderStr = AssetsUtils.getFragmentStrFromAssert(BaseApplication.instance, "fragment_draw_circular_cone_shader");
        mProgram = GlesUtil.createProgram(vertexShaderStr, fragmentShaderStr);

        avPosition = GLES30.glGetAttribLocation(mProgram, "av_Position");
        afColor = GLES30.glGetAttribLocation(mProgram, "af_Color");
        uMatrixLocation = GLES30.glGetUniformLocation(mProgram, "u_Matrix");
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES30.glViewport(0, 0, width, height);

//        float ratio = (float) width / height;
//        Matrix.frustumM(mProjectMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
//        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 7.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
//        Matrix.multiplyMM(mMVPMatrix, 0, mProjectMatrix, 0, mViewMatrix, 0);

        //计算宽高比
        float ratio = (float) width / height;
        //设置透视投影
        Matrix.frustumM(mProjectMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
        //设置相机位置
        Matrix.setLookAtM(mViewMatrix, 0, 6, 0, -1f, 0f, 0f, 0f, 0f, 0.0f, 1.0f);
        //计算变换矩阵
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectMatrix, 0, mViewMatrix, 0);

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES30.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);

        GLES30.glUseProgram(mProgram);

        GLES30.glEnableVertexAttribArray(avPosition);
        GLES30.glEnableVertexAttribArray(afColor);

        GLES30.glVertexAttribPointer(avPosition, 3, GLES30.GL_FLOAT, false, 0, vertexBuffer);
        GLES30.glVertexAttribPointer(afColor, 4, GLES30.GL_FLOAT, false, 0, colorBuffer);

        GLES30.glUniformMatrix4fv(uMatrixLocation, 1, false, mMVPMatrix, 0);

        GLES30.glDrawArrays(GLES30.GL_TRIANGLE_FAN, 0, circularCoords.length / 3);

        GLES30.glVertexAttribPointer(avPosition, 3, GLES30.GL_FLOAT, false, 0, vertexBuffer1);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLE_FAN, 0, coneCoords1.length / 3);

        GLES30.glDisableVertexAttribArray(avPosition);
        GLES30.glDisableVertexAttribArray(afColor);
    }

    private void createPositions(float radius, int n) {
        ArrayList<Float> data = new ArrayList<>();
        data.add(0.0f);             //设置圆锥顶点坐标
        data.add(0.0f);
        data.add(-0.5f);
        float angDegSpan = 360f / n;
        for (float i = 0; i < 360 + angDegSpan; i += angDegSpan) {
            data.add((float) (radius * Math.sin(i * Math.PI / 180f)));
            data.add((float) (radius * Math.cos(i * Math.PI / 180f)));
            data.add(0.0f);
        }
        float[] f = new float[data.size()];
        for (int i = 0; i < f.length; i++) {
            f[i] = data.get(i);
        }

        circularCoords = f;

        //处理各个顶点的颜色
        color = new float[f.length * 4 / 3];
        ArrayList<Float> tempC = new ArrayList<>();
        ArrayList<Float> totalC = new ArrayList<>();
        ArrayList<Float> total0 = new ArrayList<>();
        total0.add(0.5f);
        total0.add(0.0f);
        total0.add(0.0f);
        total0.add(1.0f);
        tempC.add(1.0f);
        tempC.add(1.0f);
        tempC.add(1.0f);
        tempC.add(1.0f);
        for (int i = 0; i < f.length / 3; i++) {
            if (i == 0) {
                totalC.addAll(total0);
            } else {
                totalC.addAll(tempC);
            }

        }

        for (int i = 0; i < totalC.size(); i++) {
            color[i] = totalC.get(i);
        }
    }

    private void createCircularPositions() {
        coneCoords1 = new float[circularCoords.length];

        for (int i = 0; i < circularCoords.length; i++) {
            if (i == 2) {
                coneCoords1[i] = 0.0f;
            } else {
                coneCoords1[i] = circularCoords[i];
            }
        }
    }
}