package com.example.v3_pub

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

//this is the screen i use for editing the reviews. It used firbase to get the reviews

class EditScreen : Fragment() {
    private lateinit var commentEditText: EditText
    private lateinit var ratingEditText: EditText
    private lateinit var updateButton: Button
    private var reviewId: String = ""
    private var pubId: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_screen, container, false)


        commentEditText = view.findViewById(R.id.edit_text_comment)
        ratingEditText = view.findViewById(R.id.edit_text_rating)
        updateButton = view.findViewById(R.id.button_update_review)

        // Retrieve review and pub ID
        val args = arguments
        if (args != null) {
            reviewId = args.getString("reviewId") ?: ""
            pubId = args.getString("pubId") ?: ""
            val comment = args.getString("comment")
            val rating = args.getInt("rating", 0)


            commentEditText.setText(comment)
            ratingEditText.setText(rating.toString())
        }

        updateButton.setOnClickListener {
            val updatedComment = commentEditText.text.toString().trim()
            val updatedRating = ratingEditText.text.toString().toIntOrNull() ?: 0

            // Verify IDs, logging method i used when this wasnt working
            println("Attempting to update review with reviewId: $reviewId, pubId: $pubId")

            //this is the actual update of the node itslef
            //check things have updated
            if (updatedComment.isNotEmpty() && updatedRating in 1..5) {
                val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "unknown"

                val updatedReview = mapOf(
                    "id" to reviewId,
                    "pubId" to pubId,
                    "comment" to updatedComment,
                    "rating" to updatedRating,
                    "userId" to userId
                )

                // debugging -  print the updated review
                println("Updating review: $updatedReview")


                // get the firenase reference and the corret node
                val reviewRef = FirebaseDatabase.getInstance("https://reviewpub-75274-default-rtdb.europe-west1.firebasedatabase.app")
                    .getReference("pubs/reviews/$pubId/$reviewId")

                // this is all debugging because it took a while to actually work
                reviewRef.setValue(updatedReview).addOnSuccessListener {
                    println("Review replaced successfully")
                    Toast.makeText(context, "Review replaced successfully", Toast.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                }.addOnFailureListener {
                    println("Failed to replace review")
                    Toast.makeText(context, "Failed to replace review", Toast.LENGTH_SHORT).show()
                }
            } else {
                println("Invalid input")
                Toast.makeText(context, "Please enter a valid rating between 1 and 5", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}