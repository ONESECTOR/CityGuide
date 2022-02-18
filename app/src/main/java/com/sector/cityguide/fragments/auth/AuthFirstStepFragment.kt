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
            sendCode()
        }

        binding.btnBack.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun initFirebase() {
        auth = FirebaseAuth.getInstance()
    }

    private fun sendCode() {
        val prefix = binding.phoneNumberContainer.prefixText.toString()
        val editText = binding.etPhoneNumber.text.toString().trim()
        val phoneNumber = prefix + editText
        Log.d("MyTag", phoneNumber)

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
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
                openConfirmFragment(verificationId)
            }
        }
    }

    private fun openConfirmFragment(verificationId: String) {
        val action = AuthFirstStepFragmentDirections.actionPhoneFragmentToConfirmPhoneFragment(verificationId = verificationId)
        findNavController().navigate(action)
    }
}