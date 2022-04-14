package com.coldfier.cfmusic.ui.folders_fragment

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.coldfier.cfmusic.R
import com.coldfier.cfmusic.data.database_room.model.SongFolder
import com.coldfier.cfmusic.databinding.FragmentFoldersBinding
import com.coldfier.cfmusic.ui.base.BaseFragment
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FoldersFragment :
    BaseFragment<FoldersViewModel, FragmentFoldersBinding>(R.layout.fragment_folders)
    /*FoldersAdapter.Callback*/ {

    override val viewModel by viewModels<FoldersViewModel> { viewModelFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initObservers()
    }

    private fun initViews() {
//        binding.rvFolders.adapter = FoldersAdapter(this)
    }

    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.songFoldersFlow.collect {
                binding.foldersComposeView.setContent {
                    FoldersListView(foldersList = it) { folder ->
                        folder.folderName?.let {
                            val action = FoldersFragmentDirections.actionFoldersFragmentToPickedFolderFragment(it)
                            findNavController().navigate(action)
                        }
                    }
                }
//                (binding.rvFolders.adapter as FoldersAdapter).submitList(it)
            }
        }
    }

//    override fun folderClicked(folder: SongFolder) {
//        folder.folderName?.let {
//            val action = FoldersFragmentDirections.actionFoldersFragmentToPickedFolderFragment(it)
//            findNavController().navigate(action)
//        }
//    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FoldersListView(foldersList: List<SongFolder>, clickListener: (folder: SongFolder) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(count = 3),
        modifier = Modifier
            .fillMaxWidth()
            .height(800.dp)
    ) {
        foldersList.forEach { folder ->
            item {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        clickListener.invoke(folder)
                    }
                ) {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_folder),
                            contentDescription = folder.folderName ?: "",
                            modifier = Modifier
                                .width(70.dp)
                                .height(70.dp)
                                .align(Alignment.Center)
                        )

                        Text(
                            text = folder.songsQuantity?.toString() ?: "0",
                            textAlign = TextAlign.Center,
                            color = Color(0xFF6B705C),
                            fontFamily = FontFamily(
                                Font(R.font.sf_font_regular)
                            ),
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    Text(
                        text = folder.folderName ?: "",
                        textAlign = TextAlign.Center,
                        color = Color(0xFF0E172D),
                        fontFamily = FontFamily(
                            Font(R.font.sf_font_regular)
                        ),
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewScreen() {
    FoldersListView(foldersList = listOf(SongFolder("name", 5)), clickListener = {})
}