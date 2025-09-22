package com.example.catapult.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class SubmitQuizResultDto(
    val nickname: String,
    val result: Double,
    val category: Int,
)
