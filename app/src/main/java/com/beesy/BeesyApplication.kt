package com.beesy

import android.app.Application
import com.beesy.data.AppContainer
import com.beesy.data.AppDataContainer

class BeesyApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}