package com.coldfier.cfmusic.ui.folders_fragment

import android.content.Context
import android.media.AudioManager
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.fragment.app.viewModels
import com.coldfier.cfmusic.R
import com.coldfier.cfmusic.databinding.FragmentFoldersBinding
import com.coldfier.cfmusic.ui.base.BaseFragment

class FoldersFragment :
    BaseFragment<FoldersViewModel, FragmentFoldersBinding>(R.layout.fragment_folders) {

    override val viewModel by viewModels<FoldersViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ms = MediaStore.Files()

        val audioManager = requireActivity().getSystemService(Context.AUDIO_SERVICE) as AudioManager
//        ExoPlayer.Builder(requireContext()).setLooper(Looper.getMainLooper()).setMediaSourceFactory(
//            MediaSourceFactory.UNSUPPORTED.createMediaSource(MediaItem.fromUri())
//        )
    }

    private fun initViews() {

    }

    private fun initClickers() {

    }

    private fun initObservers() {

    }
}