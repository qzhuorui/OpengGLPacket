package com.choryan.opengglpacket.gpuImage;

import android.graphics.Bitmap;
import android.opengl.GLES30;
import android.opengl.GLUtils;

import com.choryan.opengglpacket.util.GlesUtil;

/**
 * @author: ChoRyan Quan
 * @date: 2021/7/5
 */
public class GPUImage {


    private Bitmap bitmap;
    private int bitmapWidth = 1;
    private int bitmapHeight = 1;

    private int textureId = -1;

    public GPUImage(Bitmap bitmapParam) {
        this.bitmap = bitmapParam;
        bitmapWidth = bitmapParam.getWidth();
        bitmapHeight = bitmapParam.getHeight();
    }

    public void init() {
        if (null != this.bitmap) {
            textureId = GlesUtil.loadBitmapTexture(this.bitmap);
        } else {
            throw new RuntimeException("GPUImage init bitmap is null");
        }
    }

    public void changeInputTextId(Bitmap bitmap) {
        if (!bitmap.isRecycled()) {
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId);
            GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, 0, bitmap, 0);//bitmap传入已绑定texId中
            bitmap.recycle();
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);
        }
    }

    public int getTextureId() {
        return textureId;
    }

    public boolean canDraw() {
        return textureId != -1;
    }

    public void destroy() {
        textureId = -1;
        if (null != this.bitmap && !this.bitmap.isRecycled()) {
            this.bitmap.recycle();
        }
    }
}
