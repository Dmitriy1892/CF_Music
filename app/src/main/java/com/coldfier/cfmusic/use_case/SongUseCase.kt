package com.coldfier.cfmusic.use_case

import android.content.Context
import com.coldfier.cfmusic.convertToRoomSong
import com.coldfier.cfmusic.convertToSong
import com.coldfier.cfmusic.data.database_room.model.SongFolder
import com.coldfier.cfmusic.data.database_room.repository.RoomRepository
import com.coldfier.cfmusic.data.external_storage.repository.ExternalStorageRepository
import com.coldfier.cfmusic.use_case.model.Song
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onEmpty
import javax.inject.Inject

class SongUseCase @Inject constructor(
    private val appContext: Context,
    private val externalStorageRepository: ExternalStorageRepository,
    private val roomRepository: RoomRepository
) {

    suspend fun getAllSongs(): List<Song> {
        val roomSongs = externalStorageRepository.getAllSongs().map { it.convertToRoomSong() }
        roomRepository.addSongsToDatabase(roomSongs)
        return roomSongs.map { it.convertToSong(appContext) ?: Song() }
    }

    suspend fun getSongById(songId: Int): Song {
        val roomSong = roomRepository.getSongById(songId)
        return roomSong.convertToSong(appContext) ?: Song()
    }

    fun getSongFoldersList(): Flow<List<SongFolder>> {
        return roomRepository.getSongFoldersList()
    }

    fun getSongsFromFolder(folderName: String): Flow<List<Song>> {
        return roomRepository.getSongsByFolder(folderName).map { list ->
            list.map { roomSong -> roomSong.convertToSong(appContext) ?: Song() }
        }
    }
}