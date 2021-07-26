package com.choryan.opengglpacket.gpuImage;

import android.opengl.GLES20;

import com.choryan.opengglpacket.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: ChoRyan Quan
 * @date: 2021/7/6
 */
public class GPUImageFilterGroup extends GPUImageFilter {

    private List<GPUImageFilter> filters;
    private List<GPUImageFilter> mergedFilters;

    //fbo缓冲区和对应的textId
    private int[] frameBuffers;
    private int[] frameBufferTextures;


    public GPUImageFilterGroup() {
        this(null);
        LogUtil.print("GPUImageFilterGroup ******");
    }

    public GPUImageFilterGroup(List<GPUImageFilter> filters) {
        this.filters = filters;
        if (this.filters == null) {
            this.filters = new ArrayList<>();
        } else {
            updateMergedFilters();
        }

        LogUtil.print("GPUImageFilterGroup2 ******");
    }

    public void addFilter(GPUImageFilter aFilter) {
        if (aFilter == null) {
            return;
        }
        filters.add(aFilter);
        updateMergedFilters();
    }

    @Override
    public void onInit() {
        super.onInit();
        LogUtil.print("GPUImageFilterGroup-onInit ******");

        for (GPUImageFilter filter : filters) {
            filter.ifNeedInit();
        }
    }

    @Override
    public void onDestroy() {
        destroyFramebuffers();
        for (GPUImageFilter filter : filters) {
            filter.destroy();
        }
        super.onDestroy();
    }

    private void destroyFramebuffers() {
        if (frameBufferTextures != null) {
            GLES20.glDeleteTextures(frameBufferTextures.length, frameBufferTextures, 0);
            frameBufferTextures = null;
        }
        if (frameBuffers != null) {
            GLES20.glDeleteFramebuffers(frameBuffers.length, frameBuffers, 0);
            frameBuffers = null;
        }
    }

    @Override
    public void onOutputSizeChanged(int width, int height) {
        super.onOutputSizeChanged(width, height);
        LogUtil.print("GPUImageFilterGroup-onOutputSizeChanged ******");

        if (frameBuffers != null) {
            destroyFramebuffers();
        }

        int size = filters.size();
        for (int i = 0; i < size; i++) {
            filters.get(i).onOutputSizeChanged(width, height);
        }

        if (mergedFilters != null && mergedFilters.size() > 0) {
            size = mergedFilters.size();
            frameBuffers = new int[size - 1];
            frameBufferTextures = new int[size - 1];

            for (int i = 0; i < size - 1; i++) {
                GLES20.glGenFramebuffers(1, frameBuffers, i);
                GLES20.glGenTextures(1, frameBufferTextures, i);

                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, frameBufferTextures[i]);
                GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height, 0,
                        GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);
                GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
                GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

                GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBuffers[i]);
                GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, frameBufferTextures[i], 0);

                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
                GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
            }
        }
    }

    @Override
    public void bindVAOData(int vertexBufferId, int frameTextureBufferId, int frameFlipTextureBufferId) {
        super.bindVAOData(vertexBufferId, frameTextureBufferId, frameFlipTextureBufferId);

        if (mergedFilters != null) {
            int size = mergedFilters.size();
            for (int i = 0; i < size; i++) {
                GPUImageFilter filter = mergedFilters.get(i);
                if (i == size - 1) {
                    filter.bindVAOData(vertexBufferId, (size % 2 == 0) ? frameFlipTextureBufferId : frameTextureBufferId, frameFlipTextureBufferId);
                } else {
                    filter.bindVAOData(vertexBufferId, frameTextureBufferId, frameFlipTextureBufferId);
                }
            }
        }
    }

    @Override
    public void onDraw(int textureId) {
        LogUtil.print("GPUImageFilterGroup-onDraw ******");

        runPendingOnDrawTasks();
        if (!isInitialized() || frameBuffers == null || frameBufferTextures == null) {
            return;
        }

        if (mergedFilters != null) {
            int size = mergedFilters.size();
            int previousTexture = textureId;
            for (int i = 0; i < size; i++) {
                GPUImageFilter filter = mergedFilters.get(i);
                boolean isNotLast = i < size - 1;
                if (isNotLast) {
                    GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBuffers[i]);
                    GLES20.glClearColor(0, 0, 0, 0);
                }

                filter.onDraw(previousTexture);

                if (isNotLast) {
                    GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
                    previousTexture = frameBufferTextures[i];
                }
            }
        }
    }

    public List<GPUImageFilter> getFilters() {
        return filters;
    }

    public List<GPUImageFilter> getMergedFilters() {
        return mergedFilters;
    }

    public void updateMergedFilters() {
        if (filters == null) {
            return;
        }

        if (mergedFilters == null) {
            mergedFilters = new ArrayList<>();
        } else {
            mergedFilters.clear();
        }

        List<GPUImageFilter> filters;
        for (GPUImageFilter filter : this.filters) {
            if (filter instanceof GPUImageFilterGroup) {
                ((GPUImageFilterGroup) filter).updateMergedFilters();
                filters = ((GPUImageFilterGroup) filter).getMergedFilters();
                if (filters == null || filters.isEmpty())
                    continue;
                mergedFilters.addAll(filters);
                continue;
            }
            mergedFilters.add(filter);
        }
    }
}
