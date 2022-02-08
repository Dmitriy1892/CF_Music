package com.coldfier.cfmusic.ui.folders_fragment

import com.coldfier.cfmusic.data.database_room.model.SongFolder
import com.coldfier.cfmusic.ui.base.BaseViewModel
import com.coldfier.cfmusic.use_case.SongUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class FoldersViewModel @Inject constructor(
    private val songUseCase: SongUseCase
): BaseViewModel() {

    val songFoldersFlow: Flow<List<SongFolder>> = flow {
        songUseCase.getSongFoldersList().collect {
            emit(it)
        }
    }.flowOn(Dispatchers.IO)


    init {
        updateSongs()
    }

    fun updateSongs() {
        launchInIOCoroutine {
            songUseCase.getAllSongs()
        }
    }
}