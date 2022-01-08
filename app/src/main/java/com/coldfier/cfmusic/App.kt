package com.coldfier.cfmusic

import android.app.Application
import com.coldfier.cfmusic.di.DaggerAppComponent
import timber.log.Timber

class App: Application() {

    val appComponent = DaggerAppComponent.create()

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }
}