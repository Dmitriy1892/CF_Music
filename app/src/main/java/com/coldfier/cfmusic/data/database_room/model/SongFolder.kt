package com.coldfier.cfmusic.data.database_room.model

import androidx.room.ColumnInfo

data class SongFolder(
    @ColumnInfo(name = "folder_name")
    var folderName: String? = null,
    @ColumnInfo(name = "songs_quantity")
    var songsQuantity: Int? = null
)
