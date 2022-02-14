package com.coldfier.cfmusic.data.database_room.repository

import com.coldfier.cfmusic.data.database_room.dao.RoomSongDao
import com.coldfier.cfmusic.data.database_room.model.RoomSong
import com.coldfier.cfmusic.data.database_room.model.SongFolder
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class RoomRepository @Inject constructor(
    private val roomSongDao: RoomSongDao
) {
    suspend fun addSongsToDatabase(songsList: List<RoomSong>) {
        roomSongDao.insertSongs(songsList)
    }

    suspend fun updateSong(song: RoomSong) {
        roomSongDao.updateSongInfo(song)
    }

    suspend fun getSongById(songId: Int): RoomSong {
        return roomSongDao.getSongById(songId)
    }

    fun getSongsByFolder(folderName: String): Flow<List<RoomSong>> {
        return roomSongDao.getSongsWithFolder(folderName)
    }

    suspend fun getFavoritesSongs(): List<RoomSong> {
        return roomSongDao.getFavoritesSongs()
    }

    fun getSongFoldersList(): Flow<List<SongFolder>> {
        val folders = roomSongDao.getFolders()
        val tempSongFolderMap = mutableMapOf<String, Int>()
        return folders.map { list ->
            list.forEach { roomSong ->
                roomSong.folderName?.let { folder ->
                    tempSongFolderMap[folder]?.let { value ->
                        tempSongFolderMap[folder] = value + 1
                    } ?: kotlin.run {
                        tempSongFolderMap.put(folder, 1)
                    }
                }
            }

            tempSongFolderMap.map {
                SongFolder(
                    folderName = it.key,
                    songsQuantity = it.value
                )
            }
        }
    }
}