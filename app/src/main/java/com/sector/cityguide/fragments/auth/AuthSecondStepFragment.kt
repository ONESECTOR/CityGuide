package com.sector.cityguide.fragments.auth

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.FirebaseDatabase
import com.sector.cityguide.R
import com.sector.cityguide.databinding.FragmentAuthSecondStepBinding
import java.util.*

class AuthSecondStepFragment : Fragment() {
    private var _binding: FragmentAuthSecondStepBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<AuthSecondStepFragmentArgs>()

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthSecondStepBinding.inflate(inflater, container, false)

        initFirebase()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setSpannableString()

        binding.btnConfirm.setOnClickListener {
            verify()
        }
    }

    private fun getVerificationId(): String {
        return args.verificationId
    }

    private fun getPhoneNumber(): String {
        return args.phoneNumber
    }

    private fun verify() {
        val code = binding.etCode.text.toString().trim()
        Log.d("code", code)

        val credential = PhoneAuthProvider.getCredential(getVerificationId(), code)
        signInWithPhoneAuthCredential(credential)
    }

    private fun setSpannableString() {
        val spannableString = SpannableString(resources.getString(R.string.resend_code))

        val spanStyle = StyleSpan(Typeface.BOLD)
        val colorSpan = ForegroundColorSpan(Color.BLACK)
        val clickableSpan = object: ClickableSpan() {
            override fun onClick(p0: View) {
                Toast.makeText(requireContext(), "Clicked!", Toast.LENGTH_SHORT).show()
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = false
            }
        }

        when(Locale.getDefault().language) {
            "en" -> {
                spannableString.setSpan(
                    spanStyle,
                    22,
                    29,
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannableString.setSpan(
                    colorSpan,
                    22,
                    29,
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannableString.setSpan(
                    clickableSpan,
                    22,
                    29,
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            "ru" -> {
                spannableString.setSpan(
                    spanStyle,
                    15,
                    30,
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannableString.setSpan(
                    colorSpan,
                    15,
                    30,
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannableString.setSpan(
                    clickableSpan,
                    15,
                    30,
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
        binding.tvResendCode.text = spannableString
        binding.tvResendCode.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun initFirebase() {
        auth = FirebaseAuth.getInstance()
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Log.d("MyTag", "signInWithCredential:success")
                    saveUser()
                } else {
                    Log.w("MyTag", "signInWithCredential:failure", task.exception)
                }
            }
    }

    private fun saveUser() {
        val uid = auth.uid

        // user data preparation
        val hashMap: HashMap<String, Any?> = HashMap()
        hashMap["uid"] = uid
        hashMap["phone"] = getPhoneNumber()

        // setup user data to database
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(uid!!)
            .setValue(hashMap)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Saved!", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_authSecondStepFragment_to_authCompletedFragment)
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failure!", Toast.LENGTH_SHORT).show()
            }
    }
}