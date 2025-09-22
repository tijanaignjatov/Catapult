package com.example.catapult.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class WeightDto(
    val imperial: String,
    val metric: String
)
