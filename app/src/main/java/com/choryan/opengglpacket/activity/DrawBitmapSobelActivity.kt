package com.choryan.opengglpacket.activity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.choryan.opengglpacket.R
import com.choryan.opengglpacket.base.BaseActivity
import com.choryan.opengglpacket.render.DrawBitmapRender
import com.choryan.opengglpacket.render.DrawBitmapSobelRender
import com.choryan.opengglpacket.render.DrawTextRender
import kotlinx.android.synthetic.main.activity_draw_text.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author: ChoRyan Quan
 * @date: 2021/6/22
 */
class DrawBitmapSobelActivity : BaseActivity(R.layout.activity_draw_bitmap_sobel) {

    private val drawBitmapSobelRender by lazy {
        DrawBitmapSobelRender(this)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        initRender()
    }

    private fun initRender() {
        v_surface_view.setEGLContextClientVersion(3)
        v_surface_view.setRenderer(drawBitmapSobelRender)
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