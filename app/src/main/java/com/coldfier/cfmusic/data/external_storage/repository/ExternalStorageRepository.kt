package com.coldfier.cfmusic.data.external_storage.repository

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import com.coldfier.cfmusic.data.external_storage.model.ExternalStorageSong
import com.coldfier.cfmusic.use_case.model.Song
import timber.log.Timber
import javax.inject.Inject

class ExternalStorageRepository @Inject constructor(private val appContext: Context) {

    @SuppressLint("Range")
    fun getAllSongs(): List<ExternalStorageSong> {
        val tempSongList = mutableListOf<ExternalStorageSong>()
        val allSongsUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"

        val cursor = appContext.contentResolver.query(
            allSongsUri, null, selection, null, null
        )

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                try {
                    do {
                        val song = getSongFromCursorPosition(cursor)
                        tempSongList.add(song)
                        Timber.tag("SONG").d(song.toString())
                    } while (cursor.moveToNext())
                } catch (e: Exception) {
                    Timber.e(e)
                }
            }
            cursor.close()
        }
        return tempSongList.toList()
    }

    fun getSongById(songId: Int): ExternalStorageSong {
        val mediaSongUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val selection = MediaStore.Audio.Media._ID + " =? "
        val selectionArgs = arrayOf("$songId")

        val cursor = appContext.contentResolver.query(
            mediaSongUri, null, selection, selectionArgs, null
        )

        var song = try {
            cursor!!.moveToPosition(0)
            getSongFromCursorPosition(cursor)
        } catch (e: Exception) {
            Timber.e(e)
            ExternalStorageSong()
        }

        return song
    }

    @SuppressLint("Range")
    private fun getSongFromCursorPosition(cursor: Cursor): ExternalStorageSong {
        val songName =
            cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME))
        val songId =
            cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media._ID))
        val fullPath =
            cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
        val albumName =
            cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM))
        val albumId =
            cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID))
        val artist =
            cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
        val artistId =
            cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID))

        return ExternalStorageSong(
            songName = songName, songId = songId, artist = artist, artistId = artistId,
            albumName = albumName, albumId = albumId, fullPath = fullPath
        )
    }
}