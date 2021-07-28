package com.choryan.opengglpacket.gpuImage;

/**
 * @author: ChoRyan Quan
 * @date: 2021/7/27
 * 全部都draw到FBO上，最后由output这一层统一输出
 */
public class GPUImageOutputFilter extends GPUImageFilter {

    private int uniformTransformMatrixLocation;
    private int uniformRotateMatrixLocation;
    private int UniformOrthographicMatrixLocation;

    private final float[] transformMatrixArray = new float[16];
    private final float[] rotateMatrixArray = new float[16];
    private final float[] orthographicMatrix = new float[16];


    public GPUImageOutputFilter() {
        super();
//        super(AssetsUtils.getVertexStrFromAssert(BaseApplication.instance, "vertex_output_filter"), NO_FILTER_FRAGMENT_SHADER);
//        Matrix.setIdentityM(transformMatrixArray, 0);
//        Matrix.setRotateM(rotateMatrixArray, 0, 0, 0, 0, 1);
    }

    @Override
    public void onInitialized() {
        super.onInitialized();
//        UniformOrthographicMatrixLocation = GLES30.glGetUniformLocation(getProgram(), "orthographicMatrix");
//        uniformTransformMatrixLocation = GLES30.glGetUniformLocation(getProgram(), "transMatrix");
//        uniformRotateMatrixLocation = GLES30.glGetUniformLocation(getProgram(), "rotateMatrix");
    }

    @Override
    public void onOutputSizeChanged(int width, int height) {
        super.onOutputSizeChanged(width, height);
//        Matrix.orthoM(orthographicMatrix, 0, -1.0f, 1.0f,
//                -1.0f * (float) height / (float) width,
//                1.0f * (float) height / (float) width, -1.0f, 1.0f);
    }

    @Override
    protected void onDrawArraysPre() {
        super.onDrawArraysPre();
//        GLES30.glUniformMatrix4fv(uniformTransformMatrixLocation, 1, false, transformMatrixArray, 0);
//        GLES30.glUniformMatrix4fv(uniformRotateMatrixLocation, 1, false, rotateMatrixArray, 0);
//        GLES30.glUniformMatrix4fv(UniformOrthographicMatrixLocation, 1, false, orthographicMatrix, 0);
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
