package com.sector.cityguide.fragments.login

import android.os.Bundle
import android.util.Log
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

        binding.btnSignIn.setOnClickListener { submitForm() }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        emailFocusListener()
        passwordFocusListener()
    }

    private fun emailFocusListener() {
        binding.etEmail.setOnFocusChangeListener { _, focused ->
            if (!focused)
                binding.emailContainer.helperText = validateEmail()
        }
    }

    private fun passwordFocusListener() {
        binding.etPassword.setOnFocusChangeListener { _, focused ->
            if (!focused)
                binding.passwordContainer.helperText = validatePassword()
        }
    }

    private fun validateEmail(): String? {
        val email = binding.etEmail.text.toString()

        if (email.isEmpty())
            return "Field can't be empty"

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            return "Invalid email"

        return null
    }

    private fun validatePassword(): String? {
        val password = binding.etPassword.text.toString()

        if (password.isEmpty())
            return "Field can't be empty"

        return null
    }

    private fun submitForm() {
        val validEmail = binding.emailContainer.helperText == null
        val validPassword = binding.passwordContainer.helperText == null

        if (validEmail && validPassword) {
            Log.d("MyTag", "Completed!")
        }
    }
}