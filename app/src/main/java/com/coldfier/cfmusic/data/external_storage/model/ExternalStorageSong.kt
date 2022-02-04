package com.coldfier.cfmusic.data.external_storage.model

data class ExternalStorageSong(
    var songName: String? = null,
    var songId: Int? = null,
    var artist: String? = null,
    var artistId: Int? = null,
    var albumName: String? = null,
    var albumId: Int? = null,
    var fullPath: String? = null,
)