package com.example.v3_pub

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ReviewAdapter(
    private val context: Context,
    private var reviewList: List<ReviewWithUser>,
    private val pubId: String, // Pass pubId to the adapter
    private val userId: String
) : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    fun updateReviews(newReviews: List<ReviewWithUser>) {
        reviewList = newReviews
        notifyDataSetChanged() // Notify the adapter of data changes
    }

    fun sortReviewsByTimestamp(isAscending: Boolean) {
        reviewList = if (isAscending) {
            reviewList.sortedBy { parseTimestamp(it.review.timestamp) }
        } else {
            reviewList.sortedByDescending { parseTimestamp(it.review.timestamp) }
        }
        notifyDataSetChanged()
    }

    private fun parseTimestamp(timestamp: String): Date {
        return try {
            // Assuming the timestamp is in Unix epoch time (milliseconds since 1970-01-01)
            Date(timestamp.toLong())
        } catch (e: NumberFormatException) {
            try {
                // Fallback to ISO 8601 format
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).parse(timestamp)!!
            } catch (e: ParseException) {
                // Handle parsing failure, return a default date or throw an exception
                Date(0) // Default to Unix epoch start
            }
        }
    }

    class ReviewViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userName: TextView = view.findViewById(R.id.review_user_name)
        val comment: TextView = view.findViewById(R.id.review_comment)
        val timestamp: TextView = view.findViewById(R.id.review_timestamp)
        val rating: TextView = view.findViewById(R.id.review_rating)
        val editButton: Button = view.findViewById(R.id.button_edit_review)
        val deleteButton: Button = view.findViewById(R.id.button_delete_review)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.review_item, parent, false)
        return ReviewViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val reviewWithUser = reviewList[position]
        val review = reviewWithUser.review

        holder.userName.text = " ${reviewWithUser.userName}"
        holder.comment.text = "${review.comment}"
        holder.timestamp.text = " ${review.timestamp}"
        holder.rating.text = "Rating: ${review.rating}/5"

        // Only allow editing/deletion by the original author
        if (review.userId == userId) {
            holder.editButton.visibility = View.VISIBLE
            holder.deleteButton.visibility = View.VISIBLE

            // Handle edit button click
            holder.editButton.setOnClickListener {
                // Create a bundle with the review details
                val bundle = Bundle().apply {
                    putString("reviewId", review.id)
                    putString("pubId", pubId)
                    putString("comment", review.comment)
                    putInt("rating", review.rating)
                }

                // Navigate to the EditScreen using the view's NavController
                holder.itemView.findNavController()
                    .navigate(R.id.action_pubDetailFragment_to_editScreen, bundle)
            }

            // Handle delete button click
            holder.deleteButton.setOnClickListener {
                val db = FirebaseDatabase.getInstance("https://reviewpub-75274-default-rtdb.europe-west1.firebasedatabase.app")
                val reviewRef = db.getReference("pubs/reviews/$pubId/${review.id}")
                reviewRef.removeValue().addOnSuccessListener {
                    Toast.makeText(context, "Review deleted", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(context, "Failed to delete review", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            holder.editButton.visibility = View.GONE
            holder.deleteButton.visibility = View.GONE
        }
    }

    override fun getItemCount() = reviewList.size
}
