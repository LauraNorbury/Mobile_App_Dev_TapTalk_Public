package com.example.v3_pub

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ReviewRepository {
    private val db = FirebaseDatabase.getInstance("https://reviewpub-75274-default-rtdb.europe-west1.firebasedatabase.app")

    fun getReviewsForPub(pubId: String): LiveData<List<ReviewWithUser>> {
        val liveData = MutableLiveData<List<ReviewWithUser>>()
        val reviewsRef = db.getReference("pubs/reviews/$pubId")
        val usersRef = db.getReference("users")
        val reviewsWithUser = mutableListOf<ReviewWithUser>()
        val userCache = mutableMapOf<String, String>()

        reviewsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                reviewsWithUser.clear() // Ensure it's clear each time used

                snapshot.children.forEach { child ->
                    val review = child.getValue(Review::class.java)
                    val userId = review?.userId ?: ""

                    //obtain the user name locally
                    var cachedUserName: String? = userCache[userId]

                    if (cachedUserName != null) {
                        // Add the review to the list if the user name is already cached
                        reviewsWithUser.add(ReviewWithUser(review!!, cachedUserName))
                        liveData.postValue(reviewsWithUser)
                    } else {
                        //fetch the user name from the database if it's not cached
                        usersRef.child(userId).get().addOnSuccessListener { userSnapshot ->
                            cachedUserName = userSnapshot.child("name").getValue(String::class.java) ?: "Unknown"
                            userCache[userId] = cachedUserName!!
                            reviewsWithUser.add(ReviewWithUser(review!!, cachedUserName!!))
                            liveData.postValue(reviewsWithUser)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ReviewRepository", "Error fetching reviews: ${error.message}")
            }
        })

        return liveData
    }
}
