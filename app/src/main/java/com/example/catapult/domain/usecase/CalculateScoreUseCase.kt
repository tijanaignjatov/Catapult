package com.example.catapult.domain.usecase


import javax.inject.Inject


class CalculateScoreUseCase @Inject constructor() {

    fun calculateScore(correctAnswers: Int, remainingTime: Int): Float {
        val mvt = 300.0f
        val ubp = correctAnswers * 2.5f * (1 + ( (remainingTime + 120).toFloat() / mvt ))
        return ubp.coerceAtMost(100.0f)
    }
}

