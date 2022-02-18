package com.coldfier.cfmusic.ui

import com.coldfier.cfmusic.ui.base.BaseViewModel
import com.coldfier.cfmusic.use_case.SongUseCase
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val songUseCase: SongUseCase
): BaseViewModel() {

    init {
        getAllSongs()
    }

    fun getAllSongs() {
        launchInIOCoroutine {
            songUseCase.getAllSongs()
        }
    }
}