package com.sector.cityguide.fragments.profile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sector.cityguide.databinding.ItemProfileMenuBinding
import com.sector.cityguide.models.ProfileMenu

class ProfileAdapter: ListAdapter<ProfileMenu, ProfileAdapter.ViewHolder>(ItemComparator()) {

    class ViewHolder(private val binding: ItemProfileMenuBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(menu: ProfileMenu) = with(binding) {
            tvTitle.text = menu.title
            tvDescription.text = menu.description
            Glide.with(itemView.context).load(menu.icon).into(ivIcon)
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemProfileMenuBinding.inflate(layoutInflater, parent, false)
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

    class ItemComparator: DiffUtil.ItemCallback<ProfileMenu>() {
        override fun areItemsTheSame(oldItem: ProfileMenu, newItem: ProfileMenu): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ProfileMenu, newItem: ProfileMenu): Boolean {
            return oldItem == newItem
        }
    }
}