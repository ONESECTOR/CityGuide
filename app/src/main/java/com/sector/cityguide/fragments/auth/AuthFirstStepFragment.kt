package com.sector.cityguide.fragments.auth

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.sector.cityguide.databinding.FragmentAuthFirstStepBinding
import java.util.concurrent.TimeUnit

class AuthFirstStepFragment : Fragment() {
    private var _binding: FragmentAuthFirstStepBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthFirstStepBinding.inflate(inflater, container, false)

        initFirebase()
        setCallbacks()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnNext.setOnClickListener {
            if (getPhoneNumber().length != 12) {
                binding.phoneNumberContainer.error = "Введите корректный номер телефона"
            } else {
                binding.phoneNumberContainer.error = null
                binding.progressBar.visibility = View.VISIBLE
                sendCode()
            }
        }

        binding.btnBack.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initFirebase() {
        auth = FirebaseAuth.getInstance()
    }

    private fun sendCode() {
        Log.d("MyTag", getPhoneNumber())

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(getPhoneNumber())
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun getPhoneNumber(): String {
        val prefix = binding.phoneNumberContainer.prefixText.toString()
        val editText = binding.etPhoneNumber.text.toString().trim()

        return "$prefix$editText"
    }

    private fun setCallbacks() {
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // If user is already verified.
                Log.d("MyTag", "already verified")
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // When the verification is failed
                Log.d("MyTag", "problem!")
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                // When the OTP code is successfully sent
                Log.d("MyTag", "success!")
                binding.progressBar.visibility = View.GONE

                val action = AuthFirstStepFragmentDirections.
                actionAuthFirstStepFragmentToAuthSecondStepFragment(
                    verificationId = verificationId,
                    phoneNumber = getPhoneNumber()
                )
                findNavController().navigate(action)
            }
        }
    }
}