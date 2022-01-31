package com.sector.cityguide.fragments.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sector.cityguide.databinding.FragmentProfileBinding
import com.sector.cityguide.models.ProfileMenu

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ProfileAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        setupRecyclerView()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fillMenu()
    }

    private fun fillMenu() {
        val list = mutableListOf<ProfileMenu>()

        val viewModel = ViewModelProvider(requireActivity()).get(ProfileViewModel::class.java)
        viewModel.getMenuElements().observe(viewLifecycleOwner, Observer { menu ->
            list.addAll(menu)
            adapter.submitList(list)
        })
    }

    private fun setupRecyclerView() {
        adapter = ProfileAdapter()
        binding.rvProfileMenu.layoutManager = LinearLayoutManager(requireContext())
        binding.rvProfileMenu.adapter = adapter
    }
}