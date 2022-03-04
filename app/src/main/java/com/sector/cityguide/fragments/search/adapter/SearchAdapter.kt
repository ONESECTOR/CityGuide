package com.sector.cityguide.fragments.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sector.cityguide.databinding.FoundItemBinding
import com.sector.cityguide.fragments.search.SearchFragmentDirections
import com.sector.cityguide.models.Place

class SearchAdapter: ListAdapter<Place, SearchAdapter.ViewHolder>(ItemComparator()) {
    class ViewHolder(private val binding: FoundItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(place: Place) = with(binding) {
            tvTitle.text = "${place.name}, ${place.location}"
            tvDescription.text = place.description

            Glide.with(itemView.context)
                .load(place.image)
                .centerCrop()
                .into(ivPlace)
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FoundItemBinding.inflate(layoutInflater, parent, false)
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

            itemView.setOnClickListener {
                itemView.findNavController().navigate(
                    SearchFragmentDirections.actionSearchFragmentToDetailFragment(
                        getItem(position)
                    )
                )
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
}