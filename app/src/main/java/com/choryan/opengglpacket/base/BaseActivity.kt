package com.choryan.opengglpacket.base

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity

/**
 * @author: ChoRyan Quan
 * @date: 2021/6/22
 */
open class BaseActivity(@LayoutRes layoutRes: Int) : AppCompatActivity(layoutRes) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}