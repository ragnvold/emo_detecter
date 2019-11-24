package com.westwin.khakaton

import android.app.Application
import android.content.Context

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object
    {
        lateinit var instance: Application

        fun getContext(): Context {
            return instance.applicationContext
        }
    }
}