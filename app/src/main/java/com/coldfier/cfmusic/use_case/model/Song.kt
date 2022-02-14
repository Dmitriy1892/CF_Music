package com.coldfier.cfmusic.use_case.model

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Song(
    var songName: String? = null,
    var songId: Int? = null,
    var artist: String? = null,
    var artistId: Int? = null,
    var albumName: String? = null,
    var albumId: Int? = null,
    var fullPath: String? = null,
    var folderName: String? = null,
    var imageThumbnail: Bitmap? = null,
    var isFavorite: Boolean = false,
    var timeStampPaused: Double = 0.0
): Parcelable
