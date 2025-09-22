package com.example.catapult.data.remote.dto


import kotlinx.serialization.Serializable

@Serializable
data class PhotoDto(
    val id: String,
    val url: String,
    val breeds: List<BreedDto> = emptyList()
)
