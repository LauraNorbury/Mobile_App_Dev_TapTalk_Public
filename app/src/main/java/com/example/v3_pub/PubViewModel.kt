package com.example.v3_pub
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.v3_pub.Pub
import com.example.v3_pub.PubRepository

//sets up the viewModel using the format from the repository.
//
// Returns a LiveData object that contains a list of Pub objects

class PubViewModel : ViewModel() {
    private val repository = PubRepository()
    val pubs: LiveData<List<Pub>> = repository.getPubs()  //call getPubs from the repository.

    private val _favouritePubs = MutableLiveData<List<Pub>>()
    val favouritePubs: LiveData<List<Pub>> get() = _favouritePubs

    fun updateFavoritePubs() {
        _favouritePubs.value = pubs.value?.filter { it.isFavourite }
    }
}
