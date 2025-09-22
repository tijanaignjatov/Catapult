package com.example.catapult.presentation.screens.leaderboard

import com.example.catapult.data.remote.dto.QuizResultDto

object LeaderboardContract {

    data class UiState(
        val results: List<QuizResultDto> = emptyList(),
        val isLoading: Boolean = false,
        val errorMessage: String? = null
    )

    sealed class UiEvent {
        object LoadLeaderboard : UiEvent()
    }

    sealed class SideEffect {
        data class ShowToast(val message: String) : SideEffect()
    }
}
