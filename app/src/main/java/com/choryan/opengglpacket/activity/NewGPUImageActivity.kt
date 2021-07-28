package com.choryan.opengglpacket.activity

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Toast
import com.choryan.opengglpacket.R
import com.choryan.opengglpacket.base.BaseActivity
import com.choryan.opengglpacket.filter.GPUImageGrayscaleFilter
import com.choryan.opengglpacket.gpuImage.GPUImage
import com.choryan.opengglpacket.gpuImage.GPUImageFilter
import com.choryan.opengglpacket.view.SelectFilterPop
import kotlinx.android.synthetic.main.activity_draw_bitmap_filter.*
import kotlinx.android.synthetic.main.activity_draw_bitmap_sobel.v_gpuimage_view

/**
 * @author: ChoRyan Quan
 * @date: 2021/6/22
 */
class NewGPUImageActivity : BaseActivity(R.layout.activity_draw_bitmap_filter), SelectFilterPop.SelectFilterCallBack {

    private val selFilterPop by lazy {
        val pop = SelectFilterPop(this)
        pop
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sourceBitmap = BitmapFactory.decodeResource(resources, R.mipmap.teststicker)
        val dstBitmap = BitmapFactory.decodeResource(resources, R.mipmap.test_bitmap)
        v_gpuimage_view.setImageInput(GPUImage(dstBitmap))
        btn_select_filter.setOnClickListener {
            selFilterPop.show(it)
        }
        btn_remove_filter.setOnClickListener {
            Toast.makeText(this, "remove filter", Toast.LENGTH_SHORT).show()
        }
    }

    override fun provideSelFilter(gpuImageFilter: GPUImageFilter) {
        v_gpuimage_view.addFilter(gpuImageFilter)
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