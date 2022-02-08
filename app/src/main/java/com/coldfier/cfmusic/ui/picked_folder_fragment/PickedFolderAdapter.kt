package com.coldfier.cfmusic.ui.picked_folder_fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.coldfier.cfmusic.databinding.RvItemSongInfoBinding
import com.coldfier.cfmusic.use_case.model.Song

class PickedFolderAdapter(private val callback: Callback):
    ListAdapter<Song, PickedFolderAdapter.SongHolder>(SongDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongHolder {
        return SongHolder(
            RvItemSongInfoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SongHolder, position: Int) {
        holder.bind(currentList[position])
    }

    class SongDiffUtil: DiffUtil.ItemCallback<Song>() {
        override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
            return oldItem.songId == newItem.songId
        }

        override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {
            return oldItem.songName == newItem.songName
        }

    }

    inner class SongHolder(private val binding: RvItemSongInfoBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(song: Song) {
            binding.apply {
                imageViewSongThumbnail.load(song.imageThumbnail)
                tvSongName.text = song.songName
                tvSongArtist.text = song.artist
            }

            binding.root.setOnClickListener {
                callback.songPicked(song)
            }
        }
    }

    interface Callback {
        fun songPicked(song: Song)
    }
}