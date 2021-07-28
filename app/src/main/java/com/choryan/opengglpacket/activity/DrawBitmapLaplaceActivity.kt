package com.choryan.opengglpacket.activity

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import com.choryan.opengglpacket.R
import com.choryan.opengglpacket.base.BaseActivity
import com.choryan.opengglpacket.filter.GPUImageSharpenFilter
import com.choryan.opengglpacket.filter.GPUImageSobelEdgeDetectionFilter
import kotlinx.android.synthetic.main.activity_draw_bitmap_sobel.*

/**
 * @author: ChoRyan Quan
 * @date: 2021/6/22
 */
class DrawBitmapLaplaceActivity : BaseActivity(R.layout.activity_draw_bitmap_laplace) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /**
         * 对于有alpha通道的bitmap，Android系统解码API会自动执行预乘操作
         * 即bitmap每个像素的RGB值在解码时会自动乘以当前像素的alpha值
         * 意味着bitmap中存储的RGB值和原生图片的RGB值是不同的
         *
         * ---
         * 1.bitmap解码不做预乘
         * 2.OpenGL blend不乘alpha
         */
//        v_gpuimage_view.setImage(BitmapFactory.decodeResource(resources, R.mipmap.test_bitmap))
        btn_render.setOnClickListener {
//            val curFilter = GPUImageSharpenFilter()
//            curFilter.setSharpness(4.0f)
//            v_gpuimage_view.setFilter(curFilter)
        }
        btn_remove_render.setOnClickListener {
//            v_gpuimage_view.removeAllFilter()
        }
    }

    companion object {
        fun startActivity(context: Context) {
            try {
                val intent = Intent(context, DrawBitmapLaplaceActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            } catch (e: Exception) {
            }
        }
    }
}