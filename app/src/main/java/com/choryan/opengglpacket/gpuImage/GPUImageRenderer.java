package com.choryan.opengglpacket.gpuImage;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;

import com.choryan.opengglpacket.util.LogUtil;
import com.choryan.opengglpacket.util.OpenGlUtils;
import com.choryan.opengglpacket.util.Rotation;
import com.choryan.opengglpacket.util.TextureRotationUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.LinkedList;
import java.util.Queue;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static com.choryan.opengglpacket.util.TextureRotationUtil.CUBE;
import static com.choryan.opengglpacket.util.TextureRotationUtil.TEXTURE_NO_ROTATION;
import static com.choryan.opengglpacket.util.TextureRotationUtil.TEXTURE_NO_ROTATION0;

/**
 * @author: ChoRyan Quan
 * @date: 2021/7/5
 */
public class GPUImageRenderer implements GLSurfaceView.Renderer {

    private static final int NO_IMAGE = -1;

    private FloatBuffer glCubeBuffer;
    protected int mVertexBufferId;
    private FloatBuffer glTextureBuffer;
    protected int mFrameTextureBufferId;
    private FloatBuffer glTextureFlipBuffer;
    protected int mFrameFlipTextureBufferId;

    private int glTextureId = NO_IMAGE;
    private GPUImageFilter curFilter;
    private GPUImageFilter lastFilter;

    private final Queue<Runnable> runOnDraw;
    private final Queue<Runnable> runOnDrawEnd;

    private int outputWidth;
    private int outputHeight;

    private int imageWidth;
    private int imageHeight;

    private float backgroundRed = 0;
    private float backgroundGreen = 0;
    private float backgroundBlue = 0;

    public GPUImageRenderer(final GPUImageFilter filter) {
        curFilter = filter;
        runOnDraw = new LinkedList<>();
        runOnDrawEnd = new LinkedList<>();

        LogUtil.print("GPUImageRenderer");
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        LogUtil.print("GPUImageRenderer-onSurfaceCreated ********************");
        initVertexBufferObjects();
        GLES20.glClearColor(backgroundRed, backgroundGreen, backgroundBlue, 1);
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);
        curFilter.ifNeedInit();
    }

    private void initVertexBufferObjects() {
        int[] vbo = new int[3];
        GLES30.glGenBuffers(3, vbo, 0);

        glCubeBuffer = ByteBuffer.allocateDirect(CUBE.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(CUBE);
        glCubeBuffer.position(0);
        mVertexBufferId = vbo[0];
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, mVertexBufferId);
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, CUBE.length * 4, glCubeBuffer, GLES30.GL_STATIC_DRAW);

        glTextureBuffer = ByteBuffer.allocateDirect(TEXTURE_NO_ROTATION.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(TEXTURE_NO_ROTATION);
        glTextureBuffer.position(0);
        mFrameTextureBufferId = vbo[1];
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, mFrameTextureBufferId);
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, TEXTURE_NO_ROTATION.length * 4, glTextureBuffer, GLES30.GL_STATIC_DRAW);

        float[] flipTexture = TextureRotationUtil.getRotation(Rotation.NORMAL, false, true);
        glTextureFlipBuffer = ByteBuffer.allocateDirect(flipTexture.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        glTextureFlipBuffer.put(flipTexture).position(0);
        mFrameFlipTextureBufferId = vbo[2];
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, mFrameFlipTextureBufferId);
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, flipTexture.length * 4, glTextureFlipBuffer, GLES30.GL_STATIC_DRAW);

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        LogUtil.print("GPUImageRenderer-onSurfaceChanged ********************");

        outputWidth = width;
        outputHeight = height;
        GLES20.glViewport(0, 0, width, height);
        curFilter.onOutputSizeChanged(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        LogUtil.print("GPUImageRenderer-onDrawFrame ********************");

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        runAll(runOnDraw);
        curFilter.onDraw(glTextureId, mVertexBufferId, mFrameTextureBufferId, mFrameFlipTextureBufferId);
        runAll(runOnDrawEnd);
    }

    private void runAll(Queue<Runnable> queue) {
        synchronized (queue) {
            while (!queue.isEmpty()) {
                queue.poll().run();
            }
        }
    }

    public void setFilter(final GPUImageFilter targetFilter) {
        runOnDraw(new Runnable() {
            @Override
            public void run() {
                final GPUImageFilter oldFilter = curFilter;
                curFilter = targetFilter;
                if (oldFilter != null) {
                    oldFilter.destroy();
                }
                curFilter.ifNeedInit();
                curFilter.onOutputSizeChanged(outputWidth, outputHeight);
            }
        });
    }

    public void removeAllFilter() {
        curFilter.destroy();
    }

    public void setImageBitmap(final Bitmap bitmap, final boolean recycle) {
        if (bitmap == null) {
            return;
        }
        runOnDraw(new Runnable() {
            @Override
            public void run() {
                Bitmap resizedBitmap = null;
                if (bitmap.getWidth() % 2 == 1) {
                    resizedBitmap = Bitmap.createBitmap(bitmap.getWidth() + 1, bitmap.getHeight(),
                            Bitmap.Config.ARGB_8888);
                    resizedBitmap.setDensity(bitmap.getDensity());
                    Canvas can = new Canvas(resizedBitmap);
                    can.drawARGB(0x00, 0x00, 0x00, 0x00);
                    can.drawBitmap(bitmap, 0, 0, null);
                }

                glTextureId = OpenGlUtils.loadTexture(
                        resizedBitmap != null ? resizedBitmap : bitmap, glTextureId, recycle);
                if (resizedBitmap != null) {
                    resizedBitmap.recycle();
                }
                imageWidth = bitmap.getWidth();
                imageHeight = bitmap.getHeight();
            }
        });
    }

    protected void runOnDraw(final Runnable runnable) {
        synchronized (runOnDraw) {
            runOnDraw.add(runnable);
        }
    }

    protected void runOnDrawEnd(final Runnable runnable) {
        synchronized (runOnDrawEnd) {
            runOnDrawEnd.add(runnable);
        }
    }
}
