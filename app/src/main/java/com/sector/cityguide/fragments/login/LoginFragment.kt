package com.sector.cityguide.fragments.login

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sector.cityguide.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        emailFocusListener()
        passwordFocusListener()
    }

    private fun passwordFocusListener() {
        binding.etPassword.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.passwordContainer.helperText = validatePassword()
            }
        }
    }

    private fun validatePassword(): CharSequence? {
        val password = binding.etPassword.text.toString()

        // this function will update in the future

        return null
    }

    private fun emailFocusListener() {
        binding.etEmail.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.emailContainer.helperText = validateEmail()
            }
        }
    }

    private fun validateEmail(): CharSequence? {
        val email = binding.etEmail.text.toString()

        if (email.isEmpty()) {
            return "Field can't be empty"
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return "Invalid Email Address"
        }

        return null
    }
}