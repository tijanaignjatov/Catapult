package com.example.catapult.presentation.screens.breeddetails

import com.example.catapult.data.local.database.entities.BreedEntity
import com.example.catapult.data.local.database.entities.PhotoEntity

object BreedDetailsContract {

    data class UiState(
        val breed: BreedEntity? = null,
        val photoUrl: String? = null,
        val gallery: List<PhotoEntity> = emptyList(),
        val isLoading: Boolean = false,
        val errorMessage: String? = null
    )

    sealed class UiEvent {
        data class LoadBreed(val id: String) : UiEvent()
        data class OpenGallery(val startIndex: Int) : UiEvent()


    }

    sealed class SideEffect {
        data class ShowToast(val message: String) : SideEffect()
        data class NavigateToGallery(val breedId: String) : SideEffect()
    }


}
