package com.choryan.opengglpacket.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.choryan.opengglpacket.R
import com.choryan.opengglpacket.base.BaseActivity
import com.choryan.opengglpacket.render.PointLineTriangleRender
import kotlinx.android.synthetic.main.activity_pure_background.*

/**
 * @author: ChoRyan Quan
 * @date: 2021/6/22
 */
class PointLineTriangleActivity : BaseActivity(R.layout.activity_point_line_triangle) {

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        initView()
    }

    private fun initView() {
        v_surface_view.setEGLContextClientVersion(3)
        val render = PointLineTriangleRender()
        v_surface_view.setRenderer(render)
    }

    companion object {
        fun startActivity(context: Context) {
            try {
                val intent = Intent(context, PointLineTriangleActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            } catch (e: Exception) {
            }
        }
    }
}