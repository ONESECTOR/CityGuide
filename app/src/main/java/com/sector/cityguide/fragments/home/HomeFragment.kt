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
import com.sector.cityguide.models.Place

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var ref: DatabaseReference? = null
    private lateinit var adapter: HomeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        setupRecyclerView()
        initDatabase()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        readFromDatabase()
    }

    private fun setupRecyclerView() {
        adapter = HomeAdapter()
        binding.rvPlaces.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPlaces.adapter = adapter
    }

    private fun initDatabase() {
        FirebaseApp.initializeApp(requireContext())

        ref = FirebaseDatabase.getInstance().getReference("Places")
    }

    private fun readFromDatabase() {
        ref?.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val list = ArrayList<Place>()

                    for (placeSnapshot in snapshot.children) {
                        val place = placeSnapshot.getValue(Place::class.java)

                        list.add(place!!)
                    }

                    adapter.submitList(list)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}