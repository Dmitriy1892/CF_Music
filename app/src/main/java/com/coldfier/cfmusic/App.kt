package com.coldfier.cfmusic

import android.app.Application
import com.coldfier.cfmusic.di.AppComponent
import com.coldfier.cfmusic.di.DaggerAppComponent
import timber.log.Timber

class App: Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().context(this).build()

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }
}