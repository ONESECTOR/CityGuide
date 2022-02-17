package com.sector.cityguide.fragments.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sector.cityguide.databinding.ItemRecommendationBinding
import com.sector.cityguide.models.Place

class RecommendationsAdapter: ListAdapter<Place, RecommendationsAdapter.ViewHolder>(ItemComparator()) {

    class ViewHolder(private val binding: ItemRecommendationBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(place: Place) = with(binding) {
            tvTitle.text = place.name
            tvLocation.text = place.location

            Glide.with(itemView.context)
                .load(place.image)
                .centerCrop()
                .into(ivPlace)
        }

        companion object {
            fun bind(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemRecommendationBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.bind(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            bind(getItem(position))
        }
    }
}