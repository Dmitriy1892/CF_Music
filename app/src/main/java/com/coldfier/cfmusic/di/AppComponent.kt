package com.coldfier.cfmusic.di

import android.content.Context
import androidx.databinding.ViewDataBinding
import com.coldfier.cfmusic.di.module.DatabaseModule
import com.coldfier.cfmusic.di.module.ViewModelsModule
import com.coldfier.cfmusic.ui.base.BaseFragment
import com.coldfier.cfmusic.ui.base.BaseViewModel
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [
    ViewModelsModule::class,
    DatabaseModule::class
])
interface AppComponent {

    fun inject(fragment: BaseFragment<BaseViewModel, ViewDataBinding>)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): Builder

        fun build(): AppComponent
    }
}