package com.choryan.opengglpacket.render;

import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;

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
public class PointLineTriangleRender implements GLSurfaceView.Renderer {

    //一个Float占用4Byte
    private static final int BYTES_PER_FLOAT = 4;
    //三个顶点
    private static final int POSITION_COMPONENT_COUNT = 3;
    //顶点位置缓存
    private final FloatBuffer vertexBuffer;
    //顶点颜色缓存
    private final FloatBuffer colorBuffer;
    //渲染程序
    private int mProgram;

    //三个顶点的位置参数
    private float triangleCoords[] = {
            0.5f, 0.5f, 0.0f, // top
            -0.5f, -0.5f, 0.0f, // bottom left
            0.5f, -0.5f, 0.0f // bottom right
    };

    //三个顶点的颜色参数
    private float color[] = {
            1.0f, 0.0f, 0.0f, 1.0f,// top
            0.0f, 1.0f, 0.0f, 1.0f,// bottom left
            0.0f, 0.0f, 1.0f, 1.0f// bottom right
    };

    public PointLineTriangleRender() {
        vertexBuffer = ByteBuffer.allocateDirect(triangleCoords.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexBuffer.put(triangleCoords);
        vertexBuffer.position(0);

        colorBuffer = ByteBuffer.allocateDirect(color.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        colorBuffer.put(color);
        colorBuffer.position(0);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        String vertextShaderStr = AssetsUtils.getVertexStrFromAssert(BaseApplication.instance, "vertex_point_line_triangle_shader");
        String fragmentShaderStr = AssetsUtils.getFragmentStrFromAssert(BaseApplication.instance, "fragment_point_line_triangle_shader");

        mProgram = GlesUtil.createProgram(vertextShaderStr, fragmentShaderStr);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES30.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES30.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);
        GLES30.glUseProgram(mProgram);

        GLES30.glVertexAttribPointer(0, 3, GLES30.GL_FLOAT, false, 0, vertexBuffer);
        GLES30.glEnableVertexAttribArray(0);

        GLES30.glVertexAttribPointer(1, 4, GLES30.GL_FLOAT, false, 0, colorBuffer);
        GLES30.glEnableVertexAttribArray(1);

        GLES30.glDrawArrays(GLES30.GL_POINTS, 0, POSITION_COMPONENT_COUNT);

        GLES30.glLineWidth(3);
        GLES30.glDrawArrays(GLES20.GL_LINE_LOOP, 0, POSITION_COMPONENT_COUNT);

        GLES30.glDrawArrays(GLES20.GL_TRIANGLES, 0, POSITION_COMPONENT_COUNT);

        GLES30.glDisableVertexAttribArray(0);
        GLES30.glDisableVertexAttribArray(1);
    }
}
