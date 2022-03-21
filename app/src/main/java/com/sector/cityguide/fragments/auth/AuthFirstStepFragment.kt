package com.sector.cityguide.fragments.auth

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.sector.cityguide.R
import com.sector.cityguide.databinding.FragmentAuthFirstStepBinding
import java.util.concurrent.TimeUnit

class AuthFirstStepFragment : Fragment() {
    private var _binding: FragmentAuthFirstStepBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    private lateinit var phoneNumber: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthFirstStepBinding.inflate(inflater, container, false)

        initFirebase()
        setCallbacks()
        disableButton()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etPhone.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, size: Int) {
                val editText = binding.etPhone.rawText
                val prefix = "+7"

                phoneNumber = "$prefix$editText".trim()

                if (phoneNumber.length != 12) {
                    disableButton()
                } else {
                    enableButton()
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        binding.btnSendCode.setOnClickListener {
            binding.progressLayout.visibility = View.VISIBLE

            sendCode()
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

    private fun enableButton() {
        binding.apply {
            btnSendCode.isEnabled = true

            btnSendCode.setTextColor(ContextCompat.getColor(
                requireContext(),
                R.color.new_white)
            )

            btnSendCode.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.new_black
                )
            )
        }
    }

    private fun disableButton() {
        binding.apply {
            btnSendCode.isEnabled = false

            btnSendCode.setTextColor(ContextCompat.getColor(
                requireContext(),
                R.color.gray)
            )

            btnSendCode.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.silver
                )
            )
        }
    }

    private fun sendCode() {
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
                // User is already verified
            }

            override fun onVerificationFailed(exception: FirebaseException) {
                // Failed
                throw exception
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                // OTP code is successfully sent
                binding.progressLayout.visibility = View.GONE

                val action = AuthFirstStepFragmentDirections.
                actionAuthFirstStepFragmentToAuthSecondStepFragment(
                    verificationId = verificationId,
                    phoneNumber = phoneNumber
                )
                findNavController().navigate(action)
            }
        }
    }
}