package com.example.catapult.presentation.screens.breedgallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catapult.data.repository.BreedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BreedGalleryViewModel @Inject constructor(
    private val repository: BreedRepository
) : ViewModel() {

    private val _state = MutableStateFlow(BreedGalleryContract.UiState())
    val state: StateFlow<BreedGalleryContract.UiState> = _state

    private val _effect = Channel<BreedGalleryContract.SideEffect>()
    val effect = _effect.receiveAsFlow()

    private val events = MutableSharedFlow<BreedGalleryContract.UiEvent>()

    init {
        observeEvents()
    }

    fun loadPhotos(breedId: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true)
            try {
                // Ovde koristimo Flow za čitanje iz lokalne baze
                val photos = repository.observePhotosForBreed(breedId).first()

                _state.value = _state.value.copy(photos = photos, loading = false)
            } catch (e: Exception) {
                _state.value = _state.value.copy(loading = false)
                _effect.send(BreedGalleryContract.SideEffect.ShowToast("Failed to load photos"))
            }
        }
    }

    fun setEvent(event: BreedGalleryContract.UiEvent) = viewModelScope.launch {
        events.emit(event)
    }

    private fun observeEvents() {
        viewModelScope.launch {
            events.collect { event ->
                when (event) {
                    is BreedGalleryContract.UiEvent.OpenPhotoViewer -> {
                        _effect.send(BreedGalleryContract.SideEffect.NavigateToPhotoViewer(event.startIndex))
                    }
                }
            }
        }
    }
}
