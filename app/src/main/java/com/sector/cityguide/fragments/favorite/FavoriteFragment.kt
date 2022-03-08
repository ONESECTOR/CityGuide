package com.sector.cityguide.fragments.favorite

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sector.cityguide.databinding.FragmentFavoriteBinding
import com.sector.cityguide.fragments.favorite.adapter.FavoriteAdapter
import com.sector.cityguide.models.Place

class FavoriteFragment : Fragment() {
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private lateinit var favoriteAdapter: FavoriteAdapter
    private lateinit var visibility: String
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)

        initFirebase()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            btnBack.setOnClickListener {
                activity?.onBackPressed()
            }

            if (auth.currentUser == null) {
                shimmer.stopShimmer()
                shimmer.visibility = View.GONE
                layoutYouAreNotLoggedIn.visibility = View.VISIBLE
            } else {
                if (favoritesExist()) {
                    setupRecyclerView()
                    loadFavorites()

                    shimmer.stopShimmer()
                    shimmer.visibility = View.GONE
                } else {
                    shimmer.stopShimmer()
                    shimmer.visibility = View.GONE
                    layoutNoFavorite.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        binding.apply {
            shimmer.startShimmer()
        }
    }

    override fun onPause() {
        super.onPause()
        binding.apply {
            shimmer.startShimmer()
        }
    }

    private fun initFirebase() {
        FirebaseApp.initializeApp(requireContext())
        auth = FirebaseAuth.getInstance()
    }

    private fun setupRecyclerView() {
        favoriteAdapter = FavoriteAdapter()

        binding.rvFavorite.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFavorite.adapter = favoriteAdapter
    }

    private fun getProfileUid() = auth.uid

    private fun loadFavorites() {
        val reference = FirebaseDatabase.getInstance().getReference("Users")
            .child(getProfileUid()!!)
            .child("Favorites")

        reference.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val favoritePlaces = ArrayList<Place>()

                    for (ds in snapshot.children) {
                        val place = ds.getValue(Place::class.java)
                        favoritePlaces.add(place!!)
                    }

                    favoriteAdapter.submitList(favoritePlaces)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("db_error", error.toString())
            }
        })
    }

    private fun favoritesExist(): Boolean {
        val prefs = requireActivity().getSharedPreferences("favorites", Context.MODE_PRIVATE)
        Log.d("favorites", prefs.getBoolean("exist", false).toString())
        return prefs.getBoolean("exist", false)
    }
}