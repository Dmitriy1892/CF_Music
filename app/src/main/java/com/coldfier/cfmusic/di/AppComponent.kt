package com.coldfier.cfmusic.di

import android.content.Context
import androidx.databinding.ViewDataBinding
import com.coldfier.cfmusic.App
import com.coldfier.cfmusic.di.module.DatabaseModule
import com.coldfier.cfmusic.di.module.PlayerModule
import com.coldfier.cfmusic.di.module.ViewModelsModule
import com.coldfier.cfmusic.media_browser_service.PlayerMediaBrowserService
import com.coldfier.cfmusic.ui.base.BaseActivity
import com.coldfier.cfmusic.ui.base.BaseFragment
import com.coldfier.cfmusic.ui.base.BaseViewModel
import com.coldfier.cfmusic.ui.player_fragment.PlayerFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [
    ViewModelsModule::class,
    DatabaseModule::class,
    PlayerModule::class
])
interface AppComponent {

    fun inject(fragment: BaseFragment<BaseViewModel, ViewDataBinding>)

    fun injectBaseActivity(activity: BaseActivity<BaseViewModel, ViewDataBinding>)

    fun injectApp(application: App)

    fun injectPlayer(fragment: PlayerFragment)

    fun injectPlayerToService(playerMediaBrowserService: PlayerMediaBrowserService)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): Builder

        fun build(): AppComponent
    }
}