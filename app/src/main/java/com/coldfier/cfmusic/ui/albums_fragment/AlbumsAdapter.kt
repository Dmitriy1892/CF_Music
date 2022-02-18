package com.coldfier.cfmusic.ui.albums_fragment

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.coldfier.cfmusic.R
import com.coldfier.cfmusic.databinding.RvItemAlbumBinding
import com.coldfier.cfmusic.use_case.model.Album

class AlbumsAdapter(
    private val callback: Callback
): ListAdapter<Album, AlbumsAdapter.AlbumViewHolder>(AlbumsDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        return AlbumViewHolder(
            RvItemAlbumBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    class AlbumsDiffUtil: DiffUtil.ItemCallback<Album>() {
        override fun areItemsTheSame(oldItem: Album, newItem: Album): Boolean {
            return oldItem.albumId == newItem.albumId
        }

        override fun areContentsTheSame(oldItem: Album, newItem: Album): Boolean {
            return oldItem.albumId == newItem.albumId
        }

    }

    inner class AlbumViewHolder(
        private val binding: RvItemAlbumBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(album: Album) {
            binding.imageViewAlbumImage.setImageBitmap(
                album.albumImage ?:
                BitmapFactory.decodeResource(
                    binding.root.context.applicationContext.resources,
                    R.drawable.bg_album_placeholder
                )
            )

            binding.tvAlbumName.text = album.albumName ?: ""

            binding.root.setOnClickListener {
                callback.albumPicked(album)
            }
        }
    }

    interface Callback {
        fun albumPicked(album: Album)
    }
}