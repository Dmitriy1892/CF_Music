package com.coldfier.cfmusic.ui.folders_fragment

import androidx.fragment.app.viewModels
import com.coldfier.cfmusic.R
import com.coldfier.cfmusic.databinding.FragmentFoldersBinding
import com.coldfier.cfmusic.ui.base.BaseFragment

class FoldersFragment :
    BaseFragment<FoldersViewModel, FragmentFoldersBinding>(R.layout.fragment_folders) {

    override val viewModel: () -> FoldersViewModel by viewModels()


}