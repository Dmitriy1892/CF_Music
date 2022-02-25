package com.coldfier.cfmusic.di.module

import android.content.Context
import android.os.PowerManager
import android.support.v4.media.MediaBrowserCompat
import com.google.android.exoplayer2.ExoPlayer
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PlayerModule {

    @Singleton
    @Provides
    fun provideExoPlayerInstance(context: Context): ExoPlayer {
        return ExoPlayer.Builder(context).setWakeMode(PowerManager.PARTIAL_WAKE_LOCK).build()
    }
}