package com.coldfier.cfmusic.di.module

import android.content.Context
import androidx.room.Room
import com.coldfier.cfmusic.data.database_room.SongDatabase
import com.coldfier.cfmusic.data.database_room.dao.RoomSongDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideSongDatabase(context: Context): SongDatabase {
        return Room.databaseBuilder(context, SongDatabase::class.java, "song-database").build()
    }

    @Singleton
    @Provides
    fun provideRoomSongDao(songDatabase: SongDatabase): RoomSongDao {
        return songDatabase.roomSongDao()
    }
}