package com.coldfier.cfmusic.use_case.model

import android.graphics.Bitmap
import android.net.Uri

data class Album(
    var albumId: Int? = null,
    var albumName: String? = null,
    var albumImage: Bitmap? = null,
    var albumImageUri: Uri? = null
)