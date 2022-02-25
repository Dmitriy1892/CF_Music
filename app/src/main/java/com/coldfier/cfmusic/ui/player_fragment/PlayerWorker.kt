package com.coldfier.cfmusic.ui.player_fragment

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.*
import android.media.AudioManager
import android.media.session.MediaSession
import android.net.Uri
import android.os.Build
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.view.KeyEvent
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.coldfier.cfmusic.R
import com.coldfier.cfmusic.media_browser_service.KeyEventBroadcastReceiver
import com.coldfier.cfmusic.ui.picked_folder_fragment.ACTION_SONG_PICKED
import com.coldfier.cfmusic.ui.picked_folder_fragment.SONG_KEY
import com.coldfier.cfmusic.use_case.SongUseCase
import com.coldfier.cfmusic.use_case.model.Song
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.concurrent.atomic.AtomicReference

class PlayerWorker(
    appContext: Context,
    workerParams: WorkerParameters,
    private val songUseCase: SongUseCase,
    private val player: ExoPlayer
): CoroutineWorker(appContext, workerParams) {

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when(intent?.action) {
                Intent.ACTION_MEDIA_BUTTON, ACTION_REQUEST_PREVIOUS_SONG,
                ACTION_REQUEST_PLAY_PAUSE_SONG, ACTION_REQUEST_NEXT_SONG -> {
                    val keyEvent: KeyEvent? = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT)

                    when(keyEvent?.keyCode) {
                        KeyEvent.KEYCODE_MEDIA_PREVIOUS -> {
                            previousSong.get()?.let { song ->
                                currentSong = song
                                renderSongInfo(song)
                                song.fullPath?.let { path -> preparePlayer(path) }
                                nextSong.set(null)
                                previousSong.set(null)

                                val intentUpdateSongInfo = Intent(ACTION_UPDATE_SONG_INFO)
                                intentUpdateSongInfo.putExtra(SONG_KEY, song)
                                LocalBroadcastManager.getInstance(applicationContext)
                                    .sendBroadcast(intentUpdateSongInfo)

                                getPreviousAndNextSong(song)
                            }
                        }

                        KeyEvent.KEYCODE_MEDIA_PAUSE -> {
                            if (player.isPlaying) {
                                player.pause()
                                notificationLayout
                                    .setImageViewResource(R.id.btn_play_song_collapsed, R.drawable.ic_play)
                                notificationManager.notify(PLAYER_NOTIFICATION_ID, createNotification())

                                val playPauseIntent = Intent(ACTION_CHANGE_PLAY_BUTTON_ICON)
                                playPauseIntent.putExtra(EXTRA_IS_PLAYING, player.isPlaying)
                                LocalBroadcastManager.getInstance(applicationContext)
                                    .sendBroadcast(playPauseIntent)
                            }
                        }

                        KeyEvent.KEYCODE_MEDIA_PLAY -> {
                            if (!player.isPlaying) {
                                player.play()
                                notificationLayout
                                    .setImageViewResource(R.id.btn_play_song_collapsed, R.drawable.ic_pause)
                                notificationManager.notify(PLAYER_NOTIFICATION_ID, createNotification())

                                val playPauseIntent = Intent(ACTION_CHANGE_PLAY_BUTTON_ICON)
                                playPauseIntent.putExtra(EXTRA_IS_PLAYING, player.isPlaying)
                                LocalBroadcastManager.getInstance(applicationContext)
                                    .sendBroadcast(playPauseIntent)
                            }
                        }

                        KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE -> {
                            if (player.isPlaying) {
                                player.pause()
                                notificationLayout
                                    .setImageViewResource(R.id.btn_play_song_collapsed, R.drawable.ic_play)
                                notificationManager.notify(PLAYER_NOTIFICATION_ID, createNotification())
                            } else {
                                player.play()
                                notificationLayout
                                    .setImageViewResource(R.id.btn_play_song_collapsed, R.drawable.ic_pause)
                                notificationManager.notify(PLAYER_NOTIFICATION_ID, createNotification())
                            }

                            val playPauseIntent = Intent(ACTION_CHANGE_PLAY_BUTTON_ICON)
                            playPauseIntent.putExtra(EXTRA_IS_PLAYING, player.isPlaying)
                            LocalBroadcastManager.getInstance(applicationContext)
                                .sendBroadcast(playPauseIntent)
                        }

                        KeyEvent.KEYCODE_MEDIA_NEXT -> {
                            nextSong.get()?.let { song ->
                                currentSong = song
                                renderSongInfo(song)
                                song.fullPath?.let { path -> preparePlayer(path) }
                                nextSong.set(null)
                                previousSong.set(null)

                                val intentUpdateSongInfo = Intent(ACTION_UPDATE_SONG_INFO)
                                intentUpdateSongInfo.putExtra(SONG_KEY, song)
                                LocalBroadcastManager.getInstance(applicationContext)
                                    .sendBroadcast(intentUpdateSongInfo)

                                getPreviousAndNextSong(song)
                            }
                        }
                    }
                }
            }
        }
    }

    private val songReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == ACTION_SONG_PICKED) {
                try {
                    currentSong = intent.getParcelableExtra(SONG_KEY)
                    currentSong?.let {
                        renderSongInfo(it)
                        it.fullPath?.let { path -> preparePlayer(path) }
                        getPreviousAndNextSong(it)
//                        player.play() //TODO - UNCOMMENT AFTER DEBUG
                    }
                } catch (e: Exception) {
                    e.message
                }
            }
        }
    }

    private var currentSong: Song? = null
    private var nextSong: AtomicReference<Song?> = AtomicReference(null)
    private var previousSong: AtomicReference<Song?> = AtomicReference(null)

    private val notificationManager =
        appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private val notificationLayout =
        RemoteViews(applicationContext.packageName, R.layout.notification_player)

    override suspend fun doWork(): Result {
        val intentFilter = IntentFilter()
        intentFilter.apply {
            addAction(Intent.ACTION_MEDIA_BUTTON)
            addAction(ACTION_REQUEST_PREVIOUS_SONG)
            addAction(ACTION_REQUEST_PLAY_PAUSE_SONG)
            addAction(ACTION_REQUEST_NEXT_SONG)
        }
        applicationContext.registerReceiver(broadcastReceiver, intentFilter)

        val songFilter = IntentFilter(ACTION_SONG_PICKED)
        LocalBroadcastManager
            .getInstance(applicationContext).registerReceiver(songReceiver, songFilter)

        val audioManager = applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager

        val receiverComponent = ComponentName(applicationContext, KeyEventBroadcastReceiver::class.java)
        audioManager.registerMediaButtonEventReceiver(receiverComponent)

        return try {
            notificationManager.notify(PLAYER_NOTIFICATION_ID, createNotification())

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
            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            notificationManager.createNotificationChannel(channel)
        }

        val previousSongIntent = Intent(ACTION_REQUEST_PREVIOUS_SONG)
        previousSongIntent.putExtra(
            Intent.EXTRA_KEY_EVENT,
            KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_PREVIOUS)
        )
        val previousSongPendingIntent = PendingIntent.getBroadcast(
            applicationContext, 0, previousSongIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        notificationLayout
            .setOnClickPendingIntent(R.id.btn_previous_song_collapsed, previousSongPendingIntent)

        val playPauseIntent = Intent(ACTION_REQUEST_PLAY_PAUSE_SONG)
        playPauseIntent.putExtra(
            Intent.EXTRA_KEY_EVENT,
            KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE)
        )
        val playPausePendingIntent = PendingIntent.getBroadcast(
            applicationContext, 0, playPauseIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        notificationLayout
            .setOnClickPendingIntent(R.id.btn_play_song_collapsed, playPausePendingIntent)

        val nextSongIntent = Intent(ACTION_REQUEST_NEXT_SONG)
        nextSongIntent.putExtra(
            Intent.EXTRA_KEY_EVENT,
            KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_NEXT)
        )
        val nextSongPendingIntent = PendingIntent.getBroadcast(
            applicationContext, 0, nextSongIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        notificationLayout
            .setOnClickPendingIntent(R.id.btn_next_song_collapsed, nextSongPendingIntent)

        val notification = NotificationCompat.Builder(applicationContext, MUSIC_CHANNEL_ID)
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
                    applicationContext.sendBroadcast(nextSongIntent)
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

        const val ACTION_UPDATE_SONG_INFO = "com.coldfier.cfmusic.action_update_song_info"

        const val ACTION_CHANGE_PLAY_BUTTON_ICON = "com.coldfier.cfmusic.action_change_play_button_icon"
        const val EXTRA_IS_PLAYING  = "com.coldfier.cfmusic.extra_is_playing"

        const val ACTION_REQUEST_PREVIOUS_SONG = "com.coldfier.cfmusic.request_previous_song"
        const val ACTION_REQUEST_NEXT_SONG = "com.coldfier.cfmusic.request_next_song"
        const val ACTION_REQUEST_PLAY_PAUSE_SONG = "com.coldfier.cfmusic.request_play_pause_song"
    }
}