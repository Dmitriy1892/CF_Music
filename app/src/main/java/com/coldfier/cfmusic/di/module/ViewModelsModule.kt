package com.coldfier.cfmusic.di.module

import androidx.lifecycle.ViewModel
import com.coldfier.cfmusic.di.qualifier.ViewModelKey
import com.coldfier.cfmusic.ui.MainViewModel
import com.coldfier.cfmusic.ui.albums_fragment.AlbumsViewModel
import com.coldfier.cfmusic.ui.folders_fragment.FoldersViewModel
import com.coldfier.cfmusic.ui.picked_folder_fragment.PickedFolderViewModel
import com.coldfier.cfmusic.ui.player_fragment.PlayerViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    fun bindMainViewModel(mainViewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PlayerViewModel::class)
    fun bindPlayerViewModel(playerViewModel: PlayerViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FoldersViewModel::class)
    fun bindFoldersViewModel(foldersViewModel: FoldersViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PickedFolderViewModel::class)
    fun bindPickedFolderViewModel(pickedFolderViewModel: PickedFolderViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AlbumsViewModel::class)
    fun bindAlbumsViewModel(albumsViewModel: AlbumsViewModel): ViewModel
}