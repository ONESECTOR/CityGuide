package com.sector.cityguide.fragments.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.sector.cityguide.R
import com.sector.cityguide.databinding.FragmentProfileBinding
import com.sector.cityguide.fragments.profile.adapters.ProfileAdapter
import com.sector.cityguide.models.Place
import com.sector.cityguide.models.Profile

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    private var nameReference: DatabaseReference? = null
    private var phoneReference: DatabaseReference? = null

    private lateinit var nameListener: ValueEventListener
    private lateinit var phoneListener: ValueEventListener
    private lateinit var name: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        initFirebase()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (auth.currentUser != null) {
            binding.button.text = "Выйти"

            checkPhone()
            checkName()
        } else {
            hideLayout()
            binding.button.text = "Войти"
        }

        binding.btnAboutApp.setOnClickListener {
            findNavController().navigate(
                R.id.action_profileFragment_to_aboutAppFragment
            )
        }

        binding.btnChangeName.setOnClickListener {
            findNavController().navigate(
                ProfileFragmentDirections.actionProfileFragmentToEditNameFragment(
                    profileName = name
                )
            )
        }

        binding.button.setOnClickListener {
            if (auth.currentUser != null) {
                auth.signOut()
                hideLayout()

                binding.button.text = "Войти"
            } else {
                findNavController().navigate(R.id.action_profileFragment_to_authFirstStepFragment)
            }
        }
    }

    override fun onPause() {
        super.onPause()

        nameReference?.removeEventListener(nameListener)
        phoneReference?.removeEventListener(phoneListener)
    }

    private fun initFirebase() {
        FirebaseApp.initializeApp(requireContext())
        auth = FirebaseAuth.getInstance()
    }

    private fun hideLayout() {
        binding.apply {
            divider.visibility = View.GONE
            tvPhoneNumber.visibility = View.GONE
            tvAdditionally.visibility = View.GONE
        }
    }

    private fun checkPhone() {
        phoneReference = FirebaseDatabase.getInstance()
            .getReference("Users")
            .child(auth.uid!!)
            .child(0.toString())
            .child("phone")

        phoneListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val phone = snapshot.value.toString()

                    phone.startsWith("+")
                    binding.tvPhoneNumber.text = phone
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }

        phoneReference?.addValueEventListener(phoneListener)
    }

    private fun checkName() {
        nameReference = FirebaseDatabase.getInstance()
            .getReference("Users")
            .child(auth.uid!!)
            .child(0.toString())
            .child("name")

        nameListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                name = snapshot.value.toString()

                if (name.isEmpty()) {
                    binding.tvTitleName.text = "Введите имя"
                } else {
                    binding.tvTitleName.text = name
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }

        nameReference?.addValueEventListener(nameListener)
    }
}