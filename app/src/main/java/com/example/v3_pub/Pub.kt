package com.example.v3_pub

//simple pub data class that matches what is in firebase

data class Pub(
    val name: String,
    val location: String,
    val image: String,
    val Bio: String = "",
    val address: String = "",
    var isFavourite: Boolean = false
) {
    // Empty constructor needed by Firebase
    constructor() : this("", "", "", "", "", false)
}
