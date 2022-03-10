package com.sector.cityguide.fragments.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.forEach
import androidx.lifecycle.ViewModelProvider
import com.sector.cityguide.R
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.sector.cityguide.databinding.FragmentHomeBinding
import com.sector.cityguide.fragments.home.adapters.PlaceAdapter
import com.sector.cityguide.fragments.home.adapters.PopularAdapter
import com.sector.cityguide.fragments.home.viewmodel.HomeViewModel
import com.sector.cityguide.models.Place
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var placeAdapter: PlaceAdapter
    private lateinit var popularAdapter: PopularAdapter

    private lateinit var listener: ValueEventListener

    private val placesList = ArrayList<Place>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        initFirebase()
        setupPlacesAdapter()
        setupPopularAdapter()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        deactivateNavigationView()
        getPopular()
        getPlaces()

        if (auth.currentUser != null) {
            checkFavorites()
        }

        val viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        viewModel.greetingMessage().observe(viewLifecycleOwner) { greeting ->
            binding.tvGreeting.text = resources.getText(greeting)
        }

        binding.btnSearch.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        binding.shimmerPlaces.startShimmer()
        binding.shimmerPopular.startShimmer()
    }

    override fun onPause() {
        super.onPause()
        binding.shimmerPlaces.stopShimmer()
        binding.shimmerPopular.stopShimmer()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initFirebase() {
        FirebaseApp.initializeApp(requireContext())
        auth = FirebaseAuth.getInstance()
    }

    private fun getProfileUid(): String {
        return auth.uid!!
    }

    private fun setupPlacesAdapter() {
        placeAdapter = PlaceAdapter()

        binding.rvPlaces.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )

        binding.rvPlaces.adapter = placeAdapter
    }

    private fun setupPopularAdapter() {
        popularAdapter = PopularAdapter()

        binding.rvPopular.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )

        binding.rvPopular.adapter = popularAdapter
    }

    private fun getPlaces() {
        val reference = FirebaseDatabase.getInstance().getReference("Places")
        placesList.clear()

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (placeSnapshot in snapshot.children) {
                        val place = placeSnapshot.getValue(Place::class.java)

                        placesList.add(place!!)
                    }

                    placeAdapter.submitList(placesList)

                    binding.shimmerPlaces.stopShimmer()
                    binding.shimmerPlaces.visibility = View.GONE

                    activateNavigationView()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun getPopular() {
        val reference = FirebaseDatabase.getInstance().getReference("Popular")

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val list = ArrayList<Place>()

                    for (placeSnapshot in snapshot.children) {
                        val place = placeSnapshot.getValue(Place::class.java)

                        list.add(place!!)
                    }

                    popularAdapter.submitList(list)

                    binding.shimmerPopular.stopShimmer()
                    binding.shimmerPopular.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun checkFavorites() {
        val ref = FirebaseDatabase.getInstance().getReference("Users")

        ref.child(getProfileUid())
            .child("Favorites")
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val prefs = requireActivity().getSharedPreferences("favorites", Context.MODE_PRIVATE)
                        val editor = prefs.edit()
                        editor.putBoolean("exist", true)
                        editor.apply()
                    } else {
                        val prefs = requireActivity().getSharedPreferences("favorites", Context.MODE_PRIVATE)
                        val editor = prefs.edit()
                        editor.putBoolean("exist", false)
                        editor.apply()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    throw error.toException()
                }
            })
    }

    private fun deactivateNavigationView() {
        val navigationBar = activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        navigationBar?.menu?.forEach {
            it.isEnabled = false
        }
    }

    private fun activateNavigationView() {
        val navigationBar = activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        navigationBar?.menu?.forEach {
            it.isEnabled = true
        }
    }
}