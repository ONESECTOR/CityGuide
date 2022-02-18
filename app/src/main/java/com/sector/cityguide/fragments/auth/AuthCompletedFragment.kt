package com.sector.cityguide.fragments.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.sector.cityguide.R
import com.sector.cityguide.databinding.FragmentAccountCreatedBinding

class AuthCompletedFragment : Fragment() {
    private var _binding: FragmentAccountCreatedBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountCreatedBinding.inflate(inflater, container, false)

        binding.btnAccept.setOnClickListener {
            findNavController().navigate(R.id.action_authCompletedFragment_to_profileFragment)
        }

        return binding.root
    }
}