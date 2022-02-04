package com.coldfier.cfmusic

import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import com.coldfier.cfmusic.data.database_room.model.RoomSong
import com.coldfier.cfmusic.data.external_storage.model.ExternalStorageSong
import com.coldfier.cfmusic.use_case.model.Song

fun RoomSong.convertToSong(context: Context) = Song(
    songName = this.songName,
    songId = this.songId,
    artist = this.artist,
    artistId = this.artistId,
    albumName = this.albumName,
    albumId = this.albumId,
    fullPath = this.fullPath,
    imageThumbnail = this.songId?.let { getSongThumbnail(it, context) },
    isFavorite = this.isFavorite,
    timeStampPaused = this.timeStampPaused
)

fun Song.convertToRoomSong() = RoomSong(
    songName = this.songName,
    songId = this.songId,
    artist = this.artist,
    artistId = this.artistId,
    albumName = this.albumName,
    albumId = this.albumId,
    fullPath = this.fullPath,
    isFavorite = this.isFavorite,
    timeStampPaused = this.timeStampPaused
)

fun ExternalStorageSong.convertToRoomSong() = RoomSong(
    songName = this.songName,
    songId = this.songId,
    artist = this.artist,
    artistId = this.artistId,
    albumName = this.albumName,
    albumId = this.albumId,
    fullPath = this.fullPath
)

private fun getSongThumbnail(songId: Int, appContext: Context): Bitmap {
    val uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, songId.toLong())
    val bitmap =  try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(appContext.contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        } else {
            MediaStore.Images.Media.getBitmap(appContext.contentResolver, uri)
        }
    } catch (e: Exception) {
        BitmapFactory.decodeResource(
            appContext.resources,
            R.drawable.bg_song_placeholder
        )
    }
    return bitmap
}