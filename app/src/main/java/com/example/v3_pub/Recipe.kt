package com.example.v3_pub


//GSON class. declares the format for the recipes. the @SerialisedName is the GSON library
// it is used to specifty the field name for serialisation an ddeserialisation

import com.google.gson.annotations.SerializedName

data class Recipe(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("ingredients") val ingredients: String,
    @SerializedName("instructions") val instructions: String
)
