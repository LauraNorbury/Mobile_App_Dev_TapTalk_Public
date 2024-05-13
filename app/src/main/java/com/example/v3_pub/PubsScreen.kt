package com.example.v3_pub

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

//displays the list of pubs and the favourite mechanisms in a gridview


class PubsScreen : Fragment(), PubAdapter.OnPubClickListener {
    private lateinit var viewModel: PubViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_pubs, container, false)
        val gridView = rootView.findViewById<GridView>(R.id.gridview_pubs)

        viewModel = ViewModelProvider(this).get(PubViewModel::class.java)
        viewModel.pubs.observe(viewLifecycleOwner) { pubs ->
            gridView.adapter = PubAdapter(requireContext(), pubs, this)
        }

        return rootView
    }


    //these have to match wehats in Pub Data class
    override fun onPubClick(pub: Pub, pubId: String) {
        val bundle = Bundle().apply {
            putString("pubId", pubId)
            putString("imageUrl", pub.image)
            putString("name", pub.name)
            putString("location", pub.location)
            putString("bio", pub.Bio)
            putString("address", pub.address)
        }
        findNavController().navigate(R.id.action_pubs_to_reviews, bundle)
    }


    //favouriting
    override fun onFavoriteClick(pub: Pub) {
        viewModel.updateFavoritePubs()
    }
}

