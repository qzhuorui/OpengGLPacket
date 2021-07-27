package com.choryan.opengglpacket.activity

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import com.choryan.opengglpacket.R
import com.choryan.opengglpacket.base.BaseActivity
import com.choryan.opengglpacket.filter.GPUImageGrayscaleFilter
import com.choryan.opengglpacket.gpuImage.GPUImage
import kotlinx.android.synthetic.main.activity_draw_bitmap_sobel.*

/**
 * @author: ChoRyan Quan
 * @date: 2021/6/22
 */
class NewGPUImageActivity : BaseActivity(R.layout.activity_draw_bitmap_filter) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sourceBitmap = BitmapFactory.decodeResource(resources, R.mipmap.teststicker)
        val dstBitmap = BitmapFactory.decodeResource(resources, R.mipmap.test_bitmap)
        v_gpuimage_view.setImageInput(GPUImage(dstBitmap))
        btn_render.setOnClickListener {
            v_gpuimage_view.addFilter(GPUImageGrayscaleFilter())
        }
        btn_remove_render.setOnClickListener {
        }
    }

    companion object {
        fun startActivity(context: Context) {
            try {
                val intent = Intent(context, NewGPUImageActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            } catch (e: Exception) {
            }
        }
    }
}