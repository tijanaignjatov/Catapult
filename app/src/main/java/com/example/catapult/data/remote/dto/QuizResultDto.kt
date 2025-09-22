package com.example.catapult.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class QuizResultDto(
    val nickname: String,
    val result: Double,
    val category: Int,
    val createdAt: Long
)
