package com.sector.cityguide.fragments.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.sector.cityguide.R
import com.sector.cityguide.databinding.FragmentProfileBinding
import com.sector.cityguide.model.ProfileMenu

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var profileMenuList: MutableList<ProfileMenu>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        profileMenuList = mutableListOf(
            ProfileMenu(
                title = "Favorite",
                description = "What you liked",
                icon = R.drawable.ic_outline_favorite_light
            )
        )

        val adapter = ProfileAdapter(profileMenuList)
        binding.rvProfileMenu.layoutManager = LinearLayoutManager(requireContext())
        binding.rvProfileMenu.adapter = adapter
    }
}