package com.coldfier.cfmusic.ui.folders_fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.coldfier.cfmusic.R
import com.coldfier.cfmusic.data.database_room.model.SongFolder
import com.coldfier.cfmusic.databinding.RvItemMusicFolderBinding

class FoldersAdapter(private val callback: Callback): ListAdapter<SongFolder, FoldersAdapter.FolderHolder>(FoldersUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderHolder {
        return FolderHolder(
            RvItemMusicFolderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FolderHolder, position: Int) {
        holder.bind(currentList[position])
    }

    class FoldersUtil: DiffUtil.ItemCallback<SongFolder>() {
        override fun areItemsTheSame(oldItem: SongFolder, newItem: SongFolder): Boolean {
            return oldItem.folderName == newItem.folderName
        }

        override fun areContentsTheSame(oldItem: SongFolder, newItem: SongFolder): Boolean {
            return oldItem.songsQuantity == newItem.songsQuantity
        }

    }

    inner class FolderHolder(
        private val binding: RvItemMusicFolderBinding
    ): RecyclerView.ViewHolder(binding.root) {
        private val composeView = itemView as ComposeView
        fun bind(folder: SongFolder) {
            composeView.setContent {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        callback.folderClicked(folder = folder)
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
            
            
//            binding.apply {
//                tvFolderName.text = folder.folderName
//                tvSongValue.text = folder.songsQuantity?.toString() ?: "0"
//            }
//
//            binding.root.setOnClickListener {
//                callback.folderClicked(folder)
//            }
        }
    }

    interface Callback {
        fun folderClicked(folder: SongFolder)
    }
}