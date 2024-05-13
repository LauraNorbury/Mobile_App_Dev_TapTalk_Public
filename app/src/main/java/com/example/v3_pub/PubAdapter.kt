package com.example.v3_pub

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

//this is an adapter class. i am using it to handle interactions wuth pubs. It will later be used
//by the view model
//a full explanation is in my document


class PubAdapter(
    private val context: Context,
    private val pubList: List<Pub>,
    private val listener: OnPubClickListener
) : BaseAdapter() {

    interface OnPubClickListener {
        fun onPubClick(pub: Pub, pubId: String)
        fun onFavoriteClick(pub: Pub)
    }

    override fun getCount(): Int = pubList.size

    override fun getItem(position: Int): Any = pubList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.pub_item, parent, false)

        val pubImage = view.findViewById<ImageView>(R.id.pub_image)
        val pubName = view.findViewById<TextView>(R.id.pub_name)
        val favoriteIcon = view.findViewById<ImageView>(R.id.favorite_icon)

        // Set data
        val pub = getItem(position) as Pub
        val pubId = "pub${position + 1}"
        pubName.text = pub.name


        //all the handling of clicks etc

        view.setOnClickListener {
            listener.onPubClick(pub, pubId)
        }

        favoriteIcon.setImageResource(
            if (pub.isFavourite) R.drawable.ic_favourite else R.drawable.ic_favourite_border
        )

        favoriteIcon.setOnClickListener {
            pub.isFavourite = !pub.isFavourite
            listener.onFavoriteClick(pub)
            notifyDataSetChanged()
        }

        Glide.with(context)
            .load(pub.image)
            .error(R.drawable.error_placeholder)
            .into(pubImage)

        return view
    }
}
