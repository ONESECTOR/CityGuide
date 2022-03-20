package com.sector.cityguide.fragments.home

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.sector.cityguide.R
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
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

    private lateinit var nameListener: ValueEventListener
    private lateinit var placeListener: ValueEventListener
    private lateinit var popularListener: ValueEventListener
    private lateinit var favoriteListener: ValueEventListener

    private lateinit var greetingMessage: String

    private var nameReference: DatabaseReference? = null
    private var popularReference: DatabaseReference? = null
    private var placeReference: DatabaseReference? = null
    private var favoriteReference: DatabaseReference? = null

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

        getPopular()
        getPlaces()

        val viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        viewModel.greetingMessage().observe(viewLifecycleOwner) { greeting ->
            greetingMessage = resources.getText(greeting).toString()

            if (auth.currentUser == null) {
                binding.tvGreeting.text = greetingMessage
            }
        }

        if (auth.currentUser != null) {
            checkFavorites()
            checkName()
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

        nameReference?.removeEventListener(nameListener)
        placeReference?.removeEventListener(placeListener)
        popularReference?.removeEventListener(popularListener)
        favoriteReference?.removeEventListener(favoriteListener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initFirebase() {
        FirebaseApp.initializeApp(requireContext())
        auth = FirebaseAuth.getInstance()
    }

    private fun checkName() {
        nameReference = FirebaseDatabase.getInstance()
            .getReference("Users")
            .child(auth.uid!!)
            .child(0.toString())
            .child("name")

        nameListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val name = snapshot.value.toString()

                if (name.isEmpty()) {
                    binding.tvGreeting.text = greetingMessage
                } else {
                    binding.tvGreeting.text = "$greetingMessage, $name"
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }

        nameReference?.addValueEventListener(nameListener)
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
        placeReference = FirebaseDatabase.getInstance().getReference("Places")

        placeListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val list = ArrayList<Place>()

                    for (placeSnapshot in snapshot.children) {
                        val place = placeSnapshot.getValue(Place::class.java)

                        list.add(place!!)
                    }

                    placeAdapter.submitList(list)

                    binding.shimmerPlaces.stopShimmer()
                    binding.shimmerPlaces.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                throw error.toException()
            }
        }

        placeReference?.addValueEventListener(placeListener)
    }

    private fun getPopular() {
        popularReference = FirebaseDatabase.getInstance().getReference("Popular")

        popularListener = object : ValueEventListener {
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
                throw error.toException()
            }
        }

        popularReference?.addValueEventListener(popularListener)
    }

    private fun checkFavorites() {
        favoriteReference = FirebaseDatabase.getInstance()
            .getReference("Users")
            .child(getProfileUid())
            .child("Favorites")

        favoriteListener = object : ValueEventListener {
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
        }

        favoriteReference?.addValueEventListener(favoriteListener)
    }
}