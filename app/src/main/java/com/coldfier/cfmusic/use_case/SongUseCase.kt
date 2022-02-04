package com.coldfier.cfmusic.use_case

import android.content.Context
import com.coldfier.cfmusic.convertToRoomSong
import com.coldfier.cfmusic.convertToSong
import com.coldfier.cfmusic.data.database_room.model.SongFolder
import com.coldfier.cfmusic.data.database_room.repository.RoomRepository
import com.coldfier.cfmusic.data.external_storage.repository.ExternalStorageRepository
import com.coldfier.cfmusic.use_case.model.Song
import javax.inject.Inject

class SongUseCase @Inject constructor(
    private val appContext: Context,
    private val externalStorageRepository: ExternalStorageRepository,
    private val roomRepository: RoomRepository
) {
    suspend fun getAllSongs(): List<Song> {
        val roomSongs = externalStorageRepository.getAllSongs().map { it.convertToRoomSong() }
        roomRepository.addSongsToDatabase(roomSongs)
        return roomSongs.map { it.convertToSong(appContext) }
    }

    suspend fun getSongFoldersList(): List<SongFolder> {
        return try {
            roomRepository.getSongFoldersList()
        } catch (e: Exception) {
            getAllSongs()
            roomRepository.getSongFoldersList()
        }
    }

    suspend fun getSongsFromFolder(folderName: String): List<Song> {
        val roomSongs = roomRepository.getSongsByFolder(folderName)
        return roomSongs.map { it.convertToSong(appContext) }
    }
}