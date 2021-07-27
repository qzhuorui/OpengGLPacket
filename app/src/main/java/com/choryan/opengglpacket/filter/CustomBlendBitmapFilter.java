package com.choryan.opengglpacket.filter;

import android.graphics.Bitmap;

import com.choryan.opengglpacket.gpuImage.GPUImageFilter;
import com.choryan.opengglpacket.gpuImage.GPUImageFilterGroup;

/**
 * @author: ChoRyan Quan
 * @date: 2021/7/26
 */
public class CustomBlendBitmapFilter extends GPUImageFilterGroup {

    public CustomBlendBitmapFilter(Bitmap sourceBitmap, Bitmap dstBitmap) {
        super();
        addFilter(new CustomSingleBitmapFilter(dstBitmap, true));
        addFilter(new CustomStickerBitmapFilter(sourceBitmap, true));
        addFilter(new GPUImageFilter());
    }
}
