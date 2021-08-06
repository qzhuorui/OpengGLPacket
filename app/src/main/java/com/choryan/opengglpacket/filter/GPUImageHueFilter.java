package com.choryan.opengglpacket.filter;

import android.opengl.GLES20;
import android.opengl.GLES30;

import com.choryan.opengglpacket.base.BaseApplication;
import com.choryan.opengglpacket.gpuImage.GPUImageFilter;
import com.choryan.opengglpacket.util.AssetsUtils;

/**
 * @author: ChoRyan Quan
 * @date: 2021/8/6
 * @description:
 */
public class GPUImageHueFilter extends GPUImageFilter {

    private float hue;
    private int hueLocation;

    public GPUImageHueFilter() {
        this(90.0f);
    }

    public GPUImageHueFilter(final float hue) {
        super(NO_FILTER_VERTEX_SHADER, AssetsUtils.getFragmentStrFromAssert(BaseApplication.instance,"fragment_hue_filter"));
        this.hue = hue;
    }

    @Override
    public void onInit() {
        super.onInit();
        hueLocation = GLES30.glGetUniformLocation(getProgram(), "hueAdjust");
    }

    @Override
    public void onInitialized() {
        super.onInitialized();
        setHue(hue);
    }

    public void setHue(final float hue) {
        this.hue = hue;
        float hueAdjust = (this.hue % 360.0f) * (float) Math.PI / 180.0f;
        setFloat(hueLocation, hueAdjust);
    }

}
