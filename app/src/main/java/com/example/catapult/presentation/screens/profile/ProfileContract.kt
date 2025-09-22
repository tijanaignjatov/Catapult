package com.example.catapult.presentation.screens.profile

import com.example.catapult.data.local.database.entities.QuizResultEntity

object ProfileContract {

    data class UiState(
        val name: String? = null,
        val nickname: String? = null,
        val email: String? = null,
        val isLoading: Boolean = true,

        val results: List<QuizResultEntity> = emptyList(),
        val bestScore: Float = 0f,
        val leaderboardPosition: Int? = null
    )

    sealed class UiEvent {
        object LoadUser : UiEvent()
        object Logout : UiEvent()
    }

    sealed class SideEffect {
        data class ShowToast(val message: String) : SideEffect()
        object LogoutSuccess : SideEffect()
    }
}
