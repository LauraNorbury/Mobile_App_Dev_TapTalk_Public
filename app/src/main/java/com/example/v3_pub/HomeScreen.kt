package com.example.v3_pub

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

//this is my home screen
//It has the cards which have buttons to navigate to other places but it doesnt do anything other than that

class HomeScreen : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        //  card buttons
        val card1Button: Button = view.findViewById(R.id.card_1_button)
        val card2Button: Button = view.findViewById(R.id.card_2_button)
        val card3Button: Button = view.findViewById(R.id.card_3_button)
        val card4Button: Button = view.findViewById(R.id.card_4_button)

        //click listeners for each button


        card1Button.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_pubs)
        }

        card2Button.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_aboutCityScreen)
        }
        card3Button.setOnClickListener {

            findNavController().navigate(R.id.action_homeFragment_to_recipeListFragment)
        }
        card4Button.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_maps)
        }

        return view
    }
}
