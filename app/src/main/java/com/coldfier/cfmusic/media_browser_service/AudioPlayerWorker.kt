package com.coldfier.cfmusic.media_browser_service

import android.app.NotificationManager
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class AudioPlayerWorker(
    appContext: Context, workerParams: WorkerParameters
): CoroutineWorker(appContext, workerParams) {

    private val notificationManager =
        appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    override suspend fun doWork(): Result {
        TODO()
    }
}