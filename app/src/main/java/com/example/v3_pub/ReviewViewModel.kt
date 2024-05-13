package com.example.v3_pub

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

//very simple viewModel just initalises repository.

class ReviewViewModel : ViewModel() {
    private val reviewRepository = ReviewRepository()

    // Update return  to match ReviewWithUser
    fun getReviewsForPub(pubId: String): LiveData<List<ReviewWithUser>> {
        return reviewRepository.getReviewsForPub(pubId)
    }
}
