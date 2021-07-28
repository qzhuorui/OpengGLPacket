package com.choryan.opengglpacket.gpuImage

import android.content.Context
import android.graphics.Bitmap
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import com.choryan.opengglpacket.filter.CustomWaterMarkBitmapFilter
import com.choryan.opengglpacket.util.GlesUtil
import java.util.*
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * @author: ChoRyan Quan
 * @date: 2021/7/27
 */
class GPUImageView @JvmOverloads constructor(context: Context, attributes: AttributeSet? = null) :
    GLSurfaceView(context, attributes), GLSurfaceView.Renderer {

    private var mVertexBufferId = 0
    private var mFrameTextureBufferId = 0
    private var mFrameFlipTextureBufferId = 0

    private var gpuImage: GPUImage? = null
    private val outputFilter = GPUImageOutputFilter()
    private val waterMarkFilter = CustomWaterMarkBitmapFilter()

    private var commonFilterGroup: GPUImageFilterGroup? = null
    private val pendingRunnableList = LinkedList<Runnable>()

    var windowWidth: Int = 0
    var windowHeight: Int = 0

    init {
        setEGLContextClientVersion(3)
        setRenderer(this)
        renderMode = RENDERMODE_WHEN_DIRTY
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        val vbo = GlesUtil.initVertexBufferObjects()
        mVertexBufferId = vbo[0]
        mFrameTextureBufferId = vbo[1]
        mFrameFlipTextureBufferId = vbo[2]
        outputFilter.ifNeedInit()
        waterMarkFilter.ifNeedInit()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        if (width > 0 && height > 0) {
            windowWidth = width
            windowHeight = height
            outputFilter.onOutputSizeChanged(width, height)
            outputFilter.bindVAOData(mVertexBufferId, mFrameTextureBufferId, mFrameFlipTextureBufferId)
            waterMarkFilter.onOutputSizeChanged(0, 0)
            waterMarkFilter.bindVAOData(mVertexBufferId, mFrameTextureBufferId, mFrameFlipTextureBufferId)
        }
        while (pendingRunnableList.size > 0) {
            pendingRunnableList.poll()?.run()
        }
    }

    override fun onDrawFrame(gl: GL10?) {
        val inputTextureId = gpuImage!!.textureId
        if (inputTextureId != -1) {
            var outPutTextureId = inputTextureId
            commonFilterGroup?.let { _commonFilter ->
                _commonFilter.onDraw(outPutTextureId)
                outPutTextureId = _commonFilter.selfFboTextureId
            }
            GLES30.glViewport(0, 0, width, height)
            outputFilter.onDraw(outPutTextureId)
            waterMarkFilter.onDraw(-1)
        } else {
            throw RuntimeException("onDrawFrame text is -1")
        }
    }

    override fun onDetachedFromWindow() {
        preserveEGLContextOnPause = false
        queueEvent {
            gpuImage?.destroy()
            commonFilterGroup?.destroy()
            outputFilter.destroy()
            waterMarkFilter.destroy()
        }
        requestRender()
        super.onDetachedFromWindow()
    }

    fun addFilter(filter: GPUImageFilter?) {
        if (null == filter) {
            return
        }
        queueEvent {
            commonFilterGroup?.destroy()
            commonFilterGroup = if (filter is GPUImageFilterGroup) {
                filter.ifNeedInit()
                filter.onOutputSizeChanged(windowWidth, windowHeight)
                filter.bindVAOData(mVertexBufferId, mFrameTextureBufferId, mFrameFlipTextureBufferId)
                filter
            } else {
                val add = GPUImageFilterGroup()
                add.addFilter(filter)
                add.ifNeedInit()
                add.onOutputSizeChanged(windowWidth, windowHeight)
                add.bindVAOData(mVertexBufferId, mFrameTextureBufferId, mFrameFlipTextureBufferId)
                add
            }
        }
        requestRender()
    }

    fun removeFilter() {
        queueEvent {
            commonFilterGroup?.destroy()
            commonFilterGroup = null
        }
        requestRender()
    }

    fun setImageInput(gpuImage: GPUImage) {
        this.gpuImage = gpuImage
        pendingRunnableList.add(Runnable {
            gpuImage.init()
        })
    }

    fun changeImageInput(bitmap: Bitmap) {
        queueEvent {
            gpuImage?.changeInputTextId(bitmap)
        }
        requestRender()
    }

}