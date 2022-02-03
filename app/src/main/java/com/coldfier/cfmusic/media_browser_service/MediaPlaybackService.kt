package com.coldfier.cfmusic.media_browser_service

import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.media.MediaBrowserServiceCompat
import androidx.media.session.MediaButtonReceiver
import com.coldfier.cfmusic.R

private const val MY_MEDIA_ROOT_ID = "com.coldfier.cfmusic.media_browser_service.media_root_id"
private const val MY_EMPTY_MEDIA_ROOT_ID = "com.coldfier.cfmusic.media_browser_service.empty_media_root_id"

class MediaPlaybackService: MediaBrowserServiceCompat() {

    private var mediaSession: MediaSessionCompat? = null
    private lateinit var stateBuilder: PlaybackStateCompat.Builder

    override fun onCreate() {
        super.onCreate()

        mediaSession = MediaSessionCompat(baseContext, "MusicService").apply {
            setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS
                    or MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS
            )

            stateBuilder = PlaybackStateCompat.Builder().setActions(
                PlaybackStateCompat.ACTION_PLAY or PlaybackStateCompat.ACTION_PLAY_PAUSE
            )

            setPlaybackState(stateBuilder.build())

            setCallback(Callback())

            setSessionToken(sessionToken)
        }
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot {
        return BrowserRoot(MY_MEDIA_ROOT_ID, null)
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        val mediaItems = mutableListOf<MediaBrowserCompat.MediaItem>()

        if (MY_MEDIA_ROOT_ID == parentId) {
            // Build the MediaItem objects for the top level,
            // and put them in the mediaItems list...
        } else {
            // Examine the passed parentMediaId to see which submenu we're at,
            // and put the children of that menu in the mediaItems list...
        }

        result.sendResult(mediaItems)
    }

    inner class Callback: MediaSessionCompat.Callback() {
        override fun onPlay() {
            super.onPlay()

            val controller = mediaSession?.controller
            val mediaMetadata = controller?.metadata
            val description = mediaMetadata?.description
            val channelId = "MediaChannelId"

            val builder = NotificationCompat.Builder(baseContext, channelId).apply {
                setContentTitle(description?.title)
                setContentText(description?.subtitle)
                setSubText(description?.description)
                setLargeIcon(description?.iconBitmap)

                setContentIntent(controller?.sessionActivity)

                setDeleteIntent(
                    MediaButtonReceiver.buildMediaButtonPendingIntent(
                        baseContext,
                        PlaybackStateCompat.ACTION_STOP
                    )
                )

                setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

                setSmallIcon(R.mipmap.ic_launcher)

                color = ContextCompat.getColor(baseContext, R.color.teal_200)

                addAction(
                    NotificationCompat.Action(
                        R.drawable.ic_pause,
                        getString(R.string.pause),
                        MediaButtonReceiver.buildMediaButtonPendingIntent(
                            baseContext,
                            PlaybackStateCompat.ACTION_PLAY_PAUSE
                        )
                    )
                )

                setStyle(androidx.media.app.NotificationCompat.MediaStyle()
                    .setMediaSession(mediaSession?.sessionToken)
                    .setCancelButtonIntent(
                        MediaButtonReceiver.buildMediaButtonPendingIntent(
                            baseContext,
                            PlaybackStateCompat.ACTION_STOP
                        )
                    )
                )
            }

            startForeground(1, builder.build())
        }
    }
}