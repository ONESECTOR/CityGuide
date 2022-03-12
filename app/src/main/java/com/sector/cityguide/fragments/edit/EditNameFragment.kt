package com.sector.cityguide.fragments.edit

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.navArgs
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.sector.cityguide.R
import com.sector.cityguide.databinding.FragmentEditNameBinding

class EditNameFragment : Fragment() {
    private var _binding: FragmentEditNameBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<EditNameFragmentArgs>()

    private lateinit var auth: FirebaseAuth
    private lateinit var name: String

    private var reference: DatabaseReference? = null
    private var listener: ValueEventListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditNameBinding.inflate(inflater, container, false)

        initFirebase()
        disableButton()
        setExistingName()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etName.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                name = binding.etName.rawText

                if (name.isNotEmpty() || args.profileName.isNotEmpty()) {
                    enableButton()
                } else {
                    disableButton()
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        binding.btnSave.setOnClickListener {
            saveName(name)
        }

        binding.btnBack.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPause() {
        super.onPause()

        reference?.removeEventListener(listener!!)
    }

    private fun initFirebase() {
        FirebaseApp.initializeApp(requireContext())
        auth = FirebaseAuth.getInstance()
    }

    private fun saveName(name: String) {
        reference = FirebaseDatabase.getInstance()
            .getReference("Users")
            .child(auth.uid!!)
            .child(0.toString())

        listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children) {
                    snapshot.ref.child("name")
                        .setValue(name)
                        .addOnSuccessListener {
                            activity?.onBackPressed()
                        }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }

        reference?.addValueEventListener(listener!!)
    }

    private fun setExistingName() {
        val nameFromArgs = args.profileName
        name = nameFromArgs

        binding.etName.setText(nameFromArgs)

        if (name.isNotEmpty()) {
            enableButton()
        } else {
            disableButton()
        }
    }

    private fun enableButton() {
        binding.apply {
            btnSave.isEnabled = true

            btnSave.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.new_white)
            )

            btnSave.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.new_black
                )
            )
        }
    }

    private fun disableButton() {
        binding.apply {
            btnSave.isEnabled = false

            btnSave.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.gray)
            )

            btnSave.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.silver
                )
            )
        }
    }
}