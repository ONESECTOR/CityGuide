package com.sector.cityguide.fragments.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.sector.cityguide.R
import com.sector.cityguide.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        initFirebase()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayout()

        binding.button.setOnClickListener {
            if (isLogged()) {
                auth.signOut()
                setLayout()
            } else {
                findNavController().navigate(R.id.action_profileFragment_to_authFirstStepFragment)
            }
        }
    }

    private fun initFirebase() {
        auth = FirebaseAuth.getInstance()
    }

    private fun isLogged(): Boolean {
        val user = auth.currentUser

        return user != null
    }

    private fun setLayout() {
        Log.d("Fb", isLogged().toString())
        if (isLogged()) {
            binding.warningLayout.visibility = View.GONE
            binding.button.text = "Выйти"
        } else {
            binding.warningLayout.visibility = View.VISIBLE
            binding.button.text = "Войти"
        }
    }
}