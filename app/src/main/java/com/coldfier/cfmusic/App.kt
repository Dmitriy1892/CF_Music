package com.coldfier.cfmusic

import android.app.Application
import androidx.work.Configuration
import com.coldfier.cfmusic.di.AppComponent
import com.coldfier.cfmusic.di.DaggerAppComponent
import com.coldfier.cfmusic.di.factory.PlayerWorkerFactory
import timber.log.Timber
import javax.inject.Inject

class App: Application(), Configuration.Provider {

    lateinit var appComponent: AppComponent

    @Inject
    lateinit var workerFactory: PlayerWorkerFactory

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().context(this).build()
        appComponent.injectApp(this)
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder().setWorkerFactory(workerFactory).build()
    }
}