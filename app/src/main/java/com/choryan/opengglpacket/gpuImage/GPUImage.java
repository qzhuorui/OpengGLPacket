package com.choryan.opengglpacket.gpuImage;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;

/**
 * @author: ChoRyan Quan
 * @date: 2021/7/5
 */
public class GPUImage {

    public enum ScaleType {CENTER_INSIDE, CENTER_CROP}

    private final Context context;
    private GLSurfaceView glSurfaceView;

    private Bitmap currentBitmap;
    private GPUImageFilter filter;
    private final GPUImageRenderer renderer;

    private ScaleType scaleType = ScaleType.CENTER_CROP;


    public GPUImage(final Context context) {
        if (!supportsOpenGLES2(context)) {
            throw new IllegalStateException("OpenGL ES 2.0 is not supported on this phone.");
        }
        this.context = context;
        filter = new GPUImageFilter();
        renderer = new GPUImageRenderer(filter);
    }

    private boolean supportsOpenGLES2(final Context context) {
        final ActivityManager activityManager = (ActivityManager)
                context.getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo =
                activityManager.getDeviceConfigurationInfo();
        return configurationInfo.reqGlEsVersion >= 0x20000;
    }

    public void setGLSurfaceView(final GLSurfaceView view) {
        glSurfaceView = view;
        glSurfaceView.setEGLContextClientVersion(2);
        glSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        glSurfaceView.getHolder().setFormat(PixelFormat.RGBA_8888);
        glSurfaceView.setRenderer(renderer);
        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        glSurfaceView.requestRender();
    }

    public void requestRender() {
        if (glSurfaceView != null) {
            glSurfaceView.requestRender();
        }
    }

    public void setFilter(final GPUImageFilter filter) {
        this.filter = filter;
        renderer.setFilter(this.filter);
        requestRender();
    }

    public void setImage(final Bitmap bitmap) {
        currentBitmap = bitmap;
        renderer.setImageBitmap(bitmap, false);
        requestRender();
    }

}
