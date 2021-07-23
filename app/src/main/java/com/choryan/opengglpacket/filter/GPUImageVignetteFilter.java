package com.choryan.opengglpacket.filter;

import android.graphics.PointF;
import android.opengl.GLES20;

import com.choryan.opengglpacket.base.BaseApplication;
import com.choryan.opengglpacket.gpuImage.GPUImageFilter;
import com.choryan.opengglpacket.util.AssetsUtils;

/**
 * @author: ChoRyan Quan
 * @date: 2021/7/5
 */
public class GPUImageVignetteFilter extends GPUImageFilter {
    private int vignetteCenterLocation;
    private PointF vignetteCenter;
    private int vignetteColorLocation;
    private float[] vignetteColor;
    private int vignetteStartLocation;
    private float vignetteStart;
    private int vignetteEndLocation;
    private float vignetteEnd;

    public GPUImageVignetteFilter() {
        this(new PointF(0.5f, 0.5f), new float[]{0.0f, 0.0f, 0.0f}, 0.2f, 1.0f);
    }

    public GPUImageVignetteFilter(final PointF vignetteCenter, final float[] vignetteColor, final float vignetteStart, final float vignetteEnd) {
        super(NO_FILTER_VERTEX_SHADER, AssetsUtils.getFragmentStrFromAssert(BaseApplication.instance, "fragment_vignette_filter"));
        this.vignetteCenter = vignetteCenter;
        this.vignetteColor = vignetteColor;
        this.vignetteStart = vignetteStart;
        this.vignetteEnd = vignetteEnd;

    }

    @Override
    public void onInit() {
        super.onInit();
        vignetteCenterLocation = GLES20.glGetUniformLocation(getProgram(), "vignetteCenter");
        vignetteColorLocation = GLES20.glGetUniformLocation(getProgram(), "vignetteColor");
        vignetteStartLocation = GLES20.glGetUniformLocation(getProgram(), "vignetteStart");
        vignetteEndLocation = GLES20.glGetUniformLocation(getProgram(), "vignetteEnd");
    }

    @Override
    public void onInitialized() {
        super.onInitialized();
        setVignetteCenter(vignetteCenter);
        setVignetteColor(vignetteColor);
        setVignetteStart(vignetteStart);
        setVignetteEnd(vignetteEnd);
    }

    public void setVignetteCenter(final PointF vignetteCenter) {
        this.vignetteCenter = vignetteCenter;
        setPoint(vignetteCenterLocation, this.vignetteCenter);
    }

    public void setVignetteColor(final float[] vignetteColor) {
        this.vignetteColor = vignetteColor;
        setFloatVec3(vignetteColorLocation, this.vignetteColor);
    }

    public void setVignetteStart(final float vignetteStart) {
        this.vignetteStart = vignetteStart;
        setFloat(vignetteStartLocation, this.vignetteStart);
    }

    public void setVignetteEnd(final float vignetteEnd) {
        this.vignetteEnd = vignetteEnd;
        setFloat(vignetteEndLocation, this.vignetteEnd);
    }
}
