package com.example.catapult.data.repository


import com.example.catapult.data.remote.api.LeaderboardApi
import com.example.catapult.data.remote.dto.LeaderboardPostResponseDto
import com.example.catapult.data.remote.dto.QuizResultDto
import com.example.catapult.data.remote.dto.SubmitQuizResultDto
import javax.inject.Inject

class LeaderboardRepository @Inject constructor(
    private val leaderboardApi: LeaderboardApi
) {

    suspend fun getLeaderboard(category: Int = 1): List<QuizResultDto> {
        return leaderboardApi.getLeaderboard(category)
    }

    suspend fun postResult(result: SubmitQuizResultDto): LeaderboardPostResponseDto {
        return leaderboardApi.postResult(result)
    }
}
