/*
 * Copyright (C) 2018 CyberAgent, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.choryan.opengglpacket.filter;

import android.opengl.GLES20;

import com.choryan.opengglpacket.base.BaseApplication;
import com.choryan.opengglpacket.gpuImage.GPUImageFilter;
import com.choryan.opengglpacket.util.AssetsUtils;

/**
 * Sharpens the picture. <br>
 * <br>
 * sharpness: from -4.0 to 4.0, with 0.0 as the normal level
 */
public class GPUImageSharpenFilter extends GPUImageFilter {

    private int sharpnessLocation;
    private float sharpness;
    private int imageWidthFactorLocation;
    private int imageHeightFactorLocation;

    public GPUImageSharpenFilter() {
        this(4.0f);
    }

    public GPUImageSharpenFilter(final float sharpness) {
        super(AssetsUtils.getVertexStrFromAssert(BaseApplication.instance, "vertex_laplace_edge_detection_filter"),
                AssetsUtils.getFragmentStrFromAssert(BaseApplication.instance, "fragment_laplace_edge_detection_filter"));
        this.sharpness = sharpness;
    }

    @Override
    public void onInit() {
        super.onInit();
        sharpnessLocation = GLES20.glGetUniformLocation(getProgram(), "sharpness");
        imageWidthFactorLocation = GLES20.glGetUniformLocation(getProgram(), "imageWidthFactor");
        imageHeightFactorLocation = GLES20.glGetUniformLocation(getProgram(), "imageHeightFactor");
    }

    @Override
    public void onInitialized() {
        super.onInitialized();
        setSharpness(sharpness);
    }

    @Override
    public void onOutputSizeChanged(final int width, final int height) {
        super.onOutputSizeChanged(width, height);
        setFloat(imageWidthFactorLocation, 1.0f / width);
        setFloat(imageHeightFactorLocation, 1.0f / height);
    }

    public void setSharpness(final float sharpness) {
        this.sharpness = sharpness;
        setFloat(sharpnessLocation, this.sharpness);
    }
}
