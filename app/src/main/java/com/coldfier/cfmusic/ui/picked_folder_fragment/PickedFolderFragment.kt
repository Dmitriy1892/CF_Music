package com.coldfier.cfmusic.ui.picked_folder_fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.navArgs
import com.coldfier.cfmusic.R
import com.coldfier.cfmusic.databinding.FragmentPickedFolderBinding
import com.coldfier.cfmusic.ui.base.BaseFragment
import com.coldfier.cfmusic.ui.player_fragment.PlayerWorker
import com.coldfier.cfmusic.use_case.model.Song
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext

class PickedFolderFragment: BaseFragment<PickedFolderViewModel, FragmentPickedFolderBinding>(
    R.layout.fragment_picked_folder
), PickedFolderAdapter.Callback {
    override val viewModel: PickedFolderViewModel by viewModels { viewModelFactory }

    private val pickedFolderArgs by navArgs<PickedFolderFragmentArgs>()

    private val songBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                PlayerWorker.ACTION_UPDATE_SONG_INFO -> {
                    val song = intent.getParcelableExtra<Song>(SONG_KEY)
                    song?.folderName?.let { viewModel.getSongListFromFolder(it) }
                    song?.let { viewModel.setPickedSong(it) }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initClickers()
        initObservers()

        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(
            songBroadcastReceiver,
            IntentFilter(PlayerWorker.ACTION_UPDATE_SONG_INFO)
        )
    }

    private fun initViews() {
        binding.rvSongs.adapter = PickedFolderAdapter(this)

        viewModel.getSongListFromFolder(pickedFolderArgs.folderName)
    }

    private fun initClickers() {

    }

    private fun initObservers() {
        collectFlowInCoroutine {
            viewModel.songListStateFlow.collect {
                if (it.isNotEmpty()) {
                    (binding.rvSongs.adapter as PickedFolderAdapter).submitList(it)
                    viewModel.previousPickedSong.get()?.let { song ->
                        changeItemBackground(song, false)
                    }
                    changeItemBackground(
                        viewModel.pickedSongStateFlow.value,
                        true
                    )
                }
            }
        }

        collectFlowInCoroutine {
            viewModel.pickedSongStateFlow.collect {
                if (it.songId != null) {
                    val previousPickedSong = viewModel.previousPickedSong.get()
                    previousPickedSong?.let { song ->
                        changeItemBackground(song, false)
                    }
                    changeItemBackground(it, true)
                }
            }
        }
    }

    private suspend fun changeItemBackground(song: Song, isPicked: Boolean) {
        withContext(Dispatchers.IO) {
            val currentList =
                (binding.rvSongs.adapter as PickedFolderAdapter).currentList
            val songIndex = kotlin.run {
                currentList.forEachIndexed { index, currentSong ->
                    if (currentSong.songId == song.songId) return@run index
                }
                -1
            }

            withContext(Dispatchers.Main) {
                if (songIndex != -1) {
                    if (isPicked) {
                        binding.rvSongs.layoutManager?.findViewByPosition(songIndex)
                            ?.setBackgroundResource(R.color.picked_song_color)
                    } else {
                        binding.rvSongs.layoutManager?.findViewByPosition(songIndex)
                            ?.setBackgroundResource(R.color.white)
                    }
                }
            }
        }
    }

    override fun songPicked(song: Song) {
        val intent = Intent(ACTION_SONG_PICKED)
        intent.putExtra(SONG_KEY, song)
        LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)

        viewModel.setPickedSong(song)
    }
}

const val ACTION_SONG_PICKED = "com.coldfier.cfmusic.song_picked"
const val SONG_KEY = "com.coldfier.cfmusic.song_key"