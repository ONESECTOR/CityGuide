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
    private lateinit var profileAdapter: ProfileAdapter

    private var nameReference: DatabaseReference? = null
    private var aboutAppReference: DatabaseReference? = null

    private lateinit var nameListener: ValueEventListener
    private lateinit var aboutAppListener: ValueEventListener

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

            setupRecyclerView()
            checkName()
            checkPhone()
        } else {
            binding.rvProfile.visibility = View.GONE
            binding.divider.visibility = View.GONE
            binding.tvPhoneNumber.visibility = View.GONE
            binding.tvAdditionally.visibility = View.GONE
            binding.button.text = "Войти"
        }

        binding.btnAboutApp.setOnClickListener {
            findNavController().navigate(
                R.id.action_profileFragment_to_aboutAppFragment
            )
        }

        binding.button.setOnClickListener {
            if (auth.currentUser != null) {
                auth.signOut()
                binding.rvProfile.visibility = View.GONE
                binding.divider.visibility = View.GONE
                binding.tvPhoneNumber.visibility = View.GONE
                binding.tvAdditionally.visibility = View.GONE
                binding.button.text = "Войти"
            } else {
                findNavController().navigate(R.id.action_profileFragment_to_authFirstStepFragment)
            }
        }
    }

    override fun onPause() {
        super.onPause()

        nameReference?.removeEventListener(nameListener)
        aboutAppReference?.removeEventListener(aboutAppListener)
    }

    override fun onResume() {
        super.onResume()

        nameReference?.addValueEventListener(nameListener)
        aboutAppReference?.addValueEventListener(aboutAppListener)
    }

    private fun initFirebase() {
        FirebaseApp.initializeApp(requireContext())
        auth = FirebaseAuth.getInstance()
    }

    private fun setupRecyclerView() {
        profileAdapter = ProfileAdapter()

        binding.rvProfile.layoutManager = LinearLayoutManager(requireContext())
        binding.rvProfile.adapter = profileAdapter
    }

    private fun checkPhone() {
        aboutAppReference = FirebaseDatabase.getInstance()
            .getReference("Users")
            .child(auth.uid!!)
            .child("Info")
            .child("phone")

        aboutAppListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val phone = snapshot.value.toString()

                    phone.startsWith("+")
                    binding.tvPhoneNumber.text = phone
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
    }

    private fun checkName() {
        nameReference = FirebaseDatabase.getInstance()
            .getReference("Users")
            .child(auth.uid!!)

        // Почему нет .child("Info") ??????

        nameListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val profileList = ArrayList<Profile>()

                    for (ds in snapshot.children) {
                        val title = ds.getValue(Profile::class.java)

                        profileList.add(title!!)
                    }

                    profileAdapter.submitList(profileList)

                    //Log.d("title", snapshot.value.toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                throw error.toException()
            }
        }

        //reference?.addValueEventListener(listener)
    }
}