package com.coldfier.cfmusic.ui.folders_fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
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
        fun bind(folder: SongFolder) {
            binding.apply {
                tvFolderName.text = folder.folderName
                tvSongValue.text = folder.songsQuantity?.toString() ?: "0"
            }

            binding.root.setOnClickListener {
                callback.folderClicked(folder)
            }
        }
    }

    interface Callback {
        fun folderClicked(folder: SongFolder)
    }
}