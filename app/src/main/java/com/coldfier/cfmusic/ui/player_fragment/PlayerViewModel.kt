package com.coldfier.cfmusic.ui.player_fragment

import android.content.Context
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.coldfier.cfmusic.di.factory.PlayerWorkerFactory
import com.coldfier.cfmusic.ui.base.BaseViewModel
import com.coldfier.cfmusic.use_case.SongUseCase
import com.coldfier.cfmusic.use_case.model.Song
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class PlayerViewModel @Inject constructor(
    appContext: Context,
    private val songUseCase: SongUseCase
): BaseViewModel() {

    private val _songListStateFlow = MutableStateFlow(listOf<Song>())
    val songListStateFlow: StateFlow<List<Song>>
        get() = _songListStateFlow.asStateFlow()

    private val _currentSongStateFlow = MutableStateFlow(Song())
    val currentSongStateFlow: StateFlow<Song>
        get() = _currentSongStateFlow.asStateFlow()

    private var workManager = WorkManager.getInstance(appContext)

    init {
        prepareWorkerForPlayer()
    }

    fun getSongList(folderName: String) {
        launchInIOCoroutine {
            songUseCase.getSongsFromFolder(folderName).collect {
                _songListStateFlow.value = it
            }
        }
    }

    fun setCurrentSong(song: Song) {
        _currentSongStateFlow.value = song
        song.folderName?.let {
            getSongList(folderName = it)
        }
    }

    private fun prepareWorkerForPlayer() {
        val workRequestBuilder = OneTimeWorkRequestBuilder<PlayerWorker>()
        val workRequest = workRequestBuilder.build()
        workManager.enqueue(workRequest)
    }

    companion object {
        const val KEY_SONG_WORK_DATA = "com.coldfier.cfmusic.ui.key_song_work_data"
    }
}