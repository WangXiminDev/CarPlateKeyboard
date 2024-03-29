package com.example.carplatekeyboard

import android.app.Application
import android.content.Context

/**
 * @Description MyApplication
 * @Author wangximin
 */
class MyApplication : Application() {

    companion object {

        private lateinit var application: MyApplication

        fun getApplication() = application

    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        application = this
    }

}