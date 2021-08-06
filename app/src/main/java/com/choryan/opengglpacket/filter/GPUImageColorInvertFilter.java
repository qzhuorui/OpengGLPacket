package com.choryan.opengglpacket.filter;

import com.choryan.opengglpacket.base.BaseApplication;
import com.choryan.opengglpacket.gpuImage.GPUImageFilter;
import com.choryan.opengglpacket.util.AssetsUtils;

/**
 * @author: ChoRyan Quan
 * @date: 2021/8/6
 * @description: 颜色翻转
 */
public class GPUImageColorInvertFilter extends GPUImageFilter {

    public GPUImageColorInvertFilter() {
        super(NO_FILTER_VERTEX_SHADER, AssetsUtils.getFragmentStrFromAssert(BaseApplication.instance, "fragment_color_invert_filter"));
    }
}
