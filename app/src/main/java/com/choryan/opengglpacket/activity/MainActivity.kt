package com.choryan.opengglpacket.activity

import android.os.Bundle
import com.choryan.opengglpacket.R
import com.choryan.opengglpacket.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initClick()
    }

    private fun initClick() {
        tv_pure_activity.setOnClickListener {
            PureBackgroundActivity.startActivity(this)
        }
        tv_point_line_triangle_activity.setOnClickListener {
            PointLineTriangleActivity.startActivity(this)
        }
        tv_draw_rectangle_activity.setOnClickListener {
            DrawRectangleActivity.startActivity(this)
        }
        tv_draw_circle_activity.setOnClickListener {
            DrawCircleActivity.startActivity(this)
        }
        tv_draw_text_activity.setOnClickListener {
            DrawTextActivity.startActivity(this)
        }
    }
}