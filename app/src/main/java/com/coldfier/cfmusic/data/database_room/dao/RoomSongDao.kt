package com.coldfier.cfmusic.data.database_room.dao

import androidx.room.*
import com.coldfier.cfmusic.data.database_room.model.RoomSong

@Dao
interface RoomSongDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSongs(roomSongList: List<RoomSong>)

    @Update
    suspend fun updateSongInfo(roomSong: RoomSong)

    @Query("SELECT * FROM table_song WHERE folder_name = :folderName")
    suspend fun getSongsWithFolder(folderName: String): List<RoomSong>

    @Query("SELECT folder_name, full_path  FROM table_song")
    suspend fun getSongFolderNames(): List<RoomSong>

    @Query("SELECT * FROM table_song WHERE is_favorite = :isFavorite")
    suspend fun getFavoritesSongs(isFavorite: Boolean = true): List<RoomSong>
}