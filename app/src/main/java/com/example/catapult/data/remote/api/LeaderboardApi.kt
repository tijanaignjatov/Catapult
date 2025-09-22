package com.example.catapult.data.remote.api


import com.example.catapult.data.remote.dto.LeaderboardPostResponseDto
import com.example.catapult.data.remote.dto.QuizResultDto
import com.example.catapult.data.remote.dto.SubmitQuizResultDto
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.Query

interface LeaderboardApi {

    @GET("leaderboard")
    suspend fun getLeaderboard(
        @Query("category") category: Int = 1
    ): List<QuizResultDto>

    @POST("leaderboard")
    suspend fun postResult(
        @Body request: SubmitQuizResultDto
    ): LeaderboardPostResponseDto
}
