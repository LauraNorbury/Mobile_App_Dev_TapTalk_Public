package com.example.v3_pub

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.ComposeView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

//This Screen is just filler content for the app. I implemented it to show off what i learned about
// Jetpack Compose and to give an example of using composables



//With composbales you create them in below the on create and then call them in it the
// onCreate to inflate them
class AboutCityScreen : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                CityInfoPage()
            }
        }
    }


    //heres my composbale that i made. Its very basic just some modifers to do the formatting of
    //images and text
    @Composable
    fun CityInfoPage() {
        Surface(color = MaterialTheme.colorScheme.background) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF06164C)) // Using the same dark navy blue as the background. Composables use the andorid colour definitons
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "About Waterford",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text(
                    text = "The Vikings knew a good thing when they saw it: a sheltered bay, a strategic location on the River Suir, and a good trading position all attracted the Norse raiders here to create Ireland’s first city. And 1,100 years later, it’s still going strong.",
                    color = Color.White,
                    textAlign = TextAlign.Justify,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Image(
                    painter = painterResource(id = R.drawable.img_waterford),
                    contentDescription = "Image for Waterford",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp) //  image height because it was going craxy without it
                        .padding(bottom = 16.dp)
                )

                Text(
                    text = "Waterford is home to a diverse range of music venues, from traditional Irish pubs to modern nightclubs. Whether you’re looking to dance the night away to the latest chart-toppers, enjoy a pint of Guinness while listening to traditional Irish music, or catch a live gig by up-and-coming local bands, Waterford has something for everyone.",
                    color = Color.White,
                    textAlign = TextAlign.Justify
                )
            }
        }
    }
}
