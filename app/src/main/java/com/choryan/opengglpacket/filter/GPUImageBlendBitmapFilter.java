package com.choryan.opengglpacket.filter;

import android.opengl.GLES30;

import com.choryan.opengglpacket.base.BaseApplication;
import com.choryan.opengglpacket.gpuImage.GPUImageFilter;
import com.choryan.opengglpacket.util.AssetsUtils;

/**
 * @author: ChoRyan Quan
 * @date: 2021/7/26
 */
public class GPUImageBlendBitmapFilter extends GPUImageFilter {

    public GPUImageBlendBitmapFilter() {
        super(NO_FILTER_VERTEX_SHADER, AssetsUtils.getFragmentStrFromAssert(BaseApplication.instance, "fragment_blend_bitmap_filter"));
    }

    @Override
    protected void onDrawArraysPre() {
        super.onDrawArraysPre();
        GLES30.glEnable(GLES30.GL_BLEND);
        GLES30.glBlendFunc(GLES30.GL_SRC_ALPHA, GLES30.GL_ONE_MINUS_SRC_ALPHA);
    }

    @Override
    protected void onDrawArraysEnd() {
        super.onDrawArraysEnd();
        GLES30.glDisable(GLES30.GL_BLEND);
    }
}
