package com.choryan.opengglpacket.filter;

import com.choryan.opengglpacket.base.BaseApplication;
import com.choryan.opengglpacket.gpuImage.GPUImageFilterGroup;
import com.choryan.opengglpacket.util.AssetsUtils;

/**
 * @author: ChoRyan Quan
 * @date: 2021/7/5
 */
public class GPUImageSobelEdgeDetectionFilter extends GPUImageFilterGroup {
    /**
     * Sobel算子
     * h:
     * -1 -2 -1
     * 0  0  0
     * 1  2  1
     * v:
     * -1 0 1
     * -2 0 2
     * -1 0 1
     * <p>
     * sober计算横向纵向乘积
     */

    public GPUImageSobelEdgeDetectionFilter() {
        super();
        addFilter(new GPUImageGrayscaleFilter());
        addFilter(new GPUImage3x3TextureSamplingFilter(AssetsUtils.getFragmentStrFromAssert(BaseApplication.instance, "fragment_sobel_edge_detection_filter")));
    }

    public void setLineSize(final float size) {
        ((GPUImage3x3TextureSamplingFilter) getFilters().get(1)).setLineSize(size);
    }
}
