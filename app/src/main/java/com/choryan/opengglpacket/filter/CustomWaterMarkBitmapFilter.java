package com.choryan.opengglpacket.filter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES30;

import com.choryan.opengglpacket.R;
import com.choryan.opengglpacket.base.BaseApplication;
import com.choryan.opengglpacket.gpuImage.GPUImageFilter;
import com.choryan.opengglpacket.util.AssetsUtils;
import com.choryan.opengglpacket.util.GlesUtil;

/**
 * @author: ChoRyan Quan
 * @date: 2021/7/26
 */
public class CustomWaterMarkBitmapFilter extends GPUImageFilter {

    private Bitmap sourceBitmap;
    private int sourceBitmapTextureId, sourceBitmapW, sourceBitmapH;

    public CustomWaterMarkBitmapFilter() {
        super(NO_FILTER_VERTEX_SHADER, AssetsUtils.getFragmentStrFromAssert(BaseApplication.instance, "fragment_watermark_bitmap_filter"));
        sourceBitmap = BitmapFactory.decodeResource(BaseApplication.instance.getResources(), R.mipmap.wnmt110_4);
        sourceBitmapW = sourceBitmap.getWidth();
        sourceBitmapH = sourceBitmap.getHeight();
    }

    @Override
    public void onInitialized() {
        super.onInitialized();
        this.sourceBitmapTextureId = GlesUtil.loadBitmapTexture(sourceBitmap);
    }

    @Override
    protected void onDrawArraysPre() {
        super.onDrawArraysPre();
        //透明图时需要blend
        GLES30.glEnable(GLES30.GL_BLEND);
        GLES30.glBlendEquation(GLES30.GL_FUNC_ADD);
        GLES30.glBlendFunc(GLES30.GL_SRC_ALPHA, GLES30.GL_ONE_MINUS_SRC_ALPHA);
    }

    @Override
    public void onDraw(int textureId) {
        GLES30.glViewport(0, 0, sourceBitmapW, sourceBitmapH);
        super.onDraw(sourceBitmapTextureId);
    }

    @Override
    protected void onDrawArraysEnd() {
        super.onDrawArraysEnd();
        GLES30.glDisable(GLES30.GL_BLEND);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sourceBitmap.recycle();
    }
}
