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


    private final Context context;

    private GPUImageFilter filter;
    private GPUImageRenderer renderer;
    private GLSurfaceView glSurfaceView;


    public GPUImage(final Context context) {
        //淦 2.0没有VAO
        if (!supportsOpenGLES3(context)) {
            throw new IllegalStateException("OpenGL ES 3.0 is not supported on this phone.");
        }
        this.context = context;
        filter = new GPUImageFilter();
        renderer = new GPUImageRenderer(filter);
    }

    private boolean supportsOpenGLES3(final Context context) {
        final ActivityManager activityManager = (ActivityManager)
                context.getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo =
                activityManager.getDeviceConfigurationInfo();
        return configurationInfo.reqGlEsVersion >= 0x30000;
    }

    public void setGLSurfaceView(final GLSurfaceView view) {
        glSurfaceView = view;
        glSurfaceView.setEGLContextClientVersion(3);
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
        renderer.setImageBitmap(bitmap, false);
        requestRender();
    }

    public void removeALlFilter() {
        renderer.removeAllFilter();
        requestRender();
    }

}
