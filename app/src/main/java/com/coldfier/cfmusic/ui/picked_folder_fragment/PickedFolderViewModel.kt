package com.coldfier.cfmusic.ui.picked_folder_fragment

import com.coldfier.cfmusic.data.database_room.model.SongFolder
import com.coldfier.cfmusic.ui.base.BaseViewModel
import com.coldfier.cfmusic.use_case.SongUseCase
import com.coldfier.cfmusic.use_case.model.Song
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class PickedFolderViewModel @Inject constructor(
    private val songUseCase: SongUseCase
): BaseViewModel() {

    private val _songListStateFlow = MutableStateFlow(listOf<Song>())
    val songListStateFlow: StateFlow<List<Song>>
        get() = _songListStateFlow.asStateFlow()

    fun getSongListFromFolder(folderName: String) {
        launchInIOCoroutine {
            songUseCase.getSongsFromFolder(folderName).collect {
                _songListStateFlow.value = it
            }
        }
    }
}