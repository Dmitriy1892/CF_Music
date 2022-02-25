package com.coldfier.cfmusic.media_browser_service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.v4.media.session.MediaSessionCompat
import android.view.KeyEvent
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.coldfier.cfmusic.R
import com.coldfier.cfmusic.use_case.SongUseCase
import com.coldfier.cfmusic.use_case.model.Song
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject

class PlayerMediaSessionCallback @Inject constructor(
    private val player: ExoPlayer,
    private val songUseCase: SongUseCase,
    private val appContext: Context
): MediaSessionCompat.Callback() {

    private var currentSong: Song? = null
    private var nextSong: AtomicReference<Song?> = AtomicReference(null)
    private var previousSong: AtomicReference<Song?> = AtomicReference(null)

    val notificationManager =
        appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    val notificationLayout =
        RemoteViews(appContext.packageName, R.layout.notification_player)
    
    
    override fun onPlay() {
        if (player.isPlaying) return

        player.play()
        notificationLayout
            .setImageViewResource(R.id.btn_play_song_collapsed, R.drawable.ic_pause)
        notificationManager.notify(PLAYER_NOTIFICATION_ID, createNotification())
    }

    override fun onPause() {
        if (!player.isPlaying) return
        player.pause()
        notificationLayout
            .setImageViewResource(R.id.btn_play_song_collapsed, R.drawable.ic_play)
        notificationManager.notify(PLAYER_NOTIFICATION_ID, createNotification())
    }

    override fun onStop() {
        if (!player.isPlaying) return
        player.stop()
        notificationLayout
            .setImageViewResource(R.id.btn_play_song_collapsed, R.drawable.ic_play)
        notificationManager.notify(PLAYER_NOTIFICATION_ID, createNotification())
    }

    override fun onSkipToPrevious() {
        previousSong.get()?.let { song ->
            currentSong = song
            renderSongInfo(song)
            song.fullPath?.let { path -> preparePlayer(path) }
            nextSong.set(null)
            previousSong.set(null)
            getPreviousAndNextSong(song)
        }
    }

    override fun onSkipToNext() {
        nextSong.get()?.let { song ->
            currentSong = song
            renderSongInfo(song)
            song.fullPath?.let { path -> preparePlayer(path) }
            nextSong.set(null)
            previousSong.set(null)
            getPreviousAndNextSong(song)
        }
    }

    fun createNotification(): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                MUSIC_CHANNEL_ID, MUSIC_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH
            )
            channel.setSound(null, null)
            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            notificationManager.createNotificationChannel(channel)
        }

        val previousSongIntent = Intent(ACTION_REQUEST_PREVIOUS_SONG)
        previousSongIntent.putExtra(
            Intent.EXTRA_KEY_EVENT,
            KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_PREVIOUS)
        )
        val previousSongPendingIntent = PendingIntent.getBroadcast(
            appContext, 0, previousSongIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        notificationLayout
            .setOnClickPendingIntent(R.id.btn_previous_song_collapsed, previousSongPendingIntent)

        val playPauseIntent = Intent(ACTION_REQUEST_PLAY_PAUSE_SONG)
        playPauseIntent.putExtra(
            Intent.EXTRA_KEY_EVENT,
            KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE)
        )
        val playPausePendingIntent = PendingIntent.getBroadcast(
            appContext, 0, playPauseIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        notificationLayout
            .setOnClickPendingIntent(R.id.btn_play_song_collapsed, playPausePendingIntent)

        val nextSongIntent = Intent(ACTION_REQUEST_NEXT_SONG)
        nextSongIntent.putExtra(
            Intent.EXTRA_KEY_EVENT,
            KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_NEXT)
        )
        val nextSongPendingIntent = PendingIntent.getBroadcast(
            appContext, 0, nextSongIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        notificationLayout
            .setOnClickPendingIntent(R.id.btn_next_song_collapsed, nextSongPendingIntent)

        val notification = NotificationCompat.Builder(appContext,
            MUSIC_CHANNEL_ID
        )
            .setSmallIcon(R.mipmap.ic_launcher)
            .setCustomContentView(notificationLayout)
            .setSound(null)

        return notification.build()
    }

    private fun renderSongInfo(song: Song) {
        notificationLayout.setTextViewText(R.id.tv_song_name_collapsed, song.songName ?: "")
        notificationLayout.setTextViewText(R.id.tv_song_artist_collapsed, song.artist ?: "")
        notificationManager.notify(PLAYER_NOTIFICATION_ID, createNotification())
    }

    private fun preparePlayer(filePath: String) {
        val uri = Uri.parse(filePath)
        val mediaItem = MediaItem.fromUri(uri)
        player.setMediaItem(mediaItem)
        player.prepare()

        player.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_ENDED) {
                    val nextSongIntent = Intent(Intent.ACTION_MEDIA_BUTTON)
                    nextSongIntent.putExtra(
                        Intent.EXTRA_KEY_EVENT,
                        KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_NEXT)
                    )
                    appContext.sendBroadcast(nextSongIntent)
                }
            }
        })
    }

    private fun getPreviousAndNextSong(currentSong: Song) {
        val scope = CoroutineScope(Dispatchers.IO + Job())
        scope.launch {
            currentSong.folderName?.let { folderName ->
                val songList = songUseCase.getSongsFromFolder(folderName).first()
                val index = kotlin.run {
                    songList.forEachIndexed { index, song ->
                        if (song.songId == currentSong.songId) {
                            return@run index
                        }
                    }
                    scope.cancel()
                    return@launch
                }

                if (index + 1 <= songList.lastIndex) {
                    nextSong.set(songList[index + 1])
                } else {
                    val newNextSong = songUseCase.getSongsFromNextFolder(folderName)
                        .first().getOrElse(0) { null }
                    nextSong.set(newNextSong)
                }

                if (index - 1 >= 0) {
                    previousSong.set(songList[index - 1])
                } else {
                    val newSongList = songUseCase.getSongsFromPreviousFolder(folderName).first()
                    val newPreviousSong = newSongList.getOrElse(newSongList.lastIndex) { null }
                    previousSong.set(newPreviousSong)
                }
            }
            scope.cancel()
        }
    }

    companion object {
        const val PLAYER_NOTIFICATION_ID = 2048

        const val MUSIC_CHANNEL_ID = "com.coldfier.cfmusic.music_channel_id"
        const val MUSIC_CHANNEL_NAME = "com.coldfier.cfmusic.music_channel_name"

        const val ACTION_REQUEST_PREVIOUS_SONG = "com.coldfier.cfmusic.request_previous_song"
        const val ACTION_REQUEST_NEXT_SONG = "com.coldfier.cfmusic.request_next_song"
        const val ACTION_REQUEST_PLAY_PAUSE_SONG = "com.coldfier.cfmusic.request_play_pause_song"
    }
}