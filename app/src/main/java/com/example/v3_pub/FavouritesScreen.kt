package com.example.v3_pub

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.compose.rememberImagePainter

//this is the favourites screen. Its not working unfortuantley, i think its to do with the data set up
// I used composables here to show pubs

class FavouritesScreen: Fragment() {

    private val viewModel: PubViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    FavouritesScreen(viewModel)
                }
            }
        }
    }
}

@Composable
fun FavouritesScreen(viewModel: PubViewModel) {
    val favouritePubs = viewModel.favouritePubs.observeAsState(emptyList())

    LazyColumn {                                //lazy list - very simple to do!!
        items(favouritePubs.value) { pub ->
            PubItem(pub)
        }
    }
}

@Composable
fun PubItem(pub: Pub) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, Color.White)
            .padding(8.dp)
    ) {
        Image(
            painter = rememberImagePainter(data = pub.image),
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = pub.name, style = MaterialTheme.typography.bodySmall)
        }
    }
}
