package com.choryan.opengglpacket.filter;

import android.opengl.GLES30;

import com.choryan.opengglpacket.base.BaseApplication;
import com.choryan.opengglpacket.gpuImage.GPUImageFilter;
import com.choryan.opengglpacket.util.AssetsUtils;

/**
 * @author: ChoRyan Quan
 * @date: 2021/8/6
 * @description: 像素块滤镜
 */
public class GPUImagePixelationFilter extends GPUImageFilter {

    private int imageWidthFactorLocation;
    private int imageHeightFactorLocation;
    private int pixelLocation;

    private float pixel;

    public GPUImagePixelationFilter() {
        super(NO_FILTER_VERTEX_SHADER, AssetsUtils.getFragmentStrFromAssert(BaseApplication.instance, "fragment_pixelation_filter"));
        pixel = 1.0f;
    }

    @Override
    public void onInit() {
        super.onInit();
        imageWidthFactorLocation = GLES30.glGetUniformLocation(getProgram(), "imageWidthFactor");
        imageHeightFactorLocation = GLES30.glGetUniformLocation(getProgram(), "imageHeightFactor");
        pixelLocation = GLES30.glGetUniformLocation(getProgram(), "pixel");
    }

    @Override
    public void onInitialized() {
        super.onInitialized();
        setPixel(pixel);
    }

    @Override
    public void onOutputSizeChanged(int width, int height) {
        super.onOutputSizeChanged(width, height);
        setFloat(imageWidthFactorLocation, 1.0f / width);
        setFloat(imageHeightFactorLocation, 1.0f / height);
    }

    public void setPixel(final float pixel) {
        this.pixel = pixel;
        setFloat(pixelLocation, this.pixel);
    }
}
