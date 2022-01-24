package com.coldfier.cfmusic.di.module

import androidx.lifecycle.ViewModel
import com.coldfier.cfmusic.di.qualifier.ViewModelKey
import com.coldfier.cfmusic.ui.player_fragment.PlayerViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(PlayerViewModel::class)
    fun bindPlayerViewModel(playerViewModel: PlayerViewModel): ViewModel
}