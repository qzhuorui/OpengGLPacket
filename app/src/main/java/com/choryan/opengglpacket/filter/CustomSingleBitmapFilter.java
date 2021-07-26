package com.choryan.opengglpacket.filter;

import android.graphics.Bitmap;
import android.opengl.GLES30;

import com.choryan.opengglpacket.gpuImage.GPUImageFilter;
import com.choryan.opengglpacket.util.GlesUtil;

/**
 * @author: ChoRyan Quan
 * @date: 2021/7/26
 */
public class CustomSingleBitmapFilter extends GPUImageFilter {

    private boolean isUseFBO;
    private Bitmap dstBitmap;
    private int dstBitmapTextureId, dstBitmapW, dstBitmapH;

    public CustomSingleBitmapFilter(Bitmap sourceBitmap, boolean isUseFBO) {
        super();
        this.dstBitmap = sourceBitmap;
        this.isUseFBO = isUseFBO;
    }

    @Override
    public void onInitialized() {
        super.onInitialized();
        this.dstBitmapTextureId = GlesUtil.loadBitmapTexture(dstBitmap);
        this.dstBitmapW = dstBitmap.getWidth();
        this.dstBitmapH = dstBitmap.getHeight();
    }

    @Override
    public void onOutputSizeChanged(int width, int height) {
//        super.onOutputSizeChanged(width, height);
        GLES30.glViewport(0, 0, dstBitmapW, dstBitmapH);
    }

    @Override
    protected boolean isUseFbo() {
        return isUseFBO;
    }

    @Override
    protected int onBindTexturePre() {
        return dstBitmapTextureId;
    }

}
