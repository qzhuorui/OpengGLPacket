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
 * @description: 水印无法直接使用FilterGroup链，因为需要拿到上一层的数据；倒是可以使用双输入源，但是不好控制。
 * 所以引入单独的水印Filter，并且再引入OutPutFilter作为最终的输出控制。
 */
public class CustomWaterMarkBitmapFilter extends GPUImageFilter {

    private Bitmap sourceBitmap;
    private int sourceBitmapTextureId, sourceBitmapW, sourceBitmapH;

    public CustomWaterMarkBitmapFilter() {
        super(NO_FILTER_VERTEX_SHADER, AssetsUtils.getFragmentStrFromAssert(BaseApplication.instance, "fragment_watermark_bitmap_filter"));
        sourceBitmap = BitmapFactory.decodeResource(BaseApplication.instance.getResources(), R.mipmap.teststicker);
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
        GLES30.glBlendFunc(GLES30.GL_ONE, GLES30.GL_ONE_MINUS_SRC_ALPHA);
    }

    @Override
    public void onDraw(int textureId) {
        GLES30.glViewport(0, 0, sourceBitmapW / 2, sourceBitmapH / 2);
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
