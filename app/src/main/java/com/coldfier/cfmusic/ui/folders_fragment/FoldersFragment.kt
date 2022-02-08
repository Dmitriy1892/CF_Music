package com.coldfier.cfmusic.ui.folders_fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.coldfier.cfmusic.R
import com.coldfier.cfmusic.data.database_room.model.SongFolder
import com.coldfier.cfmusic.databinding.FragmentFoldersBinding
import com.coldfier.cfmusic.ui.base.BaseFragment
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class FoldersFragment :
    BaseFragment<FoldersViewModel, FragmentFoldersBinding>(R.layout.fragment_folders),
    FoldersAdapter.Callback {



    override val viewModel by viewModels<FoldersViewModel> { viewModelFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initClickers()
        initObservers()
    }

    override fun onResume() {
        super.onResume()

        try {
            if ((binding.rvFolders.adapter as FoldersAdapter).currentList.isEmpty()) {
                viewModel.updateSongs()
            }
        } catch (e: Exception) {  }
    }

    private fun initViews() {
        binding.rvFolders.adapter = FoldersAdapter(this)
    }

    private fun initClickers() {

    }

    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.songFoldersFlow.collect {
                if (it.isEmpty()) {
                    viewModel.updateSongs()
                } else {
                    (binding.rvFolders.adapter as FoldersAdapter).submitList(it)
                }
            }
        }
    }

    override fun folderClicked(folder: SongFolder) {
        //TODO - OPEN THE FOLDER FRAGMENT
    }
}