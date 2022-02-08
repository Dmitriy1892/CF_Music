package com.coldfier.cfmusic.data.database_room.dao

import androidx.room.*
import com.coldfier.cfmusic.data.database_room.model.RoomSong
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

@Dao
interface RoomSongDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSongs(roomSongList: List<RoomSong>)

    @Update
    suspend fun updateSongInfo(roomSong: RoomSong)

    @Query("SELECT * FROM table_song WHERE folder_name = :folderName ORDER BY artist_name ASC")
    fun getSongsWithFolder(folderName: String): Flow<List<RoomSong>>

    @Query("SELECT folder_name FROM table_song ORDER BY folder_name ASC")
    fun getSongFolderNames(): Flow<List<RoomSong>>

    @Query("SELECT * FROM table_song WHERE is_favorite = :isFavorite")
    suspend fun getFavoritesSongs(isFavorite: Boolean = true): List<RoomSong>

    fun getFolders() = getSongFolderNames().distinctUntilChanged()
}