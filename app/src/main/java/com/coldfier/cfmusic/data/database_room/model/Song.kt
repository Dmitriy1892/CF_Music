package com.coldfier.cfmusic.data.database_room.model

import android.graphics.drawable.Drawable

data class Song(
    var imageThumbnail: Drawable? = null,
    var artist: String? = null,
    var songName: String? = null,
    var isFavorite: Boolean = false,
    var timeStampPaused: Double = 0.0
)
