package com.coldfier.cfmusic.ui.player_fragment

import com.coldfier.cfmusic.data.database_room.model.RoomSong
import com.coldfier.cfmusic.ui.base.BaseViewModel
import com.coldfier.cfmusic.use_case.model.Song
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class PlayerViewModel @Inject constructor(

): BaseViewModel() {

    private val _songListStateFlow = MutableStateFlow(listOf<RoomSong>())
    val roomSongListStateFlow: StateFlow<List<RoomSong>>
        get() = _songListStateFlow.asStateFlow()

    private val _currentSongStateFlow = MutableStateFlow(Song())
    val currentSongStateFlow: StateFlow<Song>
        get() = _currentSongStateFlow.asStateFlow()

    var inc = 0
    fun getHello(): String = "Hello ${inc++}"

    fun getSongList() {
        launchInIOCoroutine {
//            _songListStateFlow.value = songRepository.getAllSongs()
        }
    }

    fun setCurrentSong(song: Song) {
        _currentSongStateFlow.value = song
    }
}