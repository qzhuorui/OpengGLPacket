//package com.choryan.opengglpacket.gpuImage;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.opengl.GLSurfaceView;
//import android.util.AttributeSet;
//import android.view.View;
//import android.widget.FrameLayout;
//
///**
// * @author: ChoRyan Quan
// * @date: 2021/7/5
// */
//public class GPUImageView extends FrameLayout {
//
//    private GPUImage gpuImage;
//
//    public GPUImageView(Context context) {
//        super(context);
//        init(context, null);
//    }
//
//    public GPUImageView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        init(context, attrs);
//    }
//
//    private void init(Context context, AttributeSet attrs) {
//        GLSurfaceView surfaceView = new GLSurfaceView(context, attrs);
//        gpuImage = new GPUImage(context);
//        gpuImage.setGLSurfaceView(surfaceView);
//        addView(surfaceView);
//    }
//
//    public void setImage(final Bitmap bitmap) {
//        gpuImage.setImage(bitmap);
//    }
//
//    public void setFilter(GPUImageFilter filter) {
//        gpuImage.setFilter(filter);
//    }
//
//    public void removeAllFilter() {
//        gpuImage.removeALlFilter();
//    }
//
//
//}
