package com.choryan.opengglpacket.activity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.choryan.opengglpacket.R
import com.choryan.opengglpacket.base.BaseActivity
import com.choryan.opengglpacket.filter.GPUImageSobelEdgeDetectionFilter
import com.choryan.opengglpacket.render.DrawBitmapRender
import com.choryan.opengglpacket.render.DrawBitmapSobelRender
import com.choryan.opengglpacket.render.DrawTextRender
import kotlinx.android.synthetic.main.activity_draw_bitmap_sobel.*
import kotlinx.android.synthetic.main.activity_draw_text.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author: ChoRyan Quan
 * @date: 2021/6/22
 */
class DrawBitmapSobelActivity : BaseActivity(R.layout.activity_draw_bitmap_sobel) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        v_gpuimage_view.setImage(BitmapFactory.decodeResource(resources, R.mipmap.wnmt110_4))
        btn_render.setOnClickListener {
            val curFilter = GPUImageSobelEdgeDetectionFilter()
            v_gpuimage_view.setFilter(curFilter)
        }
    }

    companion object {
        fun startActivity(context: Context) {
            try {
                val intent = Intent(context, DrawBitmapSobelActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            } catch (e: Exception) {
            }
        }
    }
}