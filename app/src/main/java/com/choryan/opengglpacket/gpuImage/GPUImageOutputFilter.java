package com.choryan.opengglpacket.gpuImage;

/**
 * @author: ChoRyan Quan
 * @date: 2021/7/27
 * 全部都draw到FBO上，最后由output这一层统一输出
 */
public class GPUImageOutputFilter extends GPUImageFilter {

    public GPUImageOutputFilter() {
        super();
    }

    @Override
    public void onInitialized() {
        super.onInitialized();
    }

    @Override
    public void onOutputSizeChanged(int width, int height) {
        super.onOutputSizeChanged(width, height);
    }

    @Override
    protected void onDrawArraysPre() {
        super.onDrawArraysPre();
    }

    @Override
    public void onDraw(int textureId) {
        super.onDraw(textureId);
    }

    @Override
    protected void onDrawArraysEnd() {
        super.onDrawArraysEnd();
    }
}
