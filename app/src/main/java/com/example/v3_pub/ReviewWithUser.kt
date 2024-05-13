package com.example.v3_pub

//i had to put this class in because without it, the UIDs were showing up on the Reviews
//i call this instead of review to display the correct review with the user name


data class ReviewWithUser(
    val review: Review,
    val userName: String
)
