package com.sector.cityguide.fragments.signup

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sector.cityguide.databinding.FragmentSignupBinding

class SignupFragment : Fragment() {
    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)

        emailFocusListener()
        passwordFocusListener()
        repeatPasswordFocusListener()

        binding.btnSignUp.setOnClickListener { submitForm() }

        return binding.root
    }

    private fun repeatPasswordFocusListener() {
        binding.etRepeatPassword.setOnFocusChangeListener { _, focused ->
            if (!focused)
                binding.repeatPasswordContainer.helperText = validateRepeatPassword()
        }
    }

    private fun validateRepeatPassword(): String? {
        val repeatedPassword = binding.etRepeatPassword.text.toString()

        if (repeatedPassword.isEmpty())
            return "Field can't be empty"

        return null
    }

    private fun passwordFocusListener() {
        binding.etPassword.setOnFocusChangeListener { _, focused ->
            if (!focused)
                binding.passwordContainer.helperText = validatePassword()
        }
    }

    private fun validatePassword(): String? {
        val password = binding.etPassword.text.toString()

        if (password.isEmpty())
            return "Field can't be empty"

        if (password.length < 8)
            return "Minimum 8 characters"

        return null
    }

    private fun submitForm() {
        val validEmail = binding.emailContainer.helperText == null
        val validPassword = binding.passwordContainer.helperText == null
        val validRepeatedPassword = binding.repeatPasswordContainer.helperText == null

        if (validEmail && validPassword && validRepeatedPassword && passwordsMatches()) {
            Log.d("MyTag", "Completed!")
        }
    }

    private fun emailFocusListener() {
        binding.etEmail.setOnFocusChangeListener { _, focused ->
            if (!focused)
                binding.emailContainer.helperText = validateEmail()
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

    private fun passwordsMatches(): Boolean {
        val password = binding.etPassword.text.toString()
        val repeatedPassword = binding.etRepeatPassword.text.toString()

        return if (password != repeatedPassword) {
            binding.repeatPasswordContainer.helperText = "Passwords dont match!"
            false
        } else {
            true
        }
    }
}