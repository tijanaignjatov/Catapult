package com.example.catapult.presentation.screens.photoviewer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catapult.data.repository.BreedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

@HiltViewModel
class PhotoViewerViewModel @Inject constructor(
    private val repository: BreedRepository
) : ViewModel() {

    private val _state = MutableStateFlow(PhotoViewerContract.UiState())
    val state: StateFlow<PhotoViewerContract.UiState> = _state

    fun observePhotos(breedId: String) {
        viewModelScope.launch {
            repository.observePhotosForBreed(breedId)
                .onStart { _state.value = _state.value.copy(loading = true) }
                .catch { _state.value = _state.value.copy(loading = false) }
                .collect { photos ->
                    _state.value = _state.value.copy(
                        photos = photos,
                        loading = false
                    )
                }
        }
    }
}
