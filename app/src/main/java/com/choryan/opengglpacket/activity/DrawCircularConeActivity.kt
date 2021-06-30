package com.choryan.opengglpacket.activity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.choryan.opengglpacket.R
import com.choryan.opengglpacket.base.BaseActivity
import com.choryan.opengglpacket.render.DrawCircleRender
import com.choryan.opengglpacket.render.DrawCircularConeRender
import com.choryan.opengglpacket.render.DrawTextRender
import kotlinx.android.synthetic.main.activity_draw_text.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author: ChoRyan Quan
 * @date: 2021/6/22
 */
class DrawCircularConeActivity : BaseActivity(R.layout.activity_draw_circular_cone) {

    private val drawCircularConeRender by lazy {
        DrawCircularConeRender()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        initRender()
    }

    private fun initRender() {
        v_surface_view.setEGLContextClientVersion(3)
        v_surface_view.setRenderer(drawCircularConeRender)
    }

    companion object {
        fun startActivity(context: Context) {
            try {
                val intent = Intent(context, DrawCircularConeActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            } catch (e: Exception) {
            }
        }
    }
}