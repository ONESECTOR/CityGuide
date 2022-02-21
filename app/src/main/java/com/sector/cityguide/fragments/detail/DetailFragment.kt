package com.sector.cityguide.fragments.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sector.cityguide.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<DetailFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadPlaceDetails()
    }

    private fun getPlaceId(): String {
        return args.currentPlace.id.toString()
    }

    private fun loadPlaceDetails() {
        val ref = FirebaseDatabase.getInstance().getReference("Places")
        ref.child(getPlaceId())
            .addListenerForSingleValueEvent(object: ValueEventListener {
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