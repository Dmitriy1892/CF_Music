package com.coldfier.cfmusic.ui.player_fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.coldfier.cfmusic.R
import com.coldfier.cfmusic.data.database_room.model.RoomSong
import com.coldfier.cfmusic.databinding.PagerItemImageBinding
import com.coldfier.cfmusic.use_case.model.Song

class PlayerViewPagerAdapter: ListAdapter<Song, PlayerViewPagerAdapter.PlayerViewHolder>(PlayerAdapterUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        return PlayerViewHolder(
            PagerItemImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    class PlayerAdapterUtil: DiffUtil.ItemCallback<Song>() {
        override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {
            return oldItem == newItem
        }

    }

    inner class PlayerViewHolder(
        private val binding: PagerItemImageBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(song: Song) {
            if (song.imageThumbnail != null) {
                binding.imageViewSongImage.load(song.imageThumbnail)
            } else {
                binding.imageViewSongImage.load(R.drawable.bg_song_placeholder)
            }
        }
    }
}