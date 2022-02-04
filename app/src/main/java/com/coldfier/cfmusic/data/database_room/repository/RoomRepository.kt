package com.coldfier.cfmusic.data.database_room.repository

import com.coldfier.cfmusic.data.database_room.dao.RoomSongDao
import com.coldfier.cfmusic.data.database_room.model.RoomSong
import com.coldfier.cfmusic.data.database_room.model.SongFolder
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

    suspend fun getSongsByFolder(folderName: String): List<RoomSong> {
        return roomSongDao.getSongsWithFolder(folderName)
    }

    suspend fun getFavoritesSongs(): List<RoomSong> {
        return roomSongDao.getFavoritesSongs()
    }

    suspend fun getSongFoldersList(): List<SongFolder> {
        val folders = roomSongDao.getSongFolderNames()
        val tempSongFolderMap = mutableMapOf<String, Int>()
        folders.forEach {
            if (it.folderName == null) {
                it.folderName = it.fullPath
                    ?.substringBeforeLast("/")
                    ?.substringAfterLast("/")
                    ?.replace("/", "")
            }

            it.folderName?.let { folder ->
                tempSongFolderMap[folder]?.let { value ->
                    tempSongFolderMap[folder] = value + 1
                } ?: kotlin.run {
                    tempSongFolderMap.put(folder, 1)
                }
            }
        }

        val songFolderList = tempSongFolderMap.map {
            SongFolder(
                folderName = it.key,
                songsQuantity = it.value
            )
        }
        return songFolderList
    }
}