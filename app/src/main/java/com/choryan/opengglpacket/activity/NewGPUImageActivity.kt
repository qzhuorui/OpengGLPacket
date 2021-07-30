package com.choryan.opengglpacket.activity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.os.Bundle
import android.widget.SeekBar
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.choryan.opengglpacket.R
import com.choryan.opengglpacket.base.BaseActivity
import com.choryan.opengglpacket.filter.GPUImageGaussianBlurFilter
import com.choryan.opengglpacket.gpuImage.GPUImage
import com.choryan.opengglpacket.gpuImage.GPUImageFilter
import com.choryan.opengglpacket.view.SelectFilterPop
import kotlinx.android.synthetic.main.activity_draw_bitmap_filter.*
import kotlinx.android.synthetic.main.activity_draw_bitmap_sobel.v_gpuimage_view
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author: ChoRyan Quan
 * @date: 2021/6/22
 */
class NewGPUImageActivity : BaseActivity(R.layout.activity_draw_bitmap_filter), SelectFilterPop.SelectFilterCallBack,
    SeekBar.OnSeekBarChangeListener {

    private var curFilter: GPUImageFilter? = null

    private val selFilterPop by lazy {
        val pop = SelectFilterPop(this)
        pop
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sourceBitmap = BitmapFactory.decodeResource(resources, R.mipmap.teststicker)
        val dstBitmap = BitmapFactory.decodeResource(resources, R.mipmap.test_bitmap)
        v_gpuimage_view.setImageInput(GPUImage(dstBitmap))

        tv_edit_text_ok.setOnClickListener {
            val inputText = et_edit_text.editableText.toString()
            if (inputText.isNotEmpty()) {
                tv_display_text.text = inputText
                tv_display_text.post {
                    lifecycleScope.launch {
                        val textBitmap = withContext(Dispatchers.IO) {
                            val bitmap =
                                Bitmap.createBitmap(tv_display_text.width, tv_display_text.height, Bitmap.Config.ARGB_8888)
                            val canvas = Canvas(bitmap)
                            tv_display_text.draw(canvas)
                            bitmap
                        }
                        v_gpuimage_view.changeImageInput(textBitmap)
                    }
                }
            }
        }

        btn_select_filter.setOnClickListener {
            selFilterPop.show(it)
        }
        btn_remove_filter.setOnClickListener {
            v_gpuimage_view.removeFilter()
        }
        sb_seek_bar.setOnSeekBarChangeListener(this)
    }

    override fun provideSelFilter(gpuImageFilter: GPUImageFilter) {
        curFilter = gpuImageFilter
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

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        curFilter?.let {
            if (it is GPUImageGaussianBlurFilter) {
                Toast.makeText(this, "curProgress: $progress", Toast.LENGTH_SHORT).show()
                it.setBlurSize(progress.toFloat())
                v_gpuimage_view.forceRequestRender()
            }
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

    override fun onStopTrackingTouch(seekBar: SeekBar?) {}
}