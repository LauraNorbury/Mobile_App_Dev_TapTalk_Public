package com.example.v3_pub

import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.*

//my maps with custom persitance. I fetch the addresses to show on the map from firebase


class MapsScreen : Fragment(), OnMapReadyCallback {


//set up
    private lateinit var mMap: GoogleMap
    private val db = FirebaseDatabase.getInstance("https://reviewpub-75274-default-rtdb.europe-west1.firebasedatabase.app")
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_maps_screen, container, false)
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //find the database and node

        database = db.getReference("pubs/pubs")
        return view
    }


    //these are all the map functions
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Set camera position to Waterford
        val waterfordCity = LatLng(52.2583, -7.1194)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(waterfordCity, 12f))

        loadPubs()
    }


    //load the pubs and c
    private fun loadPubs() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (pubSnapshot in dataSnapshot.children) {
                    val pub = pubSnapshot.getValue(Pub::class.java)
                    pub?.let {
                        val location = getLocationFromAddress(it.address)
                        Log.d("MapsScreen", "Geocoded location for address ${it.address}: $location")
                        location?.let { loc ->
                            val markerOptions = MarkerOptions()
                                .position(loc)
                                .title(it.name)
                                .snippet(it.Bio)
                            mMap.addMarker(markerOptions)
                            //  Move camera to the first pub location. this was going ot be for further functionality but i ran out of time.
                            if (pubSnapshot == dataSnapshot.children.first()) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15f))
                            }
                        } ?: run {
                            Log.e("MapsScreen", "Failed to geocode address: ${it.address}")
                        }
                    }
                }
            }

            //from here on is debugging when i was running into issues

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("MapsScreen", "Database error: ${databaseError.message}")
            }
        })
    }


    //the geo coding is fone here with a GeoCoder subclass. convert the addresses to readable ones known as geocoding
    private fun getLocationFromAddress(address: String): LatLng? {
        val coder = Geocoder(requireContext())
        return try {
            val addresses = coder.getFromLocationName(address, 5)
            if (addresses.isNullOrEmpty()) return null
            val location = addresses[0]
            LatLng(location.latitude, location.longitude)
        } catch (e: Exception) {
            Log.e("MapsScreen", "Geocoding error for address $address", e)
            null
        }
    }
}
