package com.sector.cityguide.fragments.signin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.sector.cityguide.R
import com.sector.cityguide.databinding.FragmentUserBinding

class UserFragment : Fragment() {
    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserBinding.inflate(inflater, container, false)

        initFirebase()
        checkUser()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogout.setOnClickListener {
            auth.signOut()
            findNavController().navigate(R.id.action_userFragment_to_phoneFragment)
        }
    }

    private fun initFirebase() {
        auth = FirebaseAuth.getInstance()
    }

    private fun checkUser() {
        val currentUser = auth.currentUser

        if (currentUser == null)
            findNavController().navigate(R.id.action_userFragment_to_phoneFragment)
    }
}