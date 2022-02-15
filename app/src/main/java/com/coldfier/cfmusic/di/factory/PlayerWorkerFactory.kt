package com.coldfier.cfmusic.di.factory

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.coldfier.cfmusic.ui.player_fragment.PlayerWorker
import com.coldfier.cfmusic.use_case.SongUseCase
import com.google.android.exoplayer2.ExoPlayer
import javax.inject.Inject

class PlayerWorkerFactory @Inject constructor(
    private val songUseCase: SongUseCase,
    private val exoPlayer: ExoPlayer
): WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker {
        return PlayerWorker(appContext, workerParameters, songUseCase, exoPlayer)
    }
}