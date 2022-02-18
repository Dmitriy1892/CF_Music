package com.coldfier.cfmusic.data.database_room.model

import androidx.room.ColumnInfo

data class RoomAlbum(
    @ColumnInfo(name = "album_id")
    var albumId: Int? = null,
    @ColumnInfo(name = "album_name")
    var albumName: String? = null
)