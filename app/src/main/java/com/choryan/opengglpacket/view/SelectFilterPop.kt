package com.choryan.opengglpacket.view

import android.content.Context
import android.view.*
import android.widget.PopupWindow
import android.widget.TextView
import com.choryan.opengglpacket.R
import com.choryan.opengglpacket.filter.GPUImageGrayscaleFilter
import com.choryan.opengglpacket.filter.GPUImageVignetteFilter
import com.choryan.opengglpacket.gpuImage.GPUImageFilter

/**
 * @author: ChoRyan Quan
 * @date: 2021/7/28
 */
class SelectFilterPop(private val context: Context) : PopupWindow(context), View.OnClickListener, View.OnTouchListener {

    init {
        isClippingEnabled = false
        isFocusable = true
        isOutsideTouchable = true
        width = WindowManager.LayoutParams.WRAP_CONTENT
        height = WindowManager.LayoutParams.WRAP_CONTENT
        setBackgroundDrawable(null)
        contentView = LayoutInflater.from(context).inflate(R.layout.pop_select_filter, null)
        contentView.setOnTouchListener(this)
        contentView.findViewById<TextView>(R.id.tv_gray).setOnClickListener(this)
        contentView.findViewById<TextView>(R.id.tv_vignette).setOnClickListener(this)
    }

    fun show(parent: View?) {
        parent?.let {
            showAtLocation(it, Gravity.CENTER, 0, 0)
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_gray -> {
                getCurSelectFilter(GPUImageGrayscaleFilter())
            }
            R.id.tv_vignette -> {
                getCurSelectFilter(GPUImageVignetteFilter())
            }
        }
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        dismiss()
        return false
    }

    private fun getCurSelectFilter(gpuImageFilter: GPUImageFilter) {
        if (context is SelectFilterCallBack) {
            context.provideSelFilter(gpuImageFilter)
        }
    }

    interface SelectFilterCallBack {
        fun provideSelFilter(gpuImageFilter: GPUImageFilter)
    }


}