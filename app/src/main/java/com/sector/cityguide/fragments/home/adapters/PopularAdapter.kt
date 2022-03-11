package com.sector.cityguide.fragments.home.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sector.cityguide.databinding.ItemPopularBinding
import com.sector.cityguide.fragments.home.HomeFragmentDirections
import com.sector.cityguide.models.Place

class PopularAdapter: ListAdapter<Place, PopularAdapter.ViewHolder>(ItemComparator()) {

    class ViewHolder(private val binding: ItemPopularBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(place: Place) = with(binding) {
            tvTitle.text = place.name

            Glide.with(itemView.context)
                .load(place.image)
                .centerCrop()
                .into(imageView)
        }

        companion object {
            fun bind(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemPopularBinding.inflate(layoutInflater, parent, false)
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

            itemView.setOnClickListener {
                itemView.findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToDetailFragment(getItem(position))
                )
            }
        }
    }
}