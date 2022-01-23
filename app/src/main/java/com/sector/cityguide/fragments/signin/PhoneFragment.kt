package com.sector.cityguide.fragments.signin

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.sector.cityguide.R
import com.sector.cityguide.databinding.FragmentPhoneBinding
import java.util.concurrent.TimeUnit

class PhoneFragment : Fragment() {
    private var _binding: FragmentPhoneBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    //private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhoneBinding.inflate(inflater, container, false)

        initFirebase()
        checkUser()
        setCallbacks()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSend.setOnClickListener {
            send()
        }
    }

    private fun initFirebase() {
        auth = FirebaseAuth.getInstance()
    }

    private fun checkUser() {
        val currentUser = auth.currentUser

        if (currentUser != null)
            findNavController().navigate(R.id.action_phoneFragment_to_userFragment)
    }

    private fun send() {
        val phoneNumber = binding.etPhoneNumber.text.toString().trim()

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity())                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun setCallbacks() {
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // If user is already verified.
                Log.d("MyTag", "already verified")
                findNavController().navigate(R.id.action_phoneFragment_to_userFragment)
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

    /*private fun otpSend() {
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {

            }

            override fun onVerificationFailed(e: FirebaseException) {

            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                openConfirmFragment(verificationId)
            }
        }
    }*/

    private fun openConfirmFragment(verificationId: String) {
        val action = PhoneFragmentDirections.actionPhoneFragmentToConfirmPhoneFragment(verificationId = verificationId)
        findNavController().navigate(action)
    }
}