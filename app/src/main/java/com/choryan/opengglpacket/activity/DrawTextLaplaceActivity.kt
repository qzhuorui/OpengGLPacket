package com.choryan.opengglpacket.activity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.choryan.opengglpacket.R
import com.choryan.opengglpacket.base.BaseActivity
import com.choryan.opengglpacket.filter.GPUImageSharpenFilter
import com.choryan.opengglpacket.filter.GPUImageSobelEdgeDetectionFilter
import kotlinx.android.synthetic.main.activity_draw_sobel_text.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author: ChoRyan Quan
 * @date: 2021/6/22
 */
class DrawTextLaplaceActivity : BaseActivity(R.layout.activity_draw_laplace_text) {


    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        initClick()
    }

    private fun initClick() {
        btn_render_bitmap.setOnClickListener {
            tv_display_text.text = et_edit_text.editableText.toString()
            tv_display_text.post {
                lifecycleScope.launch {
                    val renderBitmap = withContext(Dispatchers.IO) {
                        val bitmap = Bitmap.createBitmap(tv_display_text.width, tv_display_text.height, Bitmap.Config.ARGB_8888)
                        val canvas = Canvas(bitmap)
                        tv_display_text.draw(canvas)
                        bitmap
                    }
                    v_surface_view.setImage(renderBitmap)
                }
            }
        }
        btn_render_filter.setOnClickListener {
            val curFilter = GPUImageSharpenFilter()
            curFilter.setSharpness(4.0f)
            v_surface_view.setFilter(curFilter)
        }
    }

    companion object {
        fun startActivity(context: Context) {
            try {
                val intent = Intent(context, DrawTextLaplaceActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            } catch (e: Exception) {
            }
        }
    }
}