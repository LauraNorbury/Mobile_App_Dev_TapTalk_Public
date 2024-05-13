package com.example.v3_pub

// simple data class for the reviews

data class Review(
    val id: String = "",
    val comment: String = "",
    val rating: Int = 0,
    val timestamp: String = "",
    val userId: String = ""
)