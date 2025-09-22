package com.example.catapult.presentation.screens.quiz

import com.example.catapult.domain.usecase.QuizQuestion

object QuizContract {

    data class UiState(
        val questions: List<QuizQuestion> = emptyList(),
        val currentIndex: Int = 0,
        val score: Float = 0f,
        val isLoading: Boolean = false,
        val isFinished: Boolean = false,
        val isCanceled: Boolean = false,
        val remainingTime: Int = 300,
        val errorMessage: String? = null,
        val isPublishing: Boolean = false
    )

    sealed class UiEvent {
        object LoadQuestions : UiEvent()
        data class SubmitAnswer(val answer: String) : UiEvent()
        object TimeTick : UiEvent()
        object Timeout : UiEvent()
        object CancelQuiz : UiEvent()
        object PublishResult : UiEvent()
        object ResetQuiz : UiEvent()
    }

    sealed class SideEffect {
        data class ShowToast(val message: String) : SideEffect()
        object NavigateToLeaderboard : SideEffect()
        object ShowCancelDialog : SideEffect()
        object PublishSuccess : SideEffect()
    }
}
