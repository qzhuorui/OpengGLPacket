package com.choryan.opengglpacket.filter;

import android.opengl.GLES30;

import com.choryan.opengglpacket.base.BaseApplication;
import com.choryan.opengglpacket.gpuImage.GPUImageFilter;
import com.choryan.opengglpacket.util.AssetsUtils;

/**
 * @author: ChoRyan Quan
 * @date: 2021/8/6
 * @description: Contrast：对比度
 */
public class GPUImageContrastFilter extends GPUImageFilter {

    private int contrastLocation;
    private float contrast;

    public GPUImageContrastFilter() {
        this(1.2f);
    }

    public GPUImageContrastFilter(float contrast) {
        super(NO_FILTER_VERTEX_SHADER, AssetsUtils.getFragmentStrFromAssert(BaseApplication.instance, "fragment_contrast_filter"));
        this.contrast = contrast;
    }

    @Override
    public void onInit() {
        super.onInit();
        contrastLocation = GLES30.glGetUniformLocation(getProgram(), "contrast");
    }

    public void setContrast(float contrast) {
        this.contrast = contrast;
        setFloat(contrastLocation, this.contrast);
    }
}
