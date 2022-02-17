package com.sector.cityguide.fragments.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*
import com.sector.cityguide.databinding.FragmentHomeBinding
import com.sector.cityguide.fragments.home.adapters.HomeAdapter
import com.sector.cityguide.fragments.home.adapters.RecommendationsAdapter
import com.sector.cityguide.models.Place

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var refPlaces: DatabaseReference? = null
    private var refRecommendations: DatabaseReference? = null
    private lateinit var placesAdapter: HomeAdapter
    private lateinit var recommendationsAdapter: RecommendationsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        setupPlaces()
        setupRecommendations()
        getRefForPlaces()
        getRefForRecommendations()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        readFromDatabase()
    }

    private fun setupPlaces() {
        placesAdapter = HomeAdapter()

        binding.rvPlaces.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )

        binding.rvPlaces.adapter = placesAdapter
    }

    private fun setupRecommendations() {
        recommendationsAdapter = RecommendationsAdapter()

        binding.rvRecommendations.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )

        binding.rvRecommendations.adapter = recommendationsAdapter
    }

    private fun getRefForPlaces() {
        FirebaseApp.initializeApp(requireContext())
        refPlaces = FirebaseDatabase.getInstance().getReference("Places")
    }

    private fun getRefForRecommendations() {
        FirebaseApp.initializeApp(requireContext())
        refRecommendations = FirebaseDatabase.getInstance().getReference("Recommendations")
    }

    private fun readFromDatabase() {
        refPlaces?.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val list = ArrayList<Place>()

                    for (placeSnapshot in snapshot.children) {
                        val place = placeSnapshot.getValue(Place::class.java)

                        list.add(place!!)
                    }

                    placesAdapter.submitList(list)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        refRecommendations?.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val list = ArrayList<Place>()

                    for (placeSnapshot in snapshot.children) {
                        val place = placeSnapshot.getValue(Place::class.java)

                        list.add(place!!)
                    }

                    recommendationsAdapter.submitList(list)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}