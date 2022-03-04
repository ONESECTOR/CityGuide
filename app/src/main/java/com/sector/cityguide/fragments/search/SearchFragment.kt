package com.sector.cityguide.fragments.search

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sector.cityguide.databinding.FragmentSearchBinding
import com.sector.cityguide.fragments.search.adapter.SearchAdapter
import com.sector.cityguide.models.Place
import kotlin.collections.ArrayList

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() =_binding!!

    private lateinit var searchAdapter: SearchAdapter

    private val placesList = ArrayList<Place>()
    private var filteredList = ArrayList<Place>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        initFirebase()
        setupRecyclerView()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getPlaces()

        Log.d("Test", "Всего мест: ${placesList.size}")
        Log.d("Test", "Отфильтрованных: ${filteredList.size}")

        binding.btnBack.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(!TextUtils.isEmpty(newText)) {
                    binding.layoutStartSearching.visibility = View.GONE
                    setupRecyclerView()
                    filter(newText!!)
                } else {
                    Log.d("Test", "Ищите среди сотен интересных мест!")
                    Log.d("Test", "Список отфильтрованных мест очищен")
                    filteredList.clear()
                    binding.layoutStartSearching.visibility = View.VISIBLE
                    binding.layoutNoPlaces.visibility = View.GONE
                }

                return false
            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initFirebase() {
        FirebaseApp.initializeApp(requireContext())
    }

    private fun getPlaces() {
        if (placesList.isEmpty()) {
            val reference = FirebaseDatabase.getInstance().getReference("Places")

            reference.addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (placeSnapshot in snapshot.children) {
                            val place = placeSnapshot.getValue(Place::class.java)

                            placesList.add(place!!)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        }
    }

    private fun setupRecyclerView() {
        searchAdapter = SearchAdapter()

        binding.rvFoundPlaces.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFoundPlaces.adapter = searchAdapter
    }

    private fun filter(query: String) {
        Log.d("Test", query)
        Log.d("Test", "filter()")
        val filteredListTemp = ArrayList<Place>()

        for (item in placesList) {
            val filteredName = item.name.lowercase()
            val filteredRegion = item.location.lowercase()

            if (filteredName.contains(query) || filteredRegion.contains(query)) {
                filteredListTemp.add(item)
            }
        }

        filteredList = filteredListTemp
        searchAdapter.submitList(filteredList)

        if (filteredList.isEmpty()) {
            Log.d("Test", "Ничего не найдено")
            binding.layoutNoPlaces.visibility = View.VISIBLE
        }

        Log.d("Test", "Всего мест: ${placesList.size}")
        Log.d("Test", "Отфильтрованных: ${filteredList.size}")
    }
}