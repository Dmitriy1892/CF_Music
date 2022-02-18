package com.coldfier.cfmusic.use_case.model

import android.graphics.Bitmap

data class Album(
    var albumId: Int? = null,
    var albumName: String? = null,
    var albumImage: Bitmap? = null
)