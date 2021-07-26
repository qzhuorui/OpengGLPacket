package com.choryan.opengglpacket.activity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.PointF
import android.os.Bundle
import androidx.core.graphics.BitmapCompat
import com.choryan.opengglpacket.R
import com.choryan.opengglpacket.base.BaseActivity
import com.choryan.opengglpacket.filter.GPUImageBlendBitmapFilter
import com.choryan.opengglpacket.filter.GPUImageSobelEdgeDetectionFilter
import com.choryan.opengglpacket.filter.GPUImageVignetteFilter
import kotlinx.android.synthetic.main.activity_draw_bitmap_sobel.*

/**
 * @author: ChoRyan Quan
 * @date: 2021/6/22
 */
class BlendBitmapFilterActivity : BaseActivity(R.layout.activity_draw_bitmap_filter) {

    /**
     * 在部分GPU上同一个texture无法既作FBO输出，又作纹理采样输入
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sourceBitmap = BitmapFactory.decodeResource(resources, R.mipmap.teststicker)
        val dstBitmap = BitmapFactory.decodeResource(resources, R.mipmap.test_bitmap)
        v_gpuimage_view.setImage(dstBitmap)
        btn_render.setOnClickListener {
            val curFilter = GPUImageBlendBitmapFilter()
            v_gpuimage_view.setImage(sourceBitmap)
            v_gpuimage_view.setFilter(curFilter)
        }
        btn_remove_render.setOnClickListener {
            v_gpuimage_view.removeAllFilter()
        }
    }

    companion object {
        fun startActivity(context: Context) {
            try {
                val intent = Intent(context, BlendBitmapFilterActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            } catch (e: Exception) {
            }
        }
    }
}