package com.example.catapult.presentation.screens.breeds

import com.example.catapult.data.local.database.entities.BreedEntity

object BreedsContract {

    data class UiState(
        val isLoading: Boolean = false,
        val breeds: List<BreedEntity> = emptyList(),
        val errorMessage: String? = null
    )

    sealed class UiEvent {
        object LoadBreeds : UiEvent()
        data class Search(val query: String) : UiEvent()
    }

    sealed class SideEffect {
        data class ShowToast(val message: String) : SideEffect()
    }
}
