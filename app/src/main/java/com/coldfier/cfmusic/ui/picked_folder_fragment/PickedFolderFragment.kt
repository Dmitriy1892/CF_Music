package com.coldfier.cfmusic.ui.picked_folder_fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.navArgs
import com.coldfier.cfmusic.R
import com.coldfier.cfmusic.databinding.FragmentPickedFolderBinding
import com.coldfier.cfmusic.ui.base.BaseFragment
import com.coldfier.cfmusic.use_case.model.Song
import kotlinx.coroutines.flow.collect

class PickedFolderFragment: BaseFragment<PickedFolderViewModel, FragmentPickedFolderBinding>(
    R.layout.fragment_picked_folder
), PickedFolderAdapter.Callback {
    override val viewModel: PickedFolderViewModel by viewModels { viewModelFactory }

    private val pickedFolderArgs by navArgs<PickedFolderFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initClickers()
        initObservers()
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
                }
            }
        }
    }

    override fun songPicked(song: Song) {
        //TODO - PLAY PICKED MUSIC

        val intent = Intent(ACTION_SONG_PICKED)
        intent.putExtra(SONG_KEY, song)
        LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
    }
}

const val ACTION_SONG_PICKED = "com.coldfier.cfmusic.song_picked"
const val SONG_KEY = "com.coldfier.cfmusic.song_key"