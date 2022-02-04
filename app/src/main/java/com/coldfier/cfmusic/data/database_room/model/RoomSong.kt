package com.coldfier.cfmusic.data.database_room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_song")
data class RoomSong(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "song_id")
    var songId: Int? = null,
    @ColumnInfo(name = "song_name")
    var songName: String? = null,
    @ColumnInfo(name = "artist_name")
    var artist: String? = null,
    @ColumnInfo(name = "artist_id")
    var artistId: Int? = null,
    @ColumnInfo(name = "album_name")
    var albumName: String? = null,
    @ColumnInfo(name = "album_id")
    var albumId: Int? = null,
    @ColumnInfo(name = "folder_name")
    var folderName: String? = null,
    @ColumnInfo(name = "full_path")
    var fullPath: String? = null,
    @ColumnInfo(name = "is_favorite")
    var isFavorite: Boolean = false,
    @ColumnInfo(name = "time_stamp_paused")
    var timeStampPaused: Double = 0.0
)
