package com.sector.cityguide.fragments.favorite.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sector.cityguide.databinding.FavoriteItemBinding
import com.sector.cityguide.models.Place

class FavoriteAdapter: ListAdapter<Place, FavoriteAdapter.ViewHolder>(ItemComparator()) {

    class ViewHolder(private val binding: FavoriteItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(place: Place) = with(binding) {
            tvPlace.text = place.name

            Glide.with(itemView.context)
                .load(place.image)
                .centerCrop()
                .into(ivPlace)
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FavoriteItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            bind(getItem(position))
        }
    }

    class ItemComparator: DiffUtil.ItemCallback<Place>() {
        override fun areItemsTheSame(oldItem: Place, newItem: Place): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Place, newItem: Place): Boolean {
            return oldItem == newItem
        }
    }
}