package com.coldfier.cfmusic.ui.albums_fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.coldfier.cfmusic.R
import com.coldfier.cfmusic.databinding.FragmentAlbumsBinding
import com.coldfier.cfmusic.ui.base.BaseFragment
import com.coldfier.cfmusic.use_case.model.Album
import kotlinx.coroutines.flow.collect

class AlbumsFragment: BaseFragment<AlbumsViewModel, FragmentAlbumsBinding>(
    R.layout.fragment_albums
), AlbumsAdapter.Callback {

    override val viewModel: AlbumsViewModel by viewModels { viewModelFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initClickers()
        initObservers()
    }

    private fun initView() {
        binding.rvAlbums.adapter = AlbumsAdapter(this)
    }

    private fun initClickers() {

    }

    private fun initObservers() {
        collectFlowInCoroutine {
            viewModel.albumListFlow.collect {
                (binding.rvAlbums.adapter as AlbumsAdapter).submitList(it)
            }
        }

    }

    override fun albumPicked(album: Album) {
        //TODO - NAVIGATE TO DETAIL SCREEN
    }
}