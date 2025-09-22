package com.example.catapult.data.remote.dto


import kotlinx.serialization.Serializable

@Serializable
data class LeaderboardPostResponseDto(
    val result: QuizResultDto,
    val ranking: Int
)
