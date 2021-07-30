package com.choryan.opengglpacket.filter;

import com.choryan.opengglpacket.gpuImage.GPUImageFilterGroup;

/**
 * @author: ChoRyan Quan
 * @date: 2021/7/5
 */
public class CustomGaussianBlendFilter extends GPUImageFilterGroup {

    public CustomGaussianBlendFilter() {
        super();
        addFilter(new GPUImageGaussianBlurFilter());
        addFilter(new CustomTwoInputFilter());
    }

    public void setBlurSize(float blurSize) {
        ((GPUImageGaussianBlurFilter) getFilters().get(0)).setBlurSize(blurSize);
    }

    public void setInputTexture(int inputImageTexture2) {
        ((CustomTwoInputFilter) getFilters().get(1)).setInputTexture(inputImageTexture2);
    }
}
