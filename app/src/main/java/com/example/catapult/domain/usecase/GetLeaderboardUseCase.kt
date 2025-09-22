package com.example.catapult.domain.usecase


import com.example.catapult.data.remote.dto.QuizResultDto
import com.example.catapult.data.repository.LeaderboardRepository
import javax.inject.Inject

class GetLeaderboardUseCase @Inject constructor(
    private val leaderboardRepository: LeaderboardRepository
) {
    suspend operator fun invoke(category: Int = 1): List<QuizResultDto> {
        return leaderboardRepository.getLeaderboard(category)
    }
}
