package com.sector.cityguide.fragments.auth

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.TextPaint
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
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

    private lateinit var code: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthSecondStepBinding.inflate(inflater, container, false)

        initFirebase()
        disableButton()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setSpannableString()

        binding.etCode.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                code = binding.etCode.rawText

                if (code.length != 6) {
                    disableButton()
                } else {
                    enableButton()
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        binding.btnConfirm.setOnClickListener {
            binding.progressLayout.visibility = View.VISIBLE
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

    private fun enableButton() {
        binding.apply {
            btnConfirm.isEnabled = true

            btnConfirm.setTextColor(
                ContextCompat.getColor(
                requireContext(),
                R.color.new_white)
            )

            btnConfirm.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.new_black
                )
            )
        }
    }

    private fun disableButton() {
        binding.apply {
            btnConfirm.isEnabled = false

            btnConfirm.setTextColor(
                ContextCompat.getColor(
                requireContext(),
                R.color.gray)
            )

            btnConfirm.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.silver
                )
            )
        }
    }

    private fun getVerificationId(): String {
        return args.verificationId
    }

    private fun getPhoneNumber(): String {
        return args.phoneNumber
    }

    private fun verify() {
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
                    // Success
                    checkUserExist()
                }
            }
    }

    private fun saveUser() {
        val hashMap: HashMap<String, Any?> = HashMap()
        hashMap["uid"] = auth.uid
        hashMap["phone"] = getPhoneNumber()
        hashMap["name"] = ""

        val ref = FirebaseDatabase.getInstance()
            .getReference("Users")
            .child(auth.uid!!)
            .child(0.toString())
            .setValue(hashMap)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Saved!", Toast.LENGTH_SHORT).show()
                // User successfully saved
                binding.progressLayout.visibility = View.GONE

                findNavController().navigate(
                    R.id.action_authSecondStepFragment_to_profileFragment
                )
            }
            .addOnFailureListener {
                // Failure
                Toast.makeText(requireContext(), "Failure!", Toast.LENGTH_SHORT).show()
            }
    }

    private fun checkUserExist() {
        reference = FirebaseDatabase.getInstance()
            .getReference("Users")
            .child(auth.uid!!)

        listener = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // User exist
                    binding.progressLayout.visibility = View.GONE

                    findNavController().navigate(
                        R.id.action_authSecondStepFragment_to_profileFragment
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