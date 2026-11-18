package com.pinto.recordatorios

import android.app.Application
import com.pinto.recordatorios.data.AppContainer

class RecordatoriosApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}