package com.example.v3_pub

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

//this is the repsoitory that interacts with firebase

class PubRepository {
    // Reference to Firebase instance and node for pubs in schema
    private val db = FirebaseDatabase.getInstance("https://reviewpub-75274-default-rtdb.europe-west1.firebasedatabase.app")
    private val pubRef = db.getReference("pubs/pubs")

//getpubs get the data from firebase and using the Pub class it uses livedata to bind data to that type. event listeners looks
    //for data changes

    fun getPubs(): LiveData<List<Pub>> {
        val liveData = MutableLiveData<List<Pub>>()
        pubRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val pubs = mutableListOf<Pub>()
                snapshot.children.forEach { child ->
                    val pub = child.getValue(Pub::class.java)
                    pub?.let { pubs.add(it) }
                }
                liveData.value = pubs
            }

            override fun onCancelled(error: DatabaseError) {
                // log was here but i took it out because it kept crashing the app - not sure why
            }
        })
        return liveData
    }
}
