package com.sector.cityguide.fragments.detail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sector.cityguide.R
import com.sector.cityguide.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<DetailFragmentArgs>()

    private lateinit var auth: FirebaseAuth
    private var isInFavorite = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initFirebase()
        loadPlaceDetails()

        if (auth.currentUser != null) {
            checkIsInFavorite()
        }

        binding.btnBack.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.btnAddToFavorite.setOnClickListener {
            if (auth.currentUser == null) {
                val message = resources.getString(R.string.sign_in_to_add_favorites)
                Snackbar.make(binding.btnAddToFavorite, message, Snackbar.LENGTH_SHORT).show()
            } else {
                if (isInFavorite) {
                    removeFromFavorite()
                } else {
                    addToFavorite()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.shimmer.startShimmer()
    }

    override fun onPause() {
        super.onPause()
        binding.shimmer.stopShimmer()
    }

    private fun addToFavorite() {
        val hashMap = HashMap<String, Any>()

        with(args) {
            hashMap["id"] = getPlaceId()
            hashMap["name"] = currentPlace.name
            hashMap["location"] = currentPlace.description
            hashMap["image"] = currentPlace.image
            hashMap["description"] = currentPlace.description
        }

        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(getProfileUid()!!)
            .child("Favorites")
            .child(getPlaceId())
            .setValue(hashMap)
            .addOnSuccessListener {
                val message = resources.getString(R.string.message_add_to_favorite)
                Snackbar.make(binding.btnAddToFavorite, message, Snackbar.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                val message = resources.getString(R.string.error)
                Snackbar.make(binding.btnAddToFavorite, message, Snackbar.LENGTH_SHORT).show()
            }
    }

    private fun checkIsInFavorite() {
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(getProfileUid()!!)
            .child("Favorites")
            .child(getPlaceId())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    isInFavorite = snapshot.exists()

                    if (isInFavorite) {
                        binding.btnAddToFavorite.setImageResource(R.drawable.ic_filled_favorite_red)
                    } else {
                        binding.btnAddToFavorite.setImageResource(R.drawable.ic_outline_favorite)
                    }

                    binding.shimmer.stopShimmer()

                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("db_error", error.toString())
                    binding.btnAddToFavorite.setImageResource(R.drawable.ic_outline_favorite)
                }
            })
    }

    private fun removeFromFavorite() {
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(getProfileUid()!!)
            .child("Favorites")
            .child(getPlaceId())
            .removeValue()
            .addOnSuccessListener {
                val message = resources.getString(R.string.message_remove_from_favorite)
                Snackbar.make(binding.btnAddToFavorite, message, Snackbar.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                val message = resources.getString(R.string.error)
                Snackbar.make(binding.btnAddToFavorite, message, Snackbar.LENGTH_SHORT).show()
            }
    }

    private fun getPlaceId(): String {
        return args.currentPlace.id
    }

    private fun initFirebase() {
        auth = FirebaseAuth.getInstance()
    }

    private fun getProfileUid() = auth.uid

    private fun loadPlaceDetails() {
        val ref = FirebaseDatabase.getInstance().getReference("Places")
        ref.child(getPlaceId())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val title = snapshot.child("name").value.toString()
                    val location = snapshot.child("location").value.toString()
                    val image = snapshot.child("image").value.toString()

                    setLayout(
                        title = title,
                        location = location,
                        image = image
                    )
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), "Failure", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun setLayout(title: String, location: String, image: String) {
        binding.apply {
            tvTitle.text = title
            tvLocation.text = location
            Glide.with(requireContext())
                .load(image)
                .centerCrop()
                .into(ivPlace)
        }
    }
}