package com.choryan.opengglpacket.filter;

import com.choryan.opengglpacket.base.BaseApplication;
import com.choryan.opengglpacket.gpuImage.GPUImageTwoPassTextureSamplingFilter;
import com.choryan.opengglpacket.util.AssetsUtils;

/**
 * @author: ChoRyan Quan
 * @date: 2021/7/30
 * @description: 高斯模糊，横向和纵向
 */
public class GPUImageGaussianBlurFilter extends GPUImageTwoPassTextureSamplingFilter {

    protected float blurSize;

    public GPUImageGaussianBlurFilter() {
        this(15f);
    }

    public GPUImageGaussianBlurFilter(float blurSize) {
        super(AssetsUtils.getVertexStrFromAssert(BaseApplication.instance, "vertex_gaussian_blur_filter"),
                AssetsUtils.getFragmentStrFromAssert(BaseApplication.instance, "fragment_gaussian_blur_filter"),
                AssetsUtils.getVertexStrFromAssert(BaseApplication.instance, "vertex_gaussian_blur_filter"),
                AssetsUtils.getFragmentStrFromAssert(BaseApplication.instance, "fragment_gaussian_blur_filter"));
        this.blurSize = blurSize;
    }

    @Override
    public void onInitialized() {
        super.onInitialized();
        setBlurSize(blurSize);
    }

    @Override
    public float getVerticalTexelOffsetRatio() {
        return blurSize;
    }

    @Override
    public float getHorizontalTexelOffsetRatio() {
        return blurSize;
    }

    /**
     * A multiplier for the blur size, ranging from 0.0 on up, with a default of 1.0
     *
     * @param blurSize from 0.0 on up, default 1.0
     */
    public void setBlurSize(float blurSize) {
        this.blurSize = blurSize;
        runOnDraw(new Runnable() {
            @Override
            public void run() {
                initTexelOffsets();
            }
        });
    }
}
