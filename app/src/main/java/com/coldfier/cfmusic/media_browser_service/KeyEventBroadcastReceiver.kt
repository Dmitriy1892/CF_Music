package com.coldfier.cfmusic.media_browser_service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.view.KeyEvent

class KeyEventBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action != Intent.ACTION_MEDIA_BUTTON) return

        val keyEvent = intent.getParcelableExtra<KeyEvent>(Intent.EXTRA_KEY_EVENT)

        when (keyEvent?.keyCode) {
            KeyEvent.KEYCODE_MEDIA_PREVIOUS -> {
                val previousSongIntent = Intent(Intent.ACTION_MEDIA_BUTTON)
                previousSongIntent.putExtra(
                    Intent.EXTRA_KEY_EVENT,
                    KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_PREVIOUS)
                )
                context?.sendBroadcast(previousSongIntent)
            }

            KeyEvent.KEYCODE_MEDIA_PAUSE -> {
                val pauseIntent = Intent(Intent.ACTION_MEDIA_BUTTON)
                pauseIntent.putExtra(
                    Intent.EXTRA_KEY_EVENT,
                    KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_PAUSE)
                )
                context?.sendBroadcast(pauseIntent)
            }

            KeyEvent.KEYCODE_MEDIA_PLAY -> {
                val playIntent = Intent(Intent.ACTION_MEDIA_BUTTON)
                playIntent.putExtra(
                    Intent.EXTRA_KEY_EVENT,
                    KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_PLAY)
                )
                context?.sendBroadcast(playIntent)
            }

            KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE -> {
                val playPauseIntent = Intent(Intent.ACTION_MEDIA_BUTTON)
                playPauseIntent.putExtra(
                    Intent.EXTRA_KEY_EVENT,
                    KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE)
                )
                context?.sendBroadcast(playPauseIntent)
            }

            KeyEvent.KEYCODE_MEDIA_NEXT -> {
                val nextSongIntent = Intent(Intent.ACTION_MEDIA_BUTTON)
                nextSongIntent.putExtra(
                    Intent.EXTRA_KEY_EVENT,
                    KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_NEXT)
                )
                context?.sendBroadcast(nextSongIntent)
            }
        }
    }
}