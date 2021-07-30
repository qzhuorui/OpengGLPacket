package com.choryan.opengglpacket.gpuImage;

/**
 * @author: ChoRyan Quan
 * @date: 2021/7/30
 */
public class GPUImageTwoPassFilter extends GPUImageFilterGroup {
    public GPUImageTwoPassFilter(String firstVertexShader, String firstFragmentShader,
                                 String secondVertexShader, String secondFragmentShader) {
        super();
        addFilter(new GPUImageFilter(firstVertexShader, firstFragmentShader));
        addFilter(new GPUImageFilter(secondVertexShader, secondFragmentShader));
    }
}
