package com.coldfier.cfmusic.use_case

import android.content.Context
import com.coldfier.cfmusic.convertToRoomSong
import com.coldfier.cfmusic.convertToSong
import com.coldfier.cfmusic.data.database_room.model.SongFolder
import com.coldfier.cfmusic.data.database_room.repository.RoomRepository
import com.coldfier.cfmusic.data.external_storage.repository.ExternalStorageRepository
import com.coldfier.cfmusic.use_case.model.Song
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onEmpty
import javax.inject.Inject

class SongUseCase @Inject constructor(
    private val appContext: Context,
    private val externalStorageRepository: ExternalStorageRepository,
    private val roomRepository: RoomRepository
) {
    @Volatile
    private var isDbUpdated = false

    suspend fun getAllSongs(): List<Song> {
        val roomSongs = externalStorageRepository.getAllSongs().map { it.convertToRoomSong() }
        roomRepository.addSongsToDatabase(roomSongs)
        return roomSongs.map { it.convertToSong(appContext) ?: Song() }
    }

    suspend fun getSongFoldersList(): Flow<List<SongFolder>> {
        return roomRepository.getSongFoldersList()
    }

    suspend fun getSongsFromFolder(folderName: String): List<Song> {
        val roomSongs = roomRepository.getSongsByFolder(folderName)
        return roomSongs.map { it.convertToSong(appContext) ?: Song() }
    }
}