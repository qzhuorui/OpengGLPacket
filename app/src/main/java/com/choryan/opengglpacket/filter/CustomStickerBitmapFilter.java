package com.choryan.opengglpacket.filter;

import android.graphics.Bitmap;
import android.opengl.GLES30;

import com.choryan.opengglpacket.base.BaseApplication;
import com.choryan.opengglpacket.gpuImage.GPUImageFilter;
import com.choryan.opengglpacket.util.AssetsUtils;
import com.choryan.opengglpacket.util.GlesUtil;

/**
 * @author: ChoRyan Quan
 * @date: 2021/7/26
 */
public class CustomStickerBitmapFilter extends GPUImageFilter {

    private boolean isUseFBO;
    private Bitmap sourceBitmap;
    private int sourceBitmapTextureId, sourceBitmapW, sourceBitmapH;

    public CustomStickerBitmapFilter(Bitmap sourceBitmap, boolean isUseFBO) {
        super(NO_FILTER_VERTEX_SHADER, AssetsUtils.getFragmentStrFromAssert(BaseApplication.instance, "fragment_sticker_bitmap_filter"));
        this.sourceBitmap = sourceBitmap;
        this.isUseFBO = isUseFBO;
    }

    @Override
    public void onInitialized() {
        super.onInitialized();
        this.sourceBitmapTextureId = GlesUtil.loadBitmapTexture(sourceBitmap);
        this.sourceBitmapW = sourceBitmap.getWidth();
        this.sourceBitmapH = sourceBitmap.getHeight();
    }

    @Override
    public void onOutputSizeChanged(int width, int height) {
//        super.onOutputSizeChanged(width, height);
        GLES30.glViewport(0, 0, 100, 100);
    }

    @Override
    protected boolean isUseFbo() {
        return isUseFBO;
    }

    @Override
    protected int onBindTexturePre() {
        return sourceBitmapTextureId;
    }

    @Override
    protected void onDrawArraysPre() {
        super.onDrawArraysPre();
//        GLES30.glEnable(GLES30.GL_BLEND);
//        GLES30.glBlendFunc(GLES30.GL_SRC_ALPHA, GLES30.GL_ONE_MINUS_SRC_ALPHA);
    }

    @Override
    protected void onDrawArraysEnd() {
        super.onDrawArraysEnd();
//        GLES30.glDisable(GLES30.GL_BLEND);
    }
}
