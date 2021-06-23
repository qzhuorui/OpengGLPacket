package com.choryan.opengglpacket.base

import android.app.Application

/**
 * @author: ChoRyan Quan
 * @date: 2021/6/23
 */
open class BaseApplication : Application() {

    companion object {
        lateinit var instance: BaseApplication
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}