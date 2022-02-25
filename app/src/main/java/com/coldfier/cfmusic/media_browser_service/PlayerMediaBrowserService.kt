package com.coldfier.cfmusic.media_browser_service

import android.content.Intent
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.media.MediaBrowserServiceCompat
import androidx.media.session.MediaButtonReceiver
import com.coldfier.cfmusic.App
import com.coldfier.cfmusic.use_case.SongUseCase
import com.google.android.exoplayer2.ExoPlayer
import javax.inject.Inject

const val PLAYER_SERVICE_DEBUG_TAG = "com.coldfier.cfmusic.media_browser_service.debug_tag"
const val PLAYER_SERVICE_ROOT_ID = "com.coldfier.cfmusic.media_browser_service.root_id"

class PlayerMediaBrowserService: MediaBrowserServiceCompat() {

    @Inject
    lateinit var player: ExoPlayer

    @Inject
    lateinit var songUseCase: SongUseCase

    @Inject
    lateinit var mediaSessionCallback: PlayerMediaSessionCallback

    private var mediaSession: MediaSessionCompat? = null

    private lateinit var playbackStateBuilder: PlaybackStateCompat.Builder

    override fun onCreate() {
        super.onCreate()

        (applicationContext as App).appComponent.injectPlayerToService(this)

        mediaSession = MediaSessionCompat(baseContext, PLAYER_SERVICE_DEBUG_TAG).apply {

            playbackStateBuilder = PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY
                        or PlaybackStateCompat.ACTION_PLAY_PAUSE
                )

            setPlaybackState(playbackStateBuilder.build())

            setCallback(mediaSessionCallback)

            setSessionToken(sessionToken)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot {
        return BrowserRoot(PLAYER_SERVICE_ROOT_ID, null)
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        val mediaItems = mutableListOf<MediaBrowserCompat.MediaItem>()
        /**
         * BUILD MediaItems here...
         */

        result.sendResult(mediaItems)

        //TODO - UNCOMMENT IF NEEDED
//        preparePlayer()
//        player.play()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        MediaButtonReceiver.handleIntent(mediaSession, intent)
        return super.onStartCommand(intent, flags, startId)
    }
}