package com.sector.cityguide.fragments.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sector.cityguide.databinding.ItemProfileMenuBinding
import com.sector.cityguide.models.ProfileMenu

class ProfileAdapter(private val profileMenuList: List<ProfileMenu>): RecyclerView.Adapter<ProfileAdapter.ViewHolder>() {

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
            bind(profileMenuList[position])
        }
    }

    override fun getItemCount(): Int {
        return profileMenuList.size
    }
}