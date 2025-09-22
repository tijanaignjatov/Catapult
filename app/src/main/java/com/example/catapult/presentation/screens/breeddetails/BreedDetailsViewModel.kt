package com.example.catapult.presentation.screens.breeddetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catapult.data.repository.BreedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BreedDetailsViewModel @Inject constructor(
    private val repository: BreedRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(BreedDetailsContract.UiState())
    val state: StateFlow<BreedDetailsContract.UiState> = _state

    private val _effect = Channel<BreedDetailsContract.SideEffect>()
    val effect = _effect.receiveAsFlow()

    private val breedId: String = savedStateHandle["breedId"] ?: ""

    private val events = MutableSharedFlow<BreedDetailsContract.UiEvent>()

    init {
        observeEvents()
        setEvent(BreedDetailsContract.UiEvent.LoadBreed(breedId))
    }

    fun setEvent(event: BreedDetailsContract.UiEvent) = viewModelScope.launch {
        events.emit(event)
    }

    private fun observeEvents() {
        viewModelScope.launch {
            events.collect { event ->
                when (event) {
                    is BreedDetailsContract.UiEvent.LoadBreed -> loadBreed(event.id)
                    is BreedDetailsContract.UiEvent.OpenGallery -> {
                        _effect.send(BreedDetailsContract.SideEffect.NavigateToGallery(breedId))
                    }
                }
            }
        }
    }

    private fun loadBreed(id: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                val breeds = repository.observeAllBreeds().first()
                val photos = repository.observePhotosForBreed(id).first()

                val breed = breeds.find { it.id == id }
                val photoUrl = photos.firstOrNull()?.url

                _state.value = _state.value.copy(
                    breed = breed,
                    photoUrl = photoUrl,
                    gallery = photos,
                    isLoading = false
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(isLoading = false)
                _effect.send(BreedDetailsContract.SideEffect.ShowToast("Failed to load: ${e.message}"))
            }
        }
    }

}
