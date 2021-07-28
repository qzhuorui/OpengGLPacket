package com.choryan.opengglpacket.gpuImage;

import android.opengl.GLES20;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: ChoRyan Quan
 * @date: 2021/7/6
 */
public class GPUImageFilterGroup extends GPUImageFilter {

    private List<GPUImageFilter> filters;
    private List<GPUImageFilter> mergedFilters;

    public GPUImageFilterGroup() {
        this(null);
    }

    public GPUImageFilterGroup(List<GPUImageFilter> filters) {
        this.filters = filters;
        if (this.filters == null) {
            this.filters = new ArrayList<>();
        } else {
            updateMergedFilters();
        }
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
        super.destroyFrameBuffers();
        for (GPUImageFilter filter : filters) {
            filter.destroyFrameBuffers();
        }
    }

    @Override
    public void onOutputSizeChanged(int width, int height) {
        super.onOutputSizeChanged(width, height);
        int size = filters.size();
        for (int i = 0; i < size; i++) {
            filters.get(i).onOutputSizeChanged(width, height);
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
//                    filter.bindVAOData(vertexBufferId, (size % 2 == 0) ? frameFlipTextureBufferId : frameTextureBufferId, frameFlipTextureBufferId);
                    filter.bindVAOData(vertexBufferId, frameFlipTextureBufferId, frameFlipTextureBufferId);
                } else {
                    filter.bindVAOData(vertexBufferId, frameTextureBufferId, frameFlipTextureBufferId);
                }
            }
        }
    }

    @Override
    public void onDraw(int screenTextureId) {
        runPendingOnDrawTasks();
        if (!isInitialized()) {
            return;
        }

        if (mergedFilters != null && mergedFilters.size() > 0) {
            int size = mergedFilters.size();
            int inputTexture = screenTextureId;
            for (int i = 0; i < size; i++) {
                GPUImageFilter filter = mergedFilters.get(i);

                GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, filter.getSelfFboId());

                filter.onDraw(inputTexture);

                GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
                inputTexture = filter.getSelfFboTextureId();
            }
        }
    }

    @Override
    public int getSelfFboId() {
        return mergedFilters.get(mergedFilters.size() - 1).getSelfFboId();
    }

    @Override
    public int getSelfFboTextureId() {
        return mergedFilters.get(mergedFilters.size() - 1).getSelfFboTextureId();
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
