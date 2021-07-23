package com.choryan.opengglpacket.activity

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.PointF
import android.os.Bundle
import com.choryan.opengglpacket.R
import com.choryan.opengglpacket.base.BaseActivity
import com.choryan.opengglpacket.filter.GPUImageSobelEdgeDetectionFilter
import com.choryan.opengglpacket.filter.GPUImageVignetteFilter
import kotlinx.android.synthetic.main.activity_draw_bitmap_sobel.*

/**
 * @author: ChoRyan Quan
 * @date: 2021/6/22
 */
class DrawBitmapFilterActivity : BaseActivity(R.layout.activity_draw_bitmap_filter) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        v_gpuimage_view.setImage(BitmapFactory.decodeResource(resources, R.mipmap.test_bitmap))
        btn_render.setOnClickListener {
            val curFilter = GPUImageVignetteFilter()
            v_gpuimage_view.setFilter(curFilter)
        }
        btn_remove_render.setOnClickListener {
            v_gpuimage_view.removeAllFilter()
        }
    }

    companion object {
        fun startActivity(context: Context) {
            try {
                val intent = Intent(context, DrawBitmapFilterActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            } catch (e: Exception) {
            }
        }
    }
}