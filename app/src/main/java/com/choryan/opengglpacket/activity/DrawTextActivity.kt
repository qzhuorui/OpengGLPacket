package com.choryan.opengglpacket.activity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.choryan.opengglpacket.R
import com.choryan.opengglpacket.base.BaseActivity
import com.choryan.opengglpacket.render.DrawTextRender
import kotlinx.android.synthetic.main.activity_draw_text.*
import kotlinx.android.synthetic.main.activity_pure_background.v_surface_view
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author: ChoRyan Quan
 * @date: 2021/6/22
 */
class DrawTextActivity : BaseActivity(R.layout.activity_draw_text) {

    private val drawTextRender by lazy {
        DrawTextRender()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        initRender()
        initClick()
    }

    private fun initRender() {
        v_surface_view.setEGLContextClientVersion(3)
        v_surface_view.setRenderer(drawTextRender)
    }

    private fun initClick() {
        btn_done.setOnClickListener {
            lifecycleScope.launch {
                val renderBitmap = withContext(Dispatchers.IO) {
                    val bitmap = Bitmap.createBitmap(et_edit_text.width, et_edit_text.height, Bitmap.Config.RGB_565)
                    val canvas = Canvas(bitmap)
                    et_edit_text.draw(canvas)
                    bitmap
                }
                drawTextRender.setRenderBitmap(renderBitmap) {
                    v_surface_view.requestRender()
                }
            }
        }
    }

    companion object {
        fun startActivity(context: Context) {
            try {
                val intent = Intent(context, DrawTextActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            } catch (e: Exception) {
            }
        }
    }
}