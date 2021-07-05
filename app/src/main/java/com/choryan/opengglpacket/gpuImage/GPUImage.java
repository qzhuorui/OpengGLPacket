package com.choryan.opengglpacket.gpuImage;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;

/**
 * @author: ChoRyan Quan
 * @date: 2021/7/5
 */
public class GPUImage {

    private final Context context;
    private GLSurfaceView glSurfaceView;

    private final GPUImageRenderer renderer;
    private GPUImageFilter filter;

    public GPUImage(final Context context) {
        this.context = context;
        filter = new GPUImageFilter();
        renderer = new GPUImageRenderer(filter);
    }

    private void setGLSurfaceView(final GLSurfaceView view) {
        glSurfaceView = view;
        glSurfaceView.setEGLContextClientVersion(2);
        glSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        glSurfaceView.getHolder().setFormat(PixelFormat.RGBA_8888);
        glSurfaceView.setRenderer(renderer);
        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        glSurfaceView.requestRender();
    }

}
