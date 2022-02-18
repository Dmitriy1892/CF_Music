package com.coldfier.cfmusic.ui.albums_fragment

import com.coldfier.cfmusic.ui.base.BaseViewModel
import com.coldfier.cfmusic.use_case.SongUseCase
import com.coldfier.cfmusic.use_case.model.Album
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AlbumsViewModel @Inject constructor(
    private val songUseCase: SongUseCase
): BaseViewModel() {

    val albumListFlow: Flow<List<Album>> = flow {
        songUseCase.getAlbumList().collect {
            emit(it)
        }
    }.flowOn(Dispatchers.IO)
}