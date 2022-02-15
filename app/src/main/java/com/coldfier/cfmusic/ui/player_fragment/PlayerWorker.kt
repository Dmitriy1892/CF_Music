package com.coldfier.cfmusic.ui.player_fragment

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.coldfier.cfmusic.R
import com.coldfier.cfmusic.ui.picked_folder_fragment.ACTION_SONG_PICKED
import com.coldfier.cfmusic.ui.picked_folder_fragment.SONG_KEY
import com.coldfier.cfmusic.use_case.SongUseCase
import com.coldfier.cfmusic.use_case.model.Song
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem

class PlayerWorker(
    appContext: Context,
    workerParams: WorkerParameters,
    private val songUseCase: SongUseCase,
    private val player: ExoPlayer
): CoroutineWorker(appContext, workerParams) {

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when(intent?.action) {
                ACTION_REQUEST_PREVIOUS_SONG -> {

                }

                ACTION_REQUEST_PLAY_PAUSE_SONG -> {
                    if (player.isPlaying) player.pause() else player.play()

                }

                ACTION_REQUEST_NEXT_SONG -> {

                }

                ACTION_SONG_PICKED -> {

                }
            }
        }
    }

    private val songReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == ACTION_SONG_PICKED) {
                try {
                    val song = intent.getParcelableExtra<Song>(SONG_KEY)
                    song?.let {
                        renderSongInfo(it)
                        it.fullPath?.let { path -> preparePlayer(path) }
                    }
                } catch (e: Exception) {
                    e.message
                }
            }
        }
    }

    private val notificationManager =
        appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private val notificationLayout =
        RemoteViews(applicationContext.packageName, R.layout.notification_player)

    override suspend fun doWork(): Result {
        val intentFilter = IntentFilter()
        intentFilter.apply {
            addAction(ACTION_REQUEST_PREVIOUS_SONG)
            addAction(ACTION_REQUEST_PLAY_PAUSE_SONG)
            addAction(ACTION_REQUEST_NEXT_SONG)
            addAction(ACTION_SONG_PICKED)
        }
        applicationContext.registerReceiver(broadcastReceiver, intentFilter)

        val songFilter = IntentFilter(ACTION_SONG_PICKED)
        LocalBroadcastManager
            .getInstance(applicationContext).registerReceiver(songReceiver, songFilter)
        return try {
            notificationManager.notify(1, createNotification())

            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    private fun createNotification(): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                MUSIC_CHANNEL_ID, MUSIC_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH
            )
            channel.setSound(null, null)
            notificationManager.createNotificationChannel(channel)
        }

        val previousSongIntent = Intent(ACTION_REQUEST_PREVIOUS_SONG)
        val previousSongPendingIntent = PendingIntent.getBroadcast(
            applicationContext, 0, previousSongIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        notificationLayout
            .setOnClickPendingIntent(R.id.btn_previous_song_collapsed, previousSongPendingIntent)

        val playPauseIntent = Intent(ACTION_REQUEST_PLAY_PAUSE_SONG)
        val playPausePendingIntent = PendingIntent.getBroadcast(
            applicationContext, 0, playPauseIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        notificationLayout
            .setOnClickPendingIntent(R.id.btn_play_song_collapsed, playPausePendingIntent)

        val nextSongIntent = Intent(ACTION_REQUEST_NEXT_SONG)
        val nextSongPendingIntent = PendingIntent.getBroadcast(
            applicationContext, 0, nextSongIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        notificationLayout
            .setOnClickPendingIntent(R.id.btn_next_song_collapsed, nextSongPendingIntent)

        val notification = NotificationCompat.Builder(applicationContext, MUSIC_CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setCustomContentView(notificationLayout)
            .setSound(null)
            .build()

        return notification
    }

    private fun renderSongInfo(song: Song) {
        notificationLayout.setTextViewText(R.id.tv_song_name_collapsed, song.songName ?: "")
        notificationLayout.setTextViewText(R.id.tv_song_artist_collapsed, song.artist ?: "")
        notificationManager.notify(1, createNotification())
    }

    private fun preparePlayer(filePath: String) {
        val uri = Uri.parse(filePath)
        val mediaItem = MediaItem.fromUri(uri)
        player.setMediaItem(mediaItem)
        player.prepare()
    }

    companion object {
        const val MUSIC_CHANNEL_ID = "com.coldfier.cfmusic.music_channel_id"
        const val MUSIC_CHANNEL_NAME = "com.coldfier.cfmusic.music_channel_name"

        const val ACTION_REQUEST_PREVIOUS_SONG = "com.coldfier.cfmusic.request_previous_song"
        const val ACTION_REQUEST_NEXT_SONG = "com.coldfier.cfmusic.request_next_song"
        const val ACTION_REQUEST_PLAY_PAUSE_SONG = "com.coldfier.cfmusic.request_play_pause_song"
    }
}