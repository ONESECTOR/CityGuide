package com.sector.cityguide.fragments.favorite

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.sector.cityguide.databinding.FragmentFavoriteBinding
import com.sector.cityguide.fragments.favorite.adapter.FavoriteAdapter
import com.sector.cityguide.models.Place

class FavoriteFragment : Fragment() {
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private lateinit var favoriteAdapter: FavoriteAdapter
    private lateinit var auth: FirebaseAuth

    private var favoriteReference: DatabaseReference? = null
    private lateinit var favoriteListener: ValueEventListener

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
                checkFavorites()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPause() {
        super.onPause()
        favoriteReference?.removeEventListener(favoriteListener)
    }

    private fun initFirebase() {
        auth = FirebaseAuth.getInstance()
    }

    private fun setupRecyclerView() {
        favoriteAdapter = FavoriteAdapter()

        binding.rvFavorite.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFavorite.adapter = favoriteAdapter
    }

    private fun getProfileUid() = auth.uid!!

    private fun loadFavorites() {
        val reference = FirebaseDatabase.getInstance().getReference("Users")
            .child(getProfileUid())
            .child("Favorites")

        reference.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val favoritePlaces = mutableListOf<Place>()

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

    private fun checkFavorites() {
        favoriteReference = FirebaseDatabase.getInstance()
            .getReference("Users")
            .child(getProfileUid())
            .child("Favorites")

        favoriteListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    setupRecyclerView()
                    loadFavorites()

                    binding.shimmer.stopShimmer()
                    binding.shimmer.visibility = View.GONE
                } else {
                    binding.shimmer.stopShimmer()
                    binding.shimmer.visibility = View.GONE
                    binding.layoutNoFavorite.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                throw error.toException()
            }
        }

        favoriteReference?.addValueEventListener(favoriteListener)
    }
}