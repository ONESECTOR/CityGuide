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
import com.sector.cityguide.fragments.home.adapters.PlaceAdapter
import com.sector.cityguide.fragments.home.adapters.PopularAdapter
import com.sector.cityguide.models.Place

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var refPlace: DatabaseReference? = null
    private var refPopular: DatabaseReference? = null
    private lateinit var placeAdapter: PlaceAdapter
    private lateinit var popularAdapter: PopularAdapter

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
        placeAdapter = PlaceAdapter()

        binding.rvPlaces.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )

        binding.rvPlaces.adapter = placeAdapter
    }

    private fun setupRecommendations() {
        popularAdapter = PopularAdapter()

        binding.rvPopular.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )

        binding.rvPopular.adapter = popularAdapter
    }

    private fun getRefForPlaces() {
        FirebaseApp.initializeApp(requireContext())
        refPlace = FirebaseDatabase.getInstance().getReference("Places")
    }

    private fun getRefForRecommendations() {
        FirebaseApp.initializeApp(requireContext())
        refPopular = FirebaseDatabase.getInstance().getReference("Recommendations")
    }

    private fun readFromDatabase() {
        refPlace?.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val list = ArrayList<Place>()

                    for (placeSnapshot in snapshot.children) {
                        val place = placeSnapshot.getValue(Place::class.java)

                        list.add(place!!)
                    }

                    placeAdapter.submitList(list)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        refPopular?.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val list = ArrayList<Place>()

                    for (placeSnapshot in snapshot.children) {
                        val place = placeSnapshot.getValue(Place::class.java)

                        list.add(place!!)
                    }

                    popularAdapter.submitList(list)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}