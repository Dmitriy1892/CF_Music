package com.coldfier.cfmusic.di.module

import android.content.Context
import com.google.android.exoplayer2.ExoPlayer
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PlayerModule {

    @Singleton
    @Provides
    fun provideExoPlayerInstance(context: Context): ExoPlayer {
        return ExoPlayer.Builder(context).build()
    }
}