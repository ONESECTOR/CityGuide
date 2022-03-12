package com.sector.cityguide.fragments.profile.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sector.cityguide.R
import com.sector.cityguide.databinding.ItemProfileBinding
import com.sector.cityguide.fragments.profile.ProfileFragmentDirections
import com.sector.cityguide.models.Profile

class ProfileAdapter: ListAdapter<Profile, ProfileAdapter.ViewHolder>(ItemComparator()) {

    class ViewHolder(private val binding: ItemProfileBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(profile: Profile) = with(binding) {
            if (profile.name == "") {
                tvTitle.text = "Введите имя"
            } else {
                tvTitle.text = profile.name
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemProfileBinding.inflate(layoutInflater, parent, false)
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
                    ProfileFragmentDirections.actionProfileFragmentToEditNameFragment(
                        getItem(position)
                    )
                )
            }
        }
    }

    class ItemComparator: DiffUtil.ItemCallback<Profile>() {
        override fun areItemsTheSame(oldItem: Profile, newItem: Profile): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Profile, newItem: Profile): Boolean {
            return oldItem == newItem
        }
    }
}