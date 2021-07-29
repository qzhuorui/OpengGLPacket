package com.choryan.opengglpacket.filter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES30;

import com.choryan.opengglpacket.R;
import com.choryan.opengglpacket.base.BaseApplication;
import com.choryan.opengglpacket.gpuImage.GPUImageFilter;
import com.choryan.opengglpacket.util.AssetsUtils;
import com.choryan.opengglpacket.util.GlesUtil;

/**
 * @author: ChoRyan Quan
 * @date: 2021/7/28
 */
public class CustomTwoInputFilter extends GPUImageFilter {

    private Bitmap sourceBitmap;
    private int sourceBitmapTextureId, sourceBitmapW, sourceBitmapH;

    private int glUniformTexture2;
    private int glAttribTextureCoordinate2;

    public CustomTwoInputFilter() {
        super(AssetsUtils.getVertexStrFromAssert(BaseApplication.instance, "vertex_two_input_texture"),
                AssetsUtils.getFragmentStrFromAssert(BaseApplication.instance, "fragment_two_input_texture"));
        sourceBitmap = BitmapFactory.decodeResource(BaseApplication.instance.getResources(), R.mipmap.teststicker);
        sourceBitmapW = sourceBitmap.getWidth();
        sourceBitmapH = sourceBitmap.getHeight();
    }

    @Override
    public void onInit() {
        super.onInit();
        glUniformTexture2 = GLES30.glGetAttribLocation(getProgram(), "inputImageTexture2");
        glAttribTextureCoordinate2 = GLES30.glGetAttribLocation(getProgram(), "inputTextureCoordinate2");
    }

    @Override
    public void onInitialized() {
        super.onInitialized();
        this.sourceBitmapTextureId = GlesUtil.loadBitmapTexture(sourceBitmap);
    }

    @Override
    public void bindVAOData(int vertexBufferId, int frameTextureBufferId, int frameFlipTextureBufferId) {
        super.bindVAOData(vertexBufferId, frameTextureBufferId, frameFlipTextureBufferId);
        GLES30.glBindVertexArray(getCurVaoId());

        GLES30.glEnableVertexAttribArray(glAttribTextureCoordinate2);

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, frameTextureBufferId);
        GLES30.glVertexAttribPointer(glAttribTextureCoordinate2, 2, GLES30.GL_FLOAT, false, 0, 0);

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0);
        GLES30.glBindVertexArray(0);
        GLES30.glDisableVertexAttribArray(glAttribTextureCoordinate2);
    }

    @Override
    protected void onDrawArraysPre() {
        super.onDrawArraysPre();

        if (sourceBitmapTextureId != -1) {
            GLES30.glActiveTexture(GLES30.GL_TEXTURE1);
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, sourceBitmapTextureId);
            GLES30.glUniform1i(glUniformTexture2, 1);
        } else {
            throw new RuntimeException("GPUImageFilter onDraw textId is -1");
        }

        GLES30.glEnable(GLES30.GL_BLEND);
        GLES30.glBlendEquation(GLES30.GL_FUNC_ADD);
        GLES30.glBlendFunc(GLES30.GL_ONE, GLES30.GL_ONE_MINUS_SRC_ALPHA);
    }

    @Override
    public void onDraw(int textureId) {
        GLES30.glViewport(0, 0, sourceBitmapW / 2, sourceBitmapH / 2);
        super.onDraw(textureId);
    }

    @Override
    protected void onDrawArraysEnd() {
        super.onDrawArraysEnd();
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 1);
        GLES30.glDisable(GLES30.GL_BLEND);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sourceBitmap.recycle();
    }
}
