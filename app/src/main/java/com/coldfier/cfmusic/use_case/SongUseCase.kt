package com.coldfier.cfmusic.use_case

import android.content.Context
import com.coldfier.cfmusic.convertToRoomSong
import com.coldfier.cfmusic.convertToSong
import com.coldfier.cfmusic.convertToSongWithThumbnail
import com.coldfier.cfmusic.data.database_room.model.SongFolder
import com.coldfier.cfmusic.data.database_room.repository.RoomRepository
import com.coldfier.cfmusic.data.external_storage.repository.ExternalStorageRepository
import com.coldfier.cfmusic.use_case.model.Song
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class SongUseCase @Inject constructor(
    private val appContext: Context,
    private val externalStorageRepository: ExternalStorageRepository,
    private val roomRepository: RoomRepository
) {

    suspend fun getAllSongs(): List<Song> {
        val roomSongs = externalStorageRepository.getAllSongs().map { it.convertToRoomSong() }
        roomRepository.addSongsToDatabase(roomSongs)
        return roomSongs.map { it.convertToSong() ?: Song() }
    }

    suspend fun getSongById(songId: Int): Song {
        val roomSong = roomRepository.getSongById(songId)
        return roomSong.convertToSongWithThumbnail(appContext) ?: Song()
    }

    fun getSongFoldersList(): Flow<List<SongFolder>> {
        return roomRepository.getSongFoldersList()
    }

    fun getSongsFromFolder(folderName: String): Flow<List<Song>> {
        return roomRepository.getSongsByFolder(folderName).map { list ->
            list.map { roomSong -> roomSong.convertToSongWithThumbnail(appContext) ?: Song() }
        }
    }

    suspend fun getSongsFromNextFolder(currentFolderName: String): Flow<List<Song>> {
        val folderList = getSongFoldersList().first()

        val index = kotlin.run {
            folderList.mapIndexed { index, songFolder ->
                if (songFolder.folderName == currentFolderName) {
                    return@run index
                }
            }
            -1
        }

        val newFolderName = if (index + 1 <= folderList.lastIndex) {
            folderList.getOrElse(index + 1) { SongFolder() }.folderName ?: ""
        } else {
            folderList.getOrElse(0) { SongFolder() }.folderName ?: ""
        }

        return flowOf(
            try {
                getSongsFromFolder(newFolderName).first()
            } catch (e: Exception) { listOf() }
        )
    }

    suspend fun getSongsFromPreviousFolder(currentFolderName: String): Flow<List<Song>> {
        val folderList = getSongFoldersList().first()
        val index = kotlin.run {
            folderList.forEachIndexed { index, songFolder ->
                if (songFolder.folderName == currentFolderName) return@run index
            }
            -1
        }

        val newFolderName = when {
            (index - 1 >= 0) -> {
                folderList.getOrElse(index - 1) { SongFolder() }.folderName ?: ""
            }

            (index != -1) -> {
                folderList.getOrElse(folderList.lastIndex) { SongFolder() }.folderName ?: ""
            }

            else -> {
                folderList.getOrElse(0) { SongFolder() }.folderName ?: ""
            }
        }

        return flowOf (
            try {
                getSongsFromFolder(newFolderName).first()
            } catch (e: Exception) { listOf() }
        )
    }
}