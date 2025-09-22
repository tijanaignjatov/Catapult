package com.example.catapult.presentation.screens.breeddetails

import android.content.Intent
import android.net.Uri
import android.widget.Toast

import androidx.compose.foundation.Image

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.TopAppBar
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.catapult.data.local.database.entities.PhotoEntity


@Composable
fun BreedDetailsScreen(
    viewModel: BreedDetailsViewModel = hiltViewModel(),
    onNavigateToGallery: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is BreedDetailsContract.SideEffect.ShowToast -> {  Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show() }
                is BreedDetailsContract.SideEffect.NavigateToGallery -> {
                    onNavigateToGallery(effect.breedId)
                }
            }
        }
    }

    BreedDetailsContent(
        state = state,
        onPhotoClick = { viewModel.setEvent(BreedDetailsContract.UiEvent.OpenGallery(it)) },
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreedDetailsContent(
    state: BreedDetailsContract.UiState,
    onPhotoClick: (Int) -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

    if (state.isLoading) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
        return
    }

    state.breed?.let { breed ->
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(breed.name) },
                    navigationIcon = {
                        IconButton(onClick = { onBackClick() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Nazad")
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                state.photoUrl?.let { imageUrl ->
                    Image(
                        painter = rememberAsyncImagePainter(imageUrl),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }

                if (state.gallery.isNotEmpty()) {
                    Text("Galerija:", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp)
                    ) {
                        items(state.gallery.size) { index ->
                            val photo = state.gallery[index]
                            GalleryItem(photo = photo) { onPhotoClick(index) }
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }

                Text("Poreklo:", style = MaterialTheme.typography.labelLarge)
                Text(breed.origin, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(12.dp))

                Text("Životni vek:", style = MaterialTheme.typography.labelLarge)
                Text(breed.lifeSpan, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(12.dp))

                Text("Retkost:", style = MaterialTheme.typography.labelLarge)
                Spacer(modifier = Modifier.height(4.dp))
                AssistChip(
                    onClick = {},
                    label = {
                        Text(
                            text = if (breed.rare == 1) "Retka rasa" else "Nije retka",
                            style = MaterialTheme.typography.labelLarge
                        )
                    },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        labelColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))

                breed.altNames?.let {
                    if (it.isNotBlank()) {
                        Text("Alternativna imena:", style = MaterialTheme.typography.labelLarge)
                        Text(it, style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }

                Text("Temperament:", style = MaterialTheme.typography.labelLarge)
                Text(breed.temperament, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(12.dp))

                Text("Opis:", style = MaterialTheme.typography.labelLarge)
                Text(breed.description, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(24.dp))

                Text("Težina:", style = MaterialTheme.typography.labelLarge)
                Text("• Imperial: ${breed.weight.imperial}", style = MaterialTheme.typography.bodyMedium)
                Text("• Metric: ${breed.weight.metric}", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(24.dp))

                BehaviorBar("Adaptability", breed.adaptability)
                Spacer(modifier = Modifier.height(8.dp))
                BehaviorBar("Affection Level", breed.affection_level)
                Spacer(modifier = Modifier.height(8.dp))
                BehaviorBar("Child Friendly", breed.child_friendly)
                Spacer(modifier = Modifier.height(8.dp))
                BehaviorBar("Dog Friendly", breed.dog_friendly)
                Spacer(modifier = Modifier.height(8.dp))
                BehaviorBar("Energy Level", breed.energy_level)
                Spacer(modifier = Modifier.height(8.dp))
                BehaviorBar("Grooming", breed.grooming)
                Spacer(modifier = Modifier.height(8.dp))
                BehaviorBar("Health Issues", breed.health_issues)
                Spacer(modifier = Modifier.height(8.dp))
                BehaviorBar("Intelligence", breed.intelligence)
                Spacer(modifier = Modifier.height(8.dp))
                BehaviorBar("Shedding Level", breed.shedding_level)
                Spacer(modifier = Modifier.height(8.dp))
                BehaviorBar("Social Needs", breed.social_needs)
                Spacer(modifier = Modifier.height(8.dp))
                BehaviorBar("Stranger Friendly", breed.stranger_friendly)
                Spacer(modifier = Modifier.height(8.dp))
                BehaviorBar("Vocalisation", breed.vocalisation)

                Spacer(modifier = Modifier.height(32.dp))

                breed.wikipedia_url?.let { url ->
                    FilledTonalButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            context.startActivity(intent)
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text("Wikipedia")
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    } ?: run {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                "Greška: ${state.errorMessage ?: "Nepoznata greška"}",
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}



@Composable
fun GalleryItem(photo: PhotoEntity, onClick: () -> Unit) {
    Image(
        painter = rememberAsyncImagePainter(photo.url),
        contentDescription = null,
        modifier = Modifier
            .size(80.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
    )
}

@Composable
fun BehaviorBar(label: String, value: Int) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
        LinearProgressIndicator(
            progress = value / 5f,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}
