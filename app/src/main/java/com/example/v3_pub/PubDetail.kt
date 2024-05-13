package com.example.v3_pub

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth


//this is the most complex class in the whole app. I am interacting with two sets of data, pubs and
// reviews
// full details on how this is all peiced together is in my document

class PubDetail : Fragment() {

    //setting up

    private lateinit var reviewViewModel: ReviewViewModel
    private lateinit var reviewsRecyclerView: RecyclerView
    private lateinit var reviewAdapter: ReviewAdapter
    private var pubId: String? = null
    private var isAscending = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pub_detail, container, false)

        val pubImage = view.findViewById<ImageView>(R.id.detail_pub_image)
        val pubName = view.findViewById<TextView>(R.id.detail_pub_name)
        val pubLocation = view.findViewById<TextView>(R.id.detail_pub_location)
        val pubBio = view.findViewById<TextView>(R.id.detail_pub_bio)
        val pubAddress = view.findViewById<TextView>(R.id.detail_pub_address)

        reviewsRecyclerView = view.findViewById(R.id.reviews_recycler_view)
        reviewsRecyclerView.layoutManager = LinearLayoutManager(context)

        // Add divider decoration
        val dividerItemDecoration = DividerItemDecoration(
            reviewsRecyclerView.context, (reviewsRecyclerView.layoutManager as LinearLayoutManager).orientation
        )
        reviewsRecyclerView.addItemDecoration(dividerItemDecoration)

        // Retrieve the current user's ID
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid ?: ""

        //these arguments have to match exactly whats in the Pub dataclass
        val args = arguments
        if (args != null) {
            pubId = args.getString("pubId")
            val imageUrl = args.getString("imageUrl")
            val name = args.getString("name")
            val location = args.getString("location")
            val bio = args.getString("bio")
            val address = args.getString("address")  // Retrieve address

            //using the glide library for the image

            Glide.with(this).load(imageUrl).into(pubImage)
            pubName.text = name
            pubLocation.text = location
            pubBio.text = bio
            pubAddress.text = address  // Set the address text

            // Initialize ReviewAdapter with an empty list to start
            reviewAdapter = ReviewAdapter(requireContext(), emptyList(), pubId ?: "", userId)
            reviewsRecyclerView.adapter = reviewAdapter

            // Set up the ReviewViewModel to check reviews for the current pub
            reviewViewModel = ViewModelProvider(this).get(ReviewViewModel::class.java)
            reviewViewModel.getReviewsForPub(pubId ?: "").observe(viewLifecycleOwner) { reviewsWithUser ->
                // Update the adapter with new reviews
                reviewAdapter.updateReviews(reviewsWithUser)
            }

            // new revew
            view.findViewById<Button>(R.id.button_add_review).setOnClickListener {
                val bundle = Bundle()
                bundle.putString("pubId", pubId)

                findNavController().navigate(R.id.addReviewFragment, bundle)
            }

            // Set up toggle sort button
            view.findViewById<Button>(R.id.toggle_sort_button).setOnClickListener {
                isAscending = !isAscending
                reviewAdapter.sortReviewsByTimestamp(isAscending)
            }
        }

        return view
    }
}
