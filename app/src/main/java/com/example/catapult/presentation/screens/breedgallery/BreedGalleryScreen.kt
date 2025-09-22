package com.example.catapult.presentation.screens.breedgallery

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@Composable
fun BreedGalleryScreen(
    viewModel: BreedGalleryViewModel,
    breedId: String,
    onNavigateToPhotoViewer: (Int) -> Unit
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    
    LaunchedEffect(Unit) {
        viewModel.loadPhotos(breedId)

        viewModel.effect.collect { effect ->
            when (effect) {
                is BreedGalleryContract.SideEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
                is BreedGalleryContract.SideEffect.NavigateToPhotoViewer -> {
                    onNavigateToPhotoViewer(effect.startIndex)
                }
            }
        }
    }

    BreedGalleryContent(
        state = state,
        onImageClick = { index ->
            viewModel.setEvent(BreedGalleryContract.UiEvent.OpenPhotoViewer(index))
        }
    )
}

@Composable
fun BreedGalleryContent(
    state: BreedGalleryContract.UiState,
    onImageClick: (Int) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        when {
            state.loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            state.photos.isNotEmpty() -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 24.dp,
                        bottom = 24.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(state.photos) { index, photo ->
                        Image(
                            painter = rememberAsyncImagePainter(photo.url),
                            contentDescription = null,
                            modifier = Modifier
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { onImageClick(index) }
                        )
                    }
                }
            }

            else -> {
                Text("Nema fotografija", modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}
