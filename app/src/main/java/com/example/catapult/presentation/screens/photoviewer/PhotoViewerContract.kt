package com.example.catapult.presentation.screens.photoviewer

import com.example.catapult.data.local.database.entities.PhotoEntity

object PhotoViewerContract {

    data class UiState(
        val loading: Boolean = false,
        val photos: List<PhotoEntity> = emptyList()
    )
}
