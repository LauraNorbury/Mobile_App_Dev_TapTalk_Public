package com.example.v3_pub

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.*

//This screen uses firebase. It saves a review into my realtime database

class AddReviewScreen : Fragment() {

    //some initial set up

    private lateinit var commentEditText: EditText
    private lateinit var ratingEditText: EditText
    private lateinit var submitButton: Button
    private val db = FirebaseDatabase.getInstance("https://reviewpub-75274-default-rtdb.europe-west1.firebasedatabase.app")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_review, container, false)
        commentEditText = view.findViewById(R.id.edit_text_comment)
        ratingEditText = view.findViewById(R.id.edit_text_rating)
        submitButton = view.findViewById(R.id.button_submit_review)

        val pubId = arguments?.getString("pubId") ?: ""

        submitButton.setOnClickListener {
            val comment = commentEditText.text.toString()
            val rating = ratingEditText.text.toString().toIntOrNull() ?: 0

            val user = FirebaseAuth.getInstance().currentUser
            val userId = user?.uid
            val userName = user?.displayName
            val timestamp = Date().time.toString()



            // Create a new Review object with a unique ID, the comment, rating, timestamp, and user ID
            val review = Review(
                id = UUID.randomUUID().toString(),
                comment = comment,
                rating = rating,
                timestamp = timestamp,
                userId = userId ?: "unknown" //use unknown if unknown
            )


            //get the firebasee db reference at the reviews node for the specific pub
            val reviewRef = db.getReference("pubs/reviews/$pubId").push()
            reviewRef.setValue(review)

            findNavController().navigateUp()
        }

        return view
    }
}
