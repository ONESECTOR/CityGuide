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
import com.google.firebase.database.*
import com.sector.cityguide.R
import com.sector.cityguide.databinding.FragmentAuthSecondStepBinding
import java.util.*

class AuthSecondStepFragment : Fragment() {
    private var _binding: FragmentAuthSecondStepBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<AuthSecondStepFragmentArgs>()

    private lateinit var auth: FirebaseAuth
    private lateinit var listener: ValueEventListener
    private lateinit var reference: DatabaseReference

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

    override fun onPause() {
        super.onPause()

        reference.removeEventListener(listener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
                    //Log.d("MyTag", "signInWithCredential:success")
                    // Success
                    checkUserExist()
                }
            }
    }

    private fun saveUser() {
        val hashMap: HashMap<String, Any?> = HashMap()
        hashMap["uid"] = auth.uid
        hashMap["phone"] = getPhoneNumber()

        val ref = FirebaseDatabase.getInstance()
            .getReference("Users")
            .child(auth.uid!!)
            .setValue(hashMap)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Saved!", Toast.LENGTH_SHORT).show()
                // User successfully saved
                findNavController().navigate(
                    R.id.action_authSecondStepFragment_to_authCompletedFragment
                )
            }
            .addOnFailureListener {
                // Failure
                Toast.makeText(requireContext(), "Failure!", Toast.LENGTH_SHORT).show()
            }
    }

    private fun checkUserExist() {
        reference = FirebaseDatabase.getInstance().getReference("Users").child(auth.uid!!)

        listener = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // User exist
                    findNavController().navigate(
                        R.id.action_authSecondStepFragment_to_authCompletedFragment
                    )
                } else {
                    // User not exist
                    reference.removeEventListener(listener)
                    saveUser()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                throw error.toException()
            }
        }

        reference.addValueEventListener(listener)
    }
}