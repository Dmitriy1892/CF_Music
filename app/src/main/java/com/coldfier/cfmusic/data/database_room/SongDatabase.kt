package com.coldfier.cfmusic.data.database_room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.coldfier.cfmusic.data.database_room.dao.RoomSongDao
import com.coldfier.cfmusic.data.database_room.model.RoomSong

@Database(entities = [RoomSong::class], version = 1)
abstract class SongDatabase: RoomDatabase() {
    abstract fun roomSongDao(): RoomSongDao
}