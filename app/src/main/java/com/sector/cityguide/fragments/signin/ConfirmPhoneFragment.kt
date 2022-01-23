package com.sector.cityguide.fragments.signin

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.sector.cityguide.R
import com.sector.cityguide.databinding.FragmentConfirmPhoneBinding

class ConfirmPhoneFragment : Fragment() {
    private var _binding: FragmentConfirmPhoneBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<ConfirmPhoneFragmentArgs>()

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConfirmPhoneBinding.inflate(inflater, container, false)

        initFirebase()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Log.d("MyTag", getPhoneNumber())
        binding.btnVerify.setOnClickListener {
            verify()
        }
    }

    private fun getVerificationId(): String {
        return args.verificationId
    }

    private fun verify() {
        val code = binding.etCode.text.toString().trim()

        val credential = PhoneAuthProvider.getCredential(getVerificationId(), code)
        signInWithPhoneAuthCredential(credential)
    }

    private fun initFirebase() {
        auth = FirebaseAuth.getInstance()
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("MyTag", "signInWithCredential:success")
                    findNavController().navigate(R.id.action_confirmPhoneFragment_to_userFragment)
                    //val user = task.result?.user
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w("MyTag", "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
                }
            }
    }
}