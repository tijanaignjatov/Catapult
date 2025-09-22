package com.example.catapult.presentation.screens.breedgallery

import com.example.catapult.data.local.database.entities.PhotoEntity

object BreedGalleryContract {

    data class UiState(
        val loading: Boolean = false,
        val photos: List<PhotoEntity> = emptyList(),
    )

    sealed class UiEvent {
        data class OpenPhotoViewer(val startIndex: Int) : UiEvent()
    }

    sealed class SideEffect {
        data class NavigateToPhotoViewer(val startIndex: Int) : SideEffect()
        data class ShowToast(val message: String) : SideEffect()
    }
}
