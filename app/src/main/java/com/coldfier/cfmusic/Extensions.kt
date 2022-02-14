package com.coldfier.cfmusic

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Build
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import android.util.Size
import com.coldfier.cfmusic.data.database_room.model.RoomSong
import com.coldfier.cfmusic.data.external_storage.model.ExternalStorageSong
import com.coldfier.cfmusic.use_case.model.Song
import java.lang.NullPointerException

fun RoomSong.convertToSong() = this.isFavorite?.let {
    this.timeStampPaused?.let { it1 ->
        Song(
            songName = this.songName,
            songId = this.songId,
            artist = this.artist,
            artistId = this.artistId,
            albumName = this.albumName,
            albumId = this.albumId,
            fullPath = this.fullPath,
            folderName = this.folderName,
            isFavorite = it,
            timeStampPaused = it1
        )
    }
}

fun RoomSong.convertToSongWithThumbnail(context: Context) = this.isFavorite?.let {
    this.timeStampPaused?.let { it1 ->
        Song(
            songName = this.songName,
            songId = this.songId,
            artist = this.artist,
            artistId = this.artistId,
            albumName = this.albumName,
            albumId = this.albumId,
            fullPath = this.fullPath,
            folderName = this.folderName,
            imageThumbnail = this.songId?.let { getSongThumbnail(it, context) },
            isFavorite = it,
            timeStampPaused = it1
        )
    }
}

fun Song.convertToRoomSong() = RoomSong(
    songName = this.songName,
    songId = this.songId,
    artist = this.artist,
    artistId = this.artistId,
    albumName = this.albumName,
    albumId = this.albumId,
    fullPath = this.fullPath,
    folderName = this.folderName,
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
    fullPath = this.fullPath,
    folderName = this.fullPath
        ?.substringBeforeLast("/")
        ?.substringAfterLast("/")
        ?.replace("/", "")
)

@SuppressLint("Range")
private fun getSongThumbnail(songId: Int, appContext: Context): Bitmap {

    val uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, songId.toLong())



    val bitmap =  try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            appContext.contentResolver.loadThumbnail(uri, Size(640,480), null)
        } else {
            MediaStore.Images.Media.getBitmap(appContext.contentResolver, uri)
        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//            val source = ImageDecoder.createSource(appContext.contentResolver, uri)
//            ImageDecoder.decodeBitmap(source)
//        } else {
//            MediaStore.Images.Media.getBitmap(appContext.contentResolver, uri)
//        }
    } catch (e: Exception) {
        BitmapFactory.decodeResource(
            appContext.resources,
            R.drawable.bg_song_placeholder
        )
    }
    return bitmap
}

private fun getThumbnail(albumId: Int) {

}